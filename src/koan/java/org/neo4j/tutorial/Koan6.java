package org.neo4j.tutorial;

import java.util.Map;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.tooling.GlobalGraphOperations;

import static java.lang.String.format;

import static org.junit.Assert.assertThat;

import static org.neo4j.helpers.collection.IteratorUtil.asSet;
import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;

/**
 * In this Koan we use OPTIONAL MATCH to loosen the pattern to include optionally connected nodes.
 */
public class Koan6
{
    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator().getDatabase() );
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Test
    public void shouldReturnTheCharactersAndTheThingsTheyOwn() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // Hint: use 'c' for character column name and 't' for thing column name

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (c:Character)\n" +
                "OPTIONAL MATCH (c)-[:OWNS]->(t:Thing)" +
                "RETURN c.character, t.thing";


        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        assertThat( result, containsOwnersAndThings( asSet(
                pair( "Doctor", "Tardis" ),
                pair( "Doctor", "Sonic Screwdriver" ),
                pair( "Master", "Tardis" ),
                pair( "Rani", "Tardis" ),
                pair( "Meddling Monk", "Tardis" ),
                pair( "Ace", null ) ) ) );
    }

    @Test
    public void shouldVisitAllNodesAndRelationships() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (a)\n" +
                "OPTIONAL MATCH (a)-[r]-()\n" +
                "SET a.visited = true \n" +
                "SET r.visited = true \n";


        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        assertThat( universe.getDatabase(), allNodesAndRelsVisited() );

    }

    private TypeSafeMatcher<GraphDatabaseService> allNodesAndRelsVisited()
    {
        return new TypeSafeMatcher<GraphDatabaseService>()
        {
            @Override
            protected boolean matchesSafely( GraphDatabaseService graphDatabaseService )
            {
                try ( Transaction tx = graphDatabaseService.beginTx() )
                {
                    GlobalGraphOperations globalGraphOperations = GlobalGraphOperations.at( graphDatabaseService );

                    boolean result = ensureVisited( globalGraphOperations.getAllNodes() ) &&
                            ensureVisited( globalGraphOperations.getAllRelationships() );


                    tx.success();

                    return result;
                }
            }

            private boolean ensureVisited( Iterable<? extends PropertyContainer> propertyContainers )
            {
                for ( PropertyContainer container : propertyContainers )
                {
                    if ( !(container.hasProperty( "visited" ) && container.getProperty( "visited" ).equals( "true" )) )
                    {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void describeTo( Description description )
            {
                description.appendText( "" );
            }
        };
    }

    private TypeSafeMatcher<ExecutionResult> containsOwnersAndThings( final Set<Pair<String, String>> pairs )
    {
        return new TypeSafeMatcher<ExecutionResult>()
        {
            int numberOfPairs = pairs.size();

            @Override
            protected boolean matchesSafely( ExecutionResult executionResult )
            {
                ResourceIterator<Map<String, Object>> mapResourceIterator = executionResult.javaIterator();

                for ( Map<String, Object> stringObjectMap : IteratorUtil.asIterable( mapResourceIterator ) )
                {
                    for ( Pair pair : pairs )
                    {
                        if ( pair.getKey().equals( stringObjectMap.get( "c.character" ) ) &&
                                stringObjectMap.get( "t.thing" ).equals( pair.getValue() ) )
                        {
                            numberOfPairs--;
                            break;
                        }
                    }
                }

                return numberOfPairs == 0;
            }

            @Override
            public void describeTo( Description description )
            {
                description.appendText( format( "There were [%d] unmatched character/thing pairs.", numberOfPairs ) );
            }
        };
    }


    private Pair<String, String> pair( String character, String thing )
    {
        return new Pair( character, thing );
    }

    private class Pair<K, V>
    {
        private final K key;
        private final V value;

        public Pair( K key, V value )
        {
            this.key = key;
            this.value = value;
        }

        public K getKey()
        {
            return key;
        }

        public V getValue()
        {
            return value;
        }
    }
}
