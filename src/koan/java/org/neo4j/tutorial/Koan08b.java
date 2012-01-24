package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.neo4j.helpers.collection.IteratorUtil.asIterable;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificStrings.containsOnlySpecificStrings;
import static org.neo4j.tutorial.matchers.ContainsWikipediaEntries.containsWikipediaEntries;

/**
 * In this Koan we focus on aggregate functions from the Cypher graph pattern matching language
 * to process some statistics about the Doctor Who universe.
 */
public class Koan08b
{
    private static EmbeddedDoctorWhoUniverse universe;
    private static DatabaseHelper databaseHelper;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse(new DoctorWhoUniverseGenerator());
        databaseHelper = new DatabaseHelper(universe.getDatabase());
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Test
    public void shouldReturnAnyWikpediaEntriesForCompanions()
    {

        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start doctor = node:characters(character = 'Doctor') " +
                "match (doctor)<-[:COMPANION_OF]-(companion)" +
                "return companion.wikipedia?";


        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);
        Iterator<String> iterator = result.javaColumnAs("companion.wikipedia");

        assertThat(asIterable(iterator), containsWikipediaEntries("http://en.wikipedia.org/wiki/Rory_Williams",
                                                                  "http://en.wikipedia.org/wiki/Amy_Pond",
                                                                  "http://en.wikipedia.org/wiki/River_Song_(Doctor_Who)"));

    }

    @Test
    public void shouldCountTheNumberOfActorsKnownToHavePlayedTheDoctor()
    {
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start doctor = node:characters(character = 'Doctor')"
                + "match (doctor)<-[:PLAYED]-(actor) "
                + "return count(actor) as numberOfActorsWhoPlayedTheDoctor";

        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);
        Integer actorsCount = (Integer) result.javaColumnAs("numberOfActorsWhoPlayedTheDoctor").next();

        assertEquals(12, actorsCount.intValue());
    }

    @Test
    public void shouldFindEarliestAndLatestRegenerationYears()
    {
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start doctor = node:characters(character = 'Doctor') " +
                "match (doctor)<-[:PLAYED]-()-[regen:REGENERATED_TO]->() " +
                "return min(regen.year) as earliest, max(regen.year) as latest";

        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);


        Map<String, Object> map = result.javaIterator().next();
        assertEquals(2010, map.get("latest"));
        assertEquals(1966, map.get("earliest"));
    }

    @Test
    public void shouldFindTheEarliestEpisodeWhereFreemaAgyemanAndDavidTennantWorkedTogether() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start david=node:actors(actor = 'David Tennant'), freema=node:actors(actor = 'Freema Agyeman'), doctor=node:characters(character = 'Doctor'), martha=node:characters(character = 'Martha Jones') "
                + "match (freema)-[:PLAYED]->(martha)-[:APPEARED_IN]->(episode)<-[:APPEARED_IN]-(david)-[:PLAYED]->(doctor)"
                + "return min(episode.episode) as earliest";

        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);

        assertEquals("179", result.javaColumnAs("earliest").next());
    }

    @Test
    public void shouldFindAverageSalaryOfActorsWhoPlayedTheDoctor()
    {
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start doctor = node:characters(character = 'Doctor')"
                + "match (doctor)<-[:PLAYED]-(actor)"
                + "return avg(actor.salary?) as cash";


        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);

        assertEquals(600000.0, result.javaColumnAs("cash").next());
    }

    @Test
    public void shouldListTheEnemySpeciesAndCharactersForEachEpisodeWithPeterDavisonOrderedByIncreasingEpisodeNumber()
    {
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start doctor = node:characters(character = 'Doctor')"
                + "match (doctor)<-[:PLAYED]-(actor)-[:APPEARED_IN]->(episode)<-[:APPEARED_IN]-(enemy),"
                + "(enemy)-[:ENEMY_OF]->(doctor)"
                + "where actor.actor = 'Peter Davison'"
                + "return episode.episode, episode.title, collect(enemy.species?) as species, collect(enemy.character?) as characters "
                + "order by episode.episode";


        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);

        assertThat(result.dumpToString(), containsString(
                "+------------------------------------------------------------------------------------------------------------+\n" +
                        "| episode.episode | episode.title                | species                   | characters                    |\n" +
                        "+------------------------------------------------------------------------------------------------------------+\n" +
                        "| \"116\"           | \"Castrovalva\"                | List(null)                | List(Master)                  |\n" +
                        "| \"118\"           | \"Kinda\"                      | List(null)                | List(Mara)                    |\n" +
                        "| \"119\"           | \"The Visitation\"             | List(null)                | List(Terileptils)             |\n" +
                        "| \"121\"           | \"Earthshock\"                 | List(Cyberman)            | List(null)                    |\n" +
                        "| \"122\"           | \"Time-Flight\"                | List(null)                | List(Master)                  |\n" +
                        "| \"123\"           | \"Arc of Infinity\"            | List(null)                | List(Omega)                   |\n" +
                        "| \"124\"           | \"Snakedance\"                 | List(null)                | List(Mara)                    |\n" +
                        "| \"125\"           | \"Mawdryn Undead\"             | List(null, null)          | List(Mawdryn, Black Guardian) |\n" +
                        "| \"126\"           | \"Terminus\"                   | List(null)                | List(Vanir)                   |\n" +
                        "| \"127\"           | \"Enlightenment\"              | List(null)                | List(Black Guardian)          |\n" +
                        "| \"128\"           | \"The King's Demons\"          | List(null)                | List(Master)                  |\n" +
                        "| \"129\"           | \"The Five Doctors\"           | List(Dalek, null)         | List(null, Master)            |\n" +
                        "| \"130\"           | \"Warriors of the Deep\"       | List(Silurian, Sea Devil) | List(null, null)              |\n" +
                        "| \"131\"           | \"The Awakening\"              | List(null)                | List(Malus)                   |\n" +
                        "| \"132\"           | \"Frontios\"                   | List(Tractator)           | List(null)                    |\n" +
                        "| \"133\"           | \"Resurrection of the Daleks\" | List(Dalek)               | List(null)                    |\n" +
                        "| \"134\"           | \"Planet of Fire\"             | List(null)                | List(Master)                  |\n" +
                        "| \"135\"           | \"The Caves of Androzani\"     | List(null)                | List(Master)                  |\n" +
                        "+------------------------------------------------------------------------------------------------------------+"
        ));

        final List<String> columnNames = result.javaColumns();
        assertThat(columnNames,
                   containsOnlySpecificStrings("episode.episode", "episode.title", "species", "characters"));
    }

    @Test
    public void shouldFindTheEnemySpeciesThatRoseTylerFought()
    {
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start rose = node:characters(character = 'Rose Tyler'), doctor = node:characters(character = 'Doctor') "
                + "match (rose)-[:APPEARED_IN]->(episode), "
                + "(doctor)-[:ENEMY_OF]->(enemy)-[:APPEARED_IN]->(episode) "
                + "where enemy.species "
                + "return distinct enemy.species as enemySpecies";


        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);
        Iterator<String> enemySpecies = result.javaColumnAs("enemySpecies");

        assertThat(asIterable(enemySpecies),
                   containsOnlySpecificStrings("Krillitane", "Sycorax", "Cyberman", "Dalek", "Auton", "Slitheen",
                                               "Clockwork Android"));

    }
}
