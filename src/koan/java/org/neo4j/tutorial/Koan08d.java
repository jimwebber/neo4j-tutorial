package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;

import static junit.framework.Assert.assertEquals;

/**
 * In this Koan we focus on paths in Cypher.
 */
public class Koan08d
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
    public void shouldFindHowManyRegenerationsBetweenTomBakerAndChristopherEccleston() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start eccleston = node:actors(actor = 'Christopher Eccleston'), baker = node:actors(actor = 'Tom Baker') "
                + "match path = (baker)-[:REGENERATED_TO*]->(eccleston) "
                + "return length(path) as regenerations";


        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);

        assertEquals(5, result.javaColumnAs("regenerations").next());
    }

    @Test
    public void shouldFindTheLongestContinuousStoryArcWithTheMaster() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start master = node:characters(character = 'Master')\n" +
                "match (master)-[:APPEARED_IN]->(first), storyArcs = (first)-[:NEXT*]->()"+
                "where all(ep in nodes(storyArcs) where master-[:APPEARED_IN]->ep)"+
                "return length(storyArcs) as noOfPathHops\n" +
                "order by noOfPathHops desc limit 1";


        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);

        // noOfPathHops is one less than the number of episodes in a story arc
        final int noOfStories = 5;
        assertEquals(noOfStories - 1, result.javaColumnAs("noOfPathHops").next());
    }
}
