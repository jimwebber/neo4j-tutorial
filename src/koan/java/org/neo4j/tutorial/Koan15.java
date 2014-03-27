package org.neo4j.tutorial;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.collection.convert.Wrappers;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;

/**
 * In this koan we get to grips with collection functions.
 */
public class Koan15
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
    public void shouldListTheFirstFiveEpisodes() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH p=(e:Episode { episode: '1'})-[:NEXT*5..5]->(:Episode)\n" +
                "RETURN extract( e in nodes(p) | e.title)  AS episodes\n";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        assertThat( result, containsOnly( "An Unearthly Child", "The Daleks", "The Edge of Destruction",
                "Marco Polo", "The Keys of Marinus", "The Aztecs" ) );
    }

    @Test
    public void shouldAddStoryArcOnRelationshipsBetweenEpisodes() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = "MATCH p=(startEp:Episode {title: 'The Ribos Operation'})-[:NEXT*]->" +
                "(endEp:Episode {title: 'The Armageddon Factor'})\n";

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql += "FOREACH (r in relationships(p) | SET r.story_arc = 'The Key to Time')";

        // SNIPPET_END

        engine.execute( cql );

        ExecutionResult result = engine.execute( "MATCH p=(startEp:Episode {title: 'The Ribos Operation'})" +
                "-[:NEXT*]->(endEp:Episode {title: " +
                "'The Armageddon Factor'})\n" +
                " WITH relationships(p) AS rels \n" +
                "RETURN length(extract(x IN rels | x.story_arc)) as count" );

        assertEquals( 5, result.javaColumnAs( "count" ).next() );

    }

    private TypeSafeMatcher<ExecutionResult> containsOnly( final String... episodes )
    {
        return new TypeSafeMatcher<ExecutionResult>()
        {
            @Override
            protected boolean matchesSafely( ExecutionResult result )
            {

                Wrappers.SeqWrapper eps = (Wrappers.SeqWrapper) result.javaColumnAs( "episodes" ).next();

                if ( eps.size() != episodes.length )
                {
                    return false;
                }

                for ( String episode : episodes )
                {
                    if ( !eps.contains( episode ) )
                    {
                        return false;
                    }
                }

                return true;
            }

            @Override
            public void describeTo( Description description )
            {

            }
        };
    }
}
