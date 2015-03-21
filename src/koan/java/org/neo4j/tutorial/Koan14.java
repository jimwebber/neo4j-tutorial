package org.neo4j.tutorial;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import scala.collection.convert.Wrappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * In this koan we get to grips with collection functions.
 */
public class Koan14
{
    @ClassRule
    static public DoctorWhoUniverseResource neo4jResource = new DoctorWhoUniverseResource();

    @Test
    public void shouldListTheFirstFiveEpisodes() throws Exception
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH p=(e:Episode { episode: '1'})-[:NEXT*5..5]->(:Episode)\n" +
                "RETURN extract( e in nodes(p) | e.title)  AS episodes\n";

        // SNIPPET_END

        Result result = db.execute( cql );

        assertThat( result, containsOnly( "An Unearthly Child", "The Daleks", "The Edge of Destruction",
                "Marco Polo", "The Keys of Marinus", "The Aztecs" ) );
    }

    @Test
    public void shouldAddStoryArcOnRelationshipsBetweenEpisodes() throws Exception
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        String cql = "MATCH p=(startEp:Episode {title: 'The Ribos Operation'})-[:NEXT*]->" +
                "(endEp:Episode {title: 'The Armageddon Factor'})\n";

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql += "FOREACH (r in relationships(p) | SET r.story_arc = 'The Key to Time')";

        // SNIPPET_END

        db.execute( cql );

        Result result = db.execute( "MATCH p=(startEp:Episode {title: 'The Ribos Operation'})" +
                "-[:NEXT*]->(endEp:Episode {title: " +
                "'The Armageddon Factor'})\n" +
                " WITH relationships(p) AS rels \n" +
                "RETURN length(extract(x IN rels | x.story_arc)) as count" );

        assertEquals( 5, result.columnAs( "count" ).next() );

    }

    private TypeSafeMatcher<Result> containsOnly( final String... episodes )
    {
        return new TypeSafeMatcher<Result>()
        {
            @Override
            protected boolean matchesSafely( Result result )
            {

                Wrappers.SeqWrapper eps = (Wrappers.SeqWrapper) result.columnAs( "episodes" ).next();

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
