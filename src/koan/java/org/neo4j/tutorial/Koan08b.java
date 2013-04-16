package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.neo4j.helpers.collection.IteratorUtil.asIterable;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificTitles.containsOnlyTitles;

import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.kernel.impl.util.StringLogger;

/**
 * In this Koan we learn the basics of the Cypher query language, focusing on the
 * matching capabilities to RETURN subgraphs of information about the Doctor Who
 * universe.
 */
public class Koan08b
{
    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator() );
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Test
    public void shouldFindAndReturnTheDoctor()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "START doctor = node:characters(character='Doctor') RETURN doctor";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );
        Iterator<Node> episodes = result.javaColumnAs( "doctor" );

        assertEquals( episodes.next(), universe.theDoctor() );
    }

    @Test
    public void shouldFindAllTheEpisodesUsingIndexesOnly()
    {
        // The number of episodes is not the same as the highest episode number.
        // Some episodes are two-parters with the same episode number, others use schemes like
        // 218a and 218b as their episode numbers seemingly just to be difficult!

        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "START episodes= node:episodes('episode:*') "
                + "RETURN episodes";


        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        assertEquals( 252l, result.length() );
    }


    @Test
    public void shouldFindAllTheEpisodesInWhichTheCybermenAppeared() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "START cybermen = node:species(species ='Cyberman') MATCH (cybermen)-[:APPEARED_IN]->(episode) RETURN episode";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );
        Iterator<Node> episodes = result.javaColumnAs( "episode" );

        assertThat( asIterable( episodes ), containsOnlyTitles( "Closing Time",
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
                "The Moonbase" ) );
    }

    @Test
    public void shouldFindEpisodesWhereTennantAndRoseBattleTheDaleks() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "START daleks = node:species(species = 'Dalek'), rose = node:characters(character = 'Rose Tyler'), tennant = node:actors(actor = 'David Tennant')";
        cql += "MATCH (tennant)-[:APPEARED_IN]->(episode), (rose)-[:APPEARED_IN]->(episode), (daleks)-[:APPEARED_IN]->(episode)";
        cql += "RETURN episode";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );
        Iterator<Node> episodes = result.javaColumnAs( "episode" );

        assertThat( asIterable( episodes ),
                containsOnlyTitles( "Journey's End", "The Stolen Earth", "Doomsday", "Army of Ghosts",
                        "The Parting of the Ways" ) );
    }

    @Test
    public void shouldFindIndividualCompanionsAndEnemiesOfTheDoctor()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), StringLogger.DEV_NULL );
        String cql = "";

        // YOUR CODE GOES HERE

        // SNIPPET_START

        cql = "START doctor = node:characters(character= 'Doctor') ";
        cql += "MATCH (doctor)<-[:ENEMY_OF|COMPANION_OF]-(other) ";
        cql += "WHERE has(other.character) ";
        cql += "RETURN distinct other.character";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        assertEquals( 156, result.size() );
    }
}
