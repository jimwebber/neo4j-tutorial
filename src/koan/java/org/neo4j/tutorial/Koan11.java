package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;

import static org.junit.Assert.assertEquals;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;

/**
 * In this koan we call out to the (small!) algorithm library exposed to Cypher to peform shortest path calculations.
 */
public class Koan11
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
    public void shouldFindLengthOfTheShortestPathBetweenSarahJaneSmithAndSkaro() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH path = shortestPath( (sarahJaneSmith:Character)-[*..50]-(skaro:Planet) )" +
                "WHERE sarahJaneSmith.character = 'Sarah Jane Smith' " +
                "AND skaro.planet = 'Skaro'" +
                "RETURN length(path) as length";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        assertEquals( 3, result.javaColumnAs( "length" ).next() );
    }
}