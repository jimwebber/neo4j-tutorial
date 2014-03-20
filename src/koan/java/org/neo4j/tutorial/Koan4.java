package org.neo4j.tutorial;

import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.neo4j.helpers.collection.IteratorUtil.asIterable;
import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificTitles.containsOnlyTitles;

/**
 * In this Koan we learn the basics of the Cypher query language, focusing on the
 * matching capabilities to RETURN subgraphs of information about the Doctor Who
 * universe.
 */
public class Koan4
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
    public void shouldFindAndReturnTheDoctor()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (doctor:Character {character: 'Doctor'}) RETURN doctor";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );
        Iterator<Node> containsTheDoctor = result.javaColumnAs( "doctor" );

        assertEquals( containsTheDoctor.next(), universe.theDoctor() );
    }

    @Test
    public void shouldFindAllTheEpisodesUsingLabels()
    {
        // The number of episodes is not the same as the highest episode number.
        // Some episodes are two-parters with the same episode number, others use schemes like
        // 218a and 218b as their episode numbers seemingly just to be difficult!

        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (episode:Episode) RETURN episode";


        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        Iterator<String> iterator = result.javaColumnAs( "episode" );

        assertEquals( 263l, count( iterator ) );
    }

    private long count( Iterator<String> result )
    {
        long count = 0;
        while ( result.hasNext() )
        {
            count++;
            result.next();
        }

        return count;
    }

    @Test
    public void shouldFindAllTheEpisodesInWhichTheCybermenAppeared() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (cybermen:Species {species: 'Cyberman'})-[:APPEARED_IN]->(episode:Episode) " +
                "RETURN episode";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        Iterator<Node> episodes = result.javaColumnAs( "episode" );

        assertThat( asIterable( episodes ), containsOnlyTitles( universe.getDatabase(),
                "Closing Time",
                "A Good Man Goes to War",
                "The Pandorica Opens",
                "The Next Doctor",
                "Doomsday",
                "Army of Ghosts",
                "The Age of Steel",
                "Rise of the Cybermen",
                "Silver Nemesis",
                "Earthshock",
                "Revenge of the Cybermen",
                "The Wheel in Space",
                "The Tomb of the Cybermen",
                "The Moonbase",
                "The Time of the Doctor",
                "Nightmare in Silver") );
    }

    @Test
    public void shouldFindEpisodesWhereTennantAndRoseBattleTheDaleks() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;


        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (tennant:Actor)-[:APPEARED_IN]->(episode:Episode), " +
                "(rose:Character)-[:APPEARED_IN]->(episode:Episode), " +
                "(daleks:Species)-[:APPEARED_IN]->(episode:Episode) " +
                "WHERE tennant.actor ='David Tennant' " +
                "AND rose.character = 'Rose Tyler' " +
                "AND daleks.species = 'Dalek' " +
                "RETURN episode";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        Iterator<Node> episodes = result.javaColumnAs( "episode" );

        assertThat( asIterable( episodes ),
                containsOnlyTitles( universe.getDatabase(),
                        "Journey's End",
                        "The Stolen Earth",
                        "Doomsday",
                        "Army of Ghosts",
                        "The Parting of the Ways" ) );
    }

    @Test
    public void shouldFindIndividualCompanionsAndEnemiesOfTheDoctor()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = "";
        try ( Transaction transaction = universe.getDatabase().beginTx() )
        {

            // YOUR CODE GOES HERE
            // SNIPPET_START

            cql += "MATCH (doctor:Character {character: 'Doctor'})<-[:ENEMY_OF|COMPANION_OF]-(other) ";
            cql += "WHERE has(other.character) ";
            cql += "RETURN DISTINCT other.character";

            // SNIPPET_END

            ExecutionResult result = engine.execute( cql );

            assertEquals( 159, result.size() );
            transaction.success();
        }
    }
}
