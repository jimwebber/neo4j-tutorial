package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;

import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * In this Koan we use the Cypher graph pattern matching language to investigate
 * the regenerations and the history of the Dalek props, with a focus on longer matches
 * and using aggregates to process the returned data.
 */
public class Koan08c
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
    public void shouldFindTheLatestRegenerationYear()
    {
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start doctor = node:characters(character = 'Doctor')"
                + "match (doctor)<-[:PLAYED]-()-[regeneratedRelationship:REGENERATED_TO]->()"
                + "return max(regeneratedRelationship.year) as latestRegenerationYear";


        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);
        Assert.assertEquals(2010, result.javaColumnAs("latestRegenerationYear").next());
    }

    @Test
    public void shouldFindTheHardestWorkingPropPartInShowbiz() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start daleks= node:species(species = 'Dalek') match (daleks)-[:APPEARED_IN]->(episode)<-[:USED_IN]-(props)<-[:MEMBER_OF]-(prop)"
                + "-[:COMPOSED_OF]->(part)-[:ORIGINAL_PROP]->(originalprop) return originalprop.prop, part.part, count(episode.title)"
                + " order by count (episode.title) desc limit 1";

        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);

        assertHardestWorkingPropParts(result.javaIterator(), "Dalek 1", "shoulder", 15);

    }

    private void assertHardestWorkingPropParts(Iterator<Map<String, Object>> results, Object... partsAndCounts)
    {
        for (int index = 0; index < partsAndCounts.length; index = index + 3)
        {
            Map<String, Object> row = results.next();
            assertEquals(partsAndCounts[index], row.get("originalprop.prop"));
            assertEquals(partsAndCounts[index + 1], row.get("part.part"));
            assertEquals(partsAndCounts[index + 2], row.get("count(episode.title)"));
        }

        assertFalse(results.hasNext());
    }
}
