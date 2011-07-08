package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.neo4j.helpers.collection.IteratorUtil.asIterable;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificTitles.containsOnlyTitles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.cypher.commands.Query;
import org.neo4j.cypher.parser.CypherParser;
import org.neo4j.graphdb.Node;

/**
 * In this Koan we use the Cypher graph pattern matching language to investigate
 * the history of the Dalek props.
 */
public class Koan08 {
    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception {
        universe = new EmbeddedDoctorWhoUniverse();
    }

    @AfterClass
    public static void closeTheDatabase() {
        universe.stop();
    }

    @Test
    public void shouldFindAllTheEpisodesInWhichDalekPropsWereUsed() throws Exception {
        CypherParser parser = new CypherParser();
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "START daleks=(Species,species,\"Dalek\") MATCH (daleks)-[:APPEARED_IN]->(episode) RETURN episode";

        // SNIPPET_END

        Query query = parser.parse(cql);
        ExecutionResult result = engine.execute(query);
        Iterator<Node> episodes = result.javaColumnAs("episode");

        assertThat(asIterable(episodes),
                containsOnlyTitles("The Pandorica Opens", "Victory of the Daleks", "Journey's End", "The Stolen Earth", "Evolution of the Daleks",
                        "Daleks in Manhattan", "Doomsday", "Army of Ghosts", "The Parting of the Ways", "Bad Wolf", "Dalek", "Remembrance of the Daleks",
                        "Revelation of the Daleks", "Resurrection of the Daleks", "Destiny of the Daleks", "Genesis of the Daleks", "Death to the Daleks",
                        "Planet of the Daleks", "Day of the Daleks", "The Evil of the Daleks", "The Power of the Daleks", "The Daleks' Master Plan", "The Chase",
                        "The Dalek Invasion of Earth", "The Daleks"));

    }

    @Test
    public void shouldFindTheFifthMostRecentPropToAppear() throws Exception {
        CypherParser parser = new CypherParser();
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());

        String cql = null;
        ExecutionResult result;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "START daleks=(Species,species,\"Dalek\")" + " MATCH (daleks)-[:APPEARED_IN]->(episode)" + "<-[:USED_IN]-(props)" + "<-[:MEMBER_OF]-(prop)"
                + " RETURN prop.prop" + " SKIP 4 LIMIT 1";

        Query query = parser.parse(cql);
        result = engine.execute(query);

        // SNIPPET_END

        assertEquals("Supreme Dalek", result.javaColumnAs("prop.prop").next());
    }

    @Test
    public void shouldFindTheTop3HardestWorkingPropPartsInShowbiz() throws Exception {
        CypherParser parser = new CypherParser();
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "START daleks=(Species,species,\"Dalek\")" + " MATCH (daleks)-[:APPEARED_IN]->(episode)" + "<-[:USED_IN]-(props)" + "<-[:MEMBER_OF]-(prop)"
                + "-[:COMPOSED_OF]->(part)" + "-[:ORIGINAL_PROP]->(originalprop)" + " RETURN originalprop.prop, part.part, COUNT(*)"
                + " ORDER BY COUNT(*) DESC" + " LIMIT 3";

        // SNIPPET_END

        Query query = parser.parse(cql);
        ExecutionResult result = engine.execute(query);
        Iterator<Map<String, Object>> stats = result.javaIterator();

        Iterator<PropInfo> expectedStats = createExpectedStats(new PropInfo("Dalek 1", "shoulder", 12), new PropInfo("Dalek 5", "skirt", 12), new PropInfo(
                "Dalek 6", "shoulder", 12));

        while (stats.hasNext()) {
            Map<String, Object> stat = stats.next();
            PropInfo expectedStat = expectedStats.next();
            assertEquals(expectedStat.getOriginalProp(), stat.get("originalprop.prop"));
            assertEquals(expectedStat.getPart(), stat.get("part.part"));
            assertEquals(expectedStat.getCount(), stat.get("count(*)"));

        }
        assertFalse(stats.hasNext());

    }

    private Iterator<PropInfo> createExpectedStats(PropInfo... propStats) {
        Collection<PropInfo> expectedPropPartStats = new ArrayList<PropInfo>();
        for (PropInfo propStat : propStats) {
            expectedPropPartStats.add(propStat);
        }
        return expectedPropPartStats.iterator();
    }

    private class PropInfo {
        private final String originalProp;
        private final String part;
        private final int count;

        public PropInfo(String originalProp, String part, int count) {
            super();
            this.originalProp = originalProp;
            this.part = part;
            this.count = count;
        }

        public String getOriginalProp() {
            return originalProp;
        }

        public String getPart() {
            return part;
        }

        public int getCount() {
            return count;
        }
    }
}
