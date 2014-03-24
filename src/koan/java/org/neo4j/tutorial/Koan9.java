package org.neo4j.tutorial;

import java.util.Iterator;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;

/**
 * In this Koan we use the Cypher graph pattern matching language to investigate
 * the regenerations and the history of the Dalek props, with a focus on longer matches
 * and using aggregates to process the returned data.
 */
public class Koan9
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
    public void shouldFindTheHardestWorkingPropPartInShowbiz() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (daleks:Species {species: \"Dalek\"})-[:APPEARED_IN]->(episode:Episode)<-[:USED_IN]-(:Props)" +
                "<-[:MEMBER_OF]-(:Prop)-[:COMPOSED_OF]->(part:Part)-[:ORIGINAL_PROP]->(originalprop:Prop)" +
                System.lineSeparator() +
                "RETURN originalprop.prop, part.part, count(episode) ORDER BY count(episode) DESC LIMIT 1";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        assertHardestWorkingPropParts( result.javaIterator(), "Dalek 1", "shoulder", 15l );
    }

    private void assertHardestWorkingPropParts( Iterator<Map<String, Object>> results, Object... partsAndCounts )
    {
        for ( int index = 0; index < partsAndCounts.length; index = index + 3 )
        {
            Map<String, Object> row = results.next();
            assertEquals( partsAndCounts[index], row.get( "originalprop.prop" ) );
            assertEquals( partsAndCounts[index + 1], row.get( "part.part" ) );
            assertEquals( partsAndCounts[index + 2], row.get( "count(episode)" ) );
        }

        assertFalse( results.hasNext() );
    }
}
