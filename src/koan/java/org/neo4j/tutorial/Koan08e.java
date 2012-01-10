package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;

import static org.junit.Assert.assertEquals;

/**
 * In this Koan we focus on paths in Cypher.
 */
public class Koan08e
{
    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse(new DoctorWhoUniverseGenerator());
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Test
    public void shouldFindTheNumberOfEpisodesUsingShortestPath() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start first=node:episodes(episode='1'), last=node:episodes(episode='224') "
            + "match path = shortestPath( first-[*..500]->last )"
            + "return length(path) as episodes";


        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);

        assertEquals(245, result.javaColumnAs("episodes").next());
    }
}