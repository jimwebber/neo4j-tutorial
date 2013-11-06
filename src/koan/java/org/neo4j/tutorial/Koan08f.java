package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;

import static junit.framework.Assert.assertEquals;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;

/**
 * In this Koan we focus on paths in Cypher.
 */
public class Koan08f
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
    public void shouldFindHowManyRegenerationsBetweenTomBakerAndChristopherEccleston() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH path = (baker:Actor)-[:REGENERATED_TO*]->(eccleston:Actor) " +
                "WHERE eccleston.actor = 'Christopher Eccleston'\n" +
                "AND baker.actor = 'Tom Baker'" +
                "RETURN LENGTH(path) as regenerations";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        assertEquals( 5, result.javaColumnAs( "regenerations" ).next() );
    }

    @Test
    public void shouldFindTheLongestContinuousStoryArcWithTheMaster() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (master:Character)-[:APPEARED_IN]->(first:Episode), storyArcs = (first:Episode)-[:NEXT*]->()" +
                "WHERE master.character = 'Master' " +
                "AND ALL(ep in nodes(storyArcs) WHERE master-[:APPEARED_IN]->ep)" +
                "RETURN LENGTH(storyArcs) as noOfPathHops\n" +
                "ORDER BY noOfPathHops DESC LIMIT 1";


        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        // noOfPathHops is one less than the number of episodes in a story arc
        final int noOfStories = 5;
        assertEquals( noOfStories - 1, result.javaColumnAs( "noOfPathHops" ).next() );
    }
}
