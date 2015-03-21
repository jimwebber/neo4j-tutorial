package org.neo4j.tutorial;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.ClassRule;
import org.junit.Test;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;

import static org.junit.Assert.assertThat;

import static org.neo4j.helpers.collection.IteratorUtil.asIterable;
import static org.neo4j.helpers.collection.IteratorUtil.asSet;

/**
 * In this Koan we learn how to merge new nodes and relationships into an existing graph using
 * using the Cypher language.
 */
public class Koan5
{

    @ClassRule
    static public DoctorWhoUniverseResource neo4jResource = new DoctorWhoUniverseResource();

    @Test
    public void shouldBringActorsKarenGillanCaitlinBlackwoodIntoTheAmyPondSubgraph()
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        String cql = null;

        // Hint: Amy Pond is definitely in the graph, and the actors may be too. How can MERGE help?

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MERGE (amy:Character {character: 'Amy Pond'})\n" +
                "MERGE (amy)<-[:PLAYED]-(:Actor {actor: 'Karen Gillan'})\n" +
                "MERGE (amy)<-[:PLAYED]-(:Actor {actor: 'Caitlin Blackwood'})";

        // SNIPPET_END

        db.execute( cql );

        ResourceIterator<Object> iterator = db.execute( "MATCH (:Character {character: 'Amy Pond'})" +
                "<-[:PLAYED]-(a:Actor) RETURN a.actor" ).columnAs("a.actor");

        assertThat( iterator, containsExactly( "Karen Gillan", "Caitlin Blackwood" ) );
    }

    @Test
    public void shouldEnsureAmyAndRoryAreInLove()
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MERGE (amy:Character {character: 'Amy Pond'})\n" +
                "MERGE (rory:Character {character: 'Rory Williams'})\n" +
                "MERGE (amy)<-[:LOVES]-(rory)\n" +
                "MERGE (amy)-[:LOVES]->(rory)";

        // SNIPPET_END

        db.execute( cql );

        assertThat( db.execute( "MATCH (:Character {character: 'Amy Pond'})-[loves:LOVES]->(:Character " +
                "{character: 'Rory Williams'}) RETURN loves" ).columnAs("loves"), numbersExactly( 1 ) );
        assertThat( db.execute( "MATCH (:Character {character: 'Amy Pond'})<-[loves:LOVES]-(:Character " +
                "{character: 'Rory Williams'}) RETURN loves" ).columnAs("loves"), numbersExactly( 1 ) );
    }

    @Test
    public void shouldDemarcateYearsWhenAmyPondWasACompanionOfTheDoctorUsingOnMatchClauseOfMerge()
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MERGE (:Character {character: 'Amy Pond'})-[c:COMPANION_OF]->(:Character {character: 'Doctor'})" +
                "ON MATCH SET c.start = 2010, c.end = 2013";


        // SNIPPET_END

        db.execute( cql );

        assertThat( db.execute( "MATCH (:Character {character: 'Amy Pond'})" +
                "-[c:COMPANION_OF]->(:Character {character: 'Doctor'}) RETURN c.start, c.end" ),
                hasCorrectDatesForStartAndEnd( 2010, 2013 ) );

    }

    private TypeSafeMatcher<Result> hasCorrectDatesForStartAndEnd( final long start, final long end )
    {
        return new TypeSafeMatcher<Result>()
        {
            @Override
            protected boolean matchesSafely( Result result )
            {
                Map<String, Object> map = result.next();

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
