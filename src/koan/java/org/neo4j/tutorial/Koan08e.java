package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;

import static junit.framework.Assert.assertEquals;

/**
 * In this Koan we focus on calling Neo4j's library of graph algorithms from Cypher
 */
@Ignore
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
    public void shouldFindHowManyRegenerationsBetweenTomBakerAndChristopherEccleston() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start eccleston = node:actors(actor = 'Christopher Eccleston'), baker = node:actors(actor = 'Tom Baker') "
                + "match p=(baker)-[:REGENERATED_TO*]->(eccleston) "
                + "return length(p) as regenerations";


        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);

        assertEquals(5, result.javaColumnAs("regenerations").next());
    }
}
