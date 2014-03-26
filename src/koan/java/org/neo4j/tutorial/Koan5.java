package org.neo4j.tutorial;

import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.cypher.ExecutionEngine;
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

        assertThat( asIterable( iterator ), containsExactly( "Karen Gillan", "Caitlin Blackwood" ) );
    }

    private TypeSafeMatcher<Iterable<Object>> containsExactly( final String... actorNames )
    {
        final Set<String> actors = asSet( actorNames );

        return new TypeSafeMatcher<Iterable<Object>>()
        {
            @Override
            protected boolean matchesSafely( Iterable<Object> objects )
            {
                for ( Object object : objects )
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
