package org.neo4j.tutorial;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.hasItems;
import static org.neo4j.helpers.collection.IteratorUtil.asIterable;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificStrings.containsOnlySpecificStrings;
import static org.neo4j.tutorial.matchers.ContainsWikipediaEntries.containsOnlyWikipediaEntries;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.kernel.impl.util.StringLogger;

/**
 * In this Koan we focus on aggregate functions from the Cypher graph pattern matching language
 * to process some statistics about the Doctor Who universe.
 */
public class Koan08c
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
    public void shouldReturnAnyWikpediaEntriesForCompanions()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "START doctor = node:characters(character = 'Doctor') " +
                "MATCH (doctor)<-[:COMPANION_OF]-(companion) " +
                "WHERE has(companion.wikipedia) " +
                "RETURN companion.wikipedia";


        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );
        Iterator<String> iterator = result.javaColumnAs( "companion.wikipedia" );

        assertThat( iterator, containsOnlyWikipediaEntries( "http://en.wikipedia.org/wiki/Rory_Williams",
                "http://en.wikipedia.org/wiki/Amy_Pond",
                "http://en.wikipedia.org/wiki/River_Song_(Doctor_Who)" ) );

    }

    @Test
    public void shouldCountTheNumberOfActorsKnownToHavePlayedTheDoctor()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "START doctor = node:characters(character = 'Doctor')"
                + "MATCH (doctor)<-[:PLAYED]-(actor) "
                + "RETURN count(actor) AS numberOfActorsWhoPlayedTheDoctor";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );
        Long actorsCount = (Long) result.javaColumnAs( "numberOfActorsWhoPlayedTheDoctor" ).next();

        assertEquals( 12l, actorsCount.longValue() );
    }

    @Test
    public void shouldFindEarliestAndLatestRegenerationYears()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "START doctor = node:characters(character = 'Doctor') " +
                "MATCH (doctor)<-[:PLAYED]-()-[regen:REGENERATED_TO]->() " +
                "RETURN min(regen.year) AS earliest, max(regen.year) AS latest";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );


        Map<String, Object> map = result.javaIterator().next();
        assertEquals( 2010, map.get( "latest" ) );
        assertEquals( 1966, map.get( "earliest" ) );
    }

    @Test
    public void shouldFindTheEarliestEpisodeWhereFreemaAgyemanAndDavidTennantWorkedTogether() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "START david=node:actors(actor = 'David Tennant'), freema=node:actors(actor = 'Freema Agyeman'), doctor=node:characters(character = 'Doctor'), martha=node:characters(character = 'Martha Jones') "
                + "MATCH (freema)-[:PLAYED]->(martha)-[:APPEARED_IN]->(episode)<-[:APPEARED_IN]-(david)-[:PLAYED]->(doctor)"
                + "RETURN min(episode.episode) AS earliest";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        assertEquals( "179", result.javaColumnAs( "earliest" ).next() );
    }

    @Test
    public void shouldFindAverageSalaryOfActorsWhoPlayedTheDoctor()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "START doctor = node:characters(character = 'Doctor')"
                + "MATCH (doctor)<-[:PLAYED]-(actor)"
                + "RETURN avg(actor.salary?) AS cash";


        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        assertEquals( 600000.0, result.javaColumnAs( "cash" ).next() );
    }

    @Test
    public void shouldListTheEnemySpeciesAndCharactersForEachEpisodeWithPeterDavisonOrderedByIncreasingEpisodeNumber()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "START davison=node:actors(actor='Peter Davison') "
                + "MATCH (davison)-[:APPEARED_IN]->(episode)<-[:APPEARED_IN]-(enemy)-[:ENEMY_OF]->()<-[:PLAYED]-(davison)"
                + "RETURN episode.episode, episode.title, collect(enemy.species?) AS species, collect(enemy.character?) AS characters "
                + "ORDER BY episode.episode";


        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        final List<String> columnNames = result.javaColumns();
        assertThat( columnNames,
                containsOnlySpecificStrings( "episode.episode", "episode.title", "species", "characters" ) );

        assertDavisonEpisodesRetrievedCorrectly( result.javaIterator() );
    }

    @Test
    public void shouldFindTheEnemySpeciesThatRoseTylerFought()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), StringLogger.DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "START rose = node:characters(character = 'Rose Tyler'), doctor = node:characters(character = 'Doctor') "
                + "MATCH rose-[:APPEARED_IN]->episode, "
                + "(doctor)-[:ENEMY_OF]->(enemy)-[:APPEARED_IN]->(episode) "
                + "WHERE has(enemy.species)  "
                + "RETURN DISTINCT enemy.species AS enemySpecies";


        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );
        Iterator<String> enemySpecies = result.javaColumnAs( "enemySpecies" );

        assertThat( asIterable( enemySpecies ),
                containsOnlySpecificStrings( "Krillitane", "Sycorax", "Cyberman", "Dalek", "Auton", "Slitheen",
                        "Clockwork Android" ) );

    }

    @SuppressWarnings("unchecked")
    private void assertDavisonEpisodesRetrievedCorrectly( Iterator<Map<String, Object>> iterator )
    {
        Map<String, Object> next = iterator.next();
        assertEquals( Arrays.asList( "Master" ), next.get( "characters" ) );
        assertEquals( "116", next.get( "episode.episode" ) );
        assertEquals( "Castrovalva", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "Monarch" ), next.get( "characters" ) );
        assertEquals( "117", next.get( "episode.episode" ) );
        assertEquals( "Four to Doomsday", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "Mara" ), next.get( "characters" ) );
        assertEquals( "118", next.get( "episode.episode" ) );
        assertEquals( "Kinda", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "Terileptils" ), next.get( "characters" ) );
        assertEquals( "119", next.get( "episode.episode" ) );
        assertEquals( "The Visitation", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "George Cranleigh" ), next.get( "characters" ) );
        assertEquals( "120", next.get( "episode.episode" ) );
        assertEquals( "Black Orchid", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "Cyberman" ), next.get( "species" ) );
        assertEquals( "121", next.get( "episode.episode" ) );
        assertEquals( "Earthshock", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "Master" ), next.get( "characters" ) );
        assertEquals( "122", next.get( "episode.episode" ) );
        assertEquals( "Time-Flight", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "Omega" ), next.get( "characters" ) );
        assertEquals( "123", next.get( "episode.episode" ) );
        assertEquals( "Arc of Infinity", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "Mara" ), next.get( "characters" ) );
        assertEquals( "124", next.get( "episode.episode" ) );
        assertEquals( "Snakedance", next.get( "episode.title" ) );

        next = iterator.next();
        final List chars = (List) next.get( "characters" );
        assertTrue( chars.contains( "Mawdryn" ) );
        assertTrue( chars.contains( "Black Guardian" ) );
        assertEquals( "125", next.get( "episode.episode" ) );
        assertEquals( "Mawdryn Undead", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "Vanir" ), next.get( "characters" ) );
        assertEquals( "126", next.get( "episode.episode" ) );
        assertEquals( "Terminus", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "Black Guardian" ), next.get( "characters" ) );
        assertEquals( "127", next.get( "episode.episode" ) );
        assertEquals( "Enlightenment", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "Master" ), next.get( "characters" ) );
        assertEquals( "128", next.get( "episode.episode" ) );
        assertEquals( "The King's Demons", next.get( "episode.title" ) );

        next = iterator.next();
        assertThat( (Iterable<String>) next.get( "species" ), hasItems( "Dalek" ) );
        assertThat( (Iterable<String>) next.get( "characters" ), hasItems( "Master" ) );
        assertEquals( "129", next.get( "episode.episode" ) );
        assertEquals( "The Five Doctors", next.get( "episode.title" ) );

        next = iterator.next();
        assertThat( (Iterable<String>) next.get( "species" ), hasItems( "Sea Devil", "Silurian" ) );
        assertEquals( "130", next.get( "episode.episode" ) );
        assertEquals( "Warriors of the Deep", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "Malus" ), next.get( "characters" ) );
        assertEquals( "131", next.get( "episode.episode" ) );
        assertEquals( "The Awakening", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "Tractator" ), next.get( "species" ) );
        assertEquals( "132", next.get( "episode.episode" ) );
        assertEquals( "Frontios", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "Dalek" ), next.get( "species" ) );
        assertEquals( "133", next.get( "episode.episode" ) );
        assertEquals( "Resurrection of the Daleks", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "Master" ), next.get( "characters" ) );
        assertEquals( "134", next.get( "episode.episode" ) );
        assertEquals( "Planet of Fire", next.get( "episode.title" ) );

        next = iterator.next();
        assertEquals( Arrays.asList( "Master" ), next.get( "characters" ) );
        assertEquals( "135", next.get( "episode.episode" ) );
        assertEquals( "The Caves of Androzani", next.get( "episode.title" ) );
    }
}
