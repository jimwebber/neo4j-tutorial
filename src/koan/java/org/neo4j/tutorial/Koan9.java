package org.neo4j.tutorial;

import java.util.Iterator;
import java.util.Map;

import org.junit.ClassRule;
import org.junit.Test;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * In this Koan we use the Cypher graph pattern matching language to investigate
 * the regenerations and the history of the Dalek props, with a focus on longer matches
 * and using aggregates to process the returned data.
 */
public class Koan9
{
    @ClassRule
    static public DoctorWhoUniverseResource neo4jResource = new DoctorWhoUniverseResource();

    @Test
    public void shouldFindTheHardestWorkingPropPartInShowbiz() throws Exception
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (daleks:Species {species: \"Dalek\"})-[:APPEARED_IN]->(episode:Episode)<-[:USED_IN]-(:Props)" +
                "<-[:MEMBER_OF]-(:Prop)-[:COMPOSED_OF]->(part:Part)-[:ORIGINAL_PROP]->(originalprop:Prop)" +
                System.lineSeparator() +
                "RETURN originalprop.prop, part.part, count(episode) ORDER BY count(episode) DESC LIMIT 1";

        // SNIPPET_END

        Result result = db.execute( cql );

        assertHardestWorkingPropParts( result, "Dalek 1", "shoulder", 15l );
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
