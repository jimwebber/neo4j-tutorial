package org.neo4j.tutorial;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.kernel.impl.util.StringLogger;

import static org.junit.Assert.assertThat;

import static org.neo4j.helpers.collection.IteratorUtil.asIterable;
import static org.neo4j.helpers.collection.IteratorUtil.asSet;

/**
 * In this Koan we learn how to merge new nodes and relationships into an existing graph using
 * using the Cypher language.
 */
public class Koan5
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
    public void shouldBringActorsKarenGillanCaitlinBlackwoodIntoTheAmyPondSubgraph()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // Hint: Amy Pond is definitely in the graph, and the actors may be too. How can MERGE help?

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MERGE (amy:Character {character: 'Amy Pond'})<-[:PLAYED]-(:Actor {actor: 'Karen Gillan'})\n" +
                "MERGE (amy)<-[:PLAYED]-(:Actor {actor: 'Caitlin Blackwood', age: 6})";

        // SNIPPET_END

        engine.execute( cql );

        ResourceIterator<Object> iterator = engine.execute( "MATCH (:Character {character: 'Amy Pond'})" +
                "<-[:PLAYED]-(a:Actor) RETURN a.actor" ).javaColumnAs( "a.actor" );

        assertThat( iterator, containsExactly( "Karen Gillan", "Caitlin Blackwood" ) );
    }

    @Test
    public void shouldEnsureAmyAndRoryAreInLove()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MERGE (amy:Character {character: 'Amy Pond'})-[:LOVES]->(rory:Character {character: 'Rory Williams'})" +
                "\n" +
                "MERGE (amy)<-[:LOVES]-(rory)\n";

        // SNIPPET_END

        engine.execute( cql );

        assertThat( engine.execute( "MATCH (:Character {character: 'Amy Pond'})-[loves:LOVES]->(:Character " +
                "{character: 'Rory Williams'}) RETURN loves" ).javaColumnAs( "loves" ), numbersExactly( 1 ) );
        assertThat( engine.execute( "MATCH (:Character {character: 'Amy Pond'})<-[loves:LOVES]-(:Character " +
                "{character: 'Rory Williams'}) RETURN loves" ).javaColumnAs( "loves" ), numbersExactly( 1 ) );
    }

    @Test
    public void shouldDemarcateYearsWhenAmyPondWasACompanionOfTheDoctor()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MERGE (:Character {character: 'Amy Pond'})-[c:COMPANION_OF]->(:Character {character: 'Doctor'})" +
                "ON MATCH SET c.start = 2010, c.end = 2013";


        // SNIPPET_END

        engine.execute( cql );

        assertThat( engine.execute( "MATCH (:Character {character: 'Amy Pond'})" +
                "-[c:COMPANION_OF]->(:Character {character: 'Doctor'}) RETURN c.start, c.end" ),
                hasCorrectDatesForStartAndEnd( 2010, 2013 ) );

    }

    private TypeSafeMatcher<ExecutionResult> hasCorrectDatesForStartAndEnd( final long start, final long end )
    {
        return new TypeSafeMatcher<ExecutionResult>()
        {
            @Override
            protected boolean matchesSafely( ExecutionResult result )
            {
                Map<String, Object> map = result.javaIterator().next();

                boolean answer = map.containsKey( "c.start" ) && map.get( "c.start" ).equals( start );
                answer = answer && map.containsKey( "c.end" ) && map.get( "c.end" ).equals( end );

                return answer;
            }

            @Override
            public void describeTo( Description description )
            {
                description.appendText( "" );
            }
        };
    }

    private TypeSafeMatcher<ResourceIterator<Object>> numbersExactly( final int i )
    {
        return new TypeSafeMatcher<ResourceIterator<Object>>()
        {

            @Override
            protected boolean matchesSafely( ResourceIterator<Object> relationships )
            {
                return i == DatabaseHelper.destructivelyCount( relationships );
            }

            @Override
            public void describeTo( Description description )
            {
                description.appendText( "" );
            }
        };
    }


    private TypeSafeMatcher<Iterator<Object>> containsExactly( final String... actorNames )
    {
        final Set<String> actors = asSet( actorNames );

        return new TypeSafeMatcher<Iterator<Object>>()
        {
            @Override
            protected boolean matchesSafely( Iterator<Object> objects )
            {
                for ( Object object : asIterable( objects ) )
                {
                    if ( !actors.remove( object ) )
                    {
                        return false;
                    }
                }

                return actors.size() == 0;
            }

            @Override
            public void describeTo( Description description )
            {
                description.appendText( "" );
            }
        };
    }
}
