package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.neo4j.helpers.collection.IteratorUtil.asIterable;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificTitles.containsOnlyTitles;

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
public class Koan08
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
    public void shouldFindAllTheEpisodesInWhichTheDaleksAppeared() throws Exception
    {
        CypherParser parser = new CypherParser();
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase() );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start daleks=(species,species,\"Dalek\") match (daleks)-[:APPEARED_IN]->(episode) return episode";

        // SNIPPET_END

        Query query = parser.parse( cql );
        ExecutionResult result = engine.execute( query );
        Iterator<Node> episodes = result.javaColumnAs( "episode" );

        assertThat(
                asIterable( episodes ),
                containsOnlyTitles( "The Pandorica Opens", "Victory of the Daleks", "Journey's End",
                        "The Stolen Earth", "Evolution of the Daleks", "Daleks in Manhattan", "Doomsday",
                        "Army of Ghosts", "The Parting of the Ways", "Bad Wolf", "Dalek", "Remembrance of the Daleks",
                        "Revelation of the Daleks", "Resurrection of the Daleks", "The Five Doctors",
                        "Destiny of the Daleks", "Genesis of the Daleks", "Death to the Daleks",
                        "Planet of the Daleks", "Frontier in Space", "Day of the Daleks", "The War Games",
                        "The Evil of the Daleks", "The Power of the Daleks", "The Daleks' Master Plan", "The Chase",
                        "The Space Museum", "The Dalek Invasion of Earth", "The Daleks" ) );

    }

    @Test
    public void shouldFindEpisodesWhereTennantAndRoseBattleTheDaleks() throws Exception
    {
        CypherParser parser = new CypherParser();
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase() );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start daleks = (species, species, \"Dalek\"), rose = (Characters, name, \"Rose Tyler\"), tennant = (actors, actor, \"David Tennant\")";
        cql += "match (tennant)-[:APPEARED_IN]->(ep), (rose)-[:APPEARED_IN]->(ep), (daleks)-[:APPEARED_IN]->(ep)";
        cql += "return ep";

        // SNIPPET_END

        Query query = parser.parse( cql );
        ExecutionResult result = engine.execute( query );
        Iterator<Node> episodes = result.javaColumnAs( "ep" );

        assertThat(
                asIterable( episodes ),
                containsOnlyTitles( "Journey's End", "The Stolen Earth", "Doomsday", "Army of Ghosts",
                        "The Parting of the Ways" ) );
    }

    @Test
    public void shouldFindTheFifthMostRecentPropToAppear() throws Exception
    {
        CypherParser parser = new CypherParser();
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase() );

        String cql = null;
        ExecutionResult result = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        // Not every prop part can be identified with a prop - e.g. the
        // Exhibition skirt
        // As a result, prop.prop will not exist for every prop node
        // So, we must use prop.prop? - this fills the prop.prop column with a
        // <null>
        // value for prop parts with no identifiable prop

        cql = "start dalek  = (species, species, 'Dalek') ";
        cql += "match (dalek)-[:APPEARED_IN]->(episode)<-[:USED_IN]-(props)<-[:MEMBER_OF]-(prop) ";
        cql += "return prop.prop?, episode.episode order by episode.episode desc skip 4 limit 1";

        Query query = parser.parse( cql );
        result = engine.execute( query );

        // SNIPPET_END

        assertEquals( "Supreme Dalek", result.javaColumnAs( "prop.prop" )
                .next() );
    }

    @Test
    public void shouldFindTheHardestWorkingPropPartInShowbiz() throws Exception
    {
        CypherParser parser = new CypherParser();
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase() );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start daleks=(species,species,\"Dalek\") match (daleks)-[:APPEARED_IN]->(episode)<-[:USED_IN]-(props)<-[:MEMBER_OF]-(prop)"
              + "-[:COMPOSED_OF]->(part)-[:ORIGINAL_PROP]->(originalprop) return originalprop.prop, part.type, count(episode.title)"
              + " order by count(episode.title) desc limit 1";

        // SNIPPET_END

        Query query = parser.parse( cql );
        ExecutionResult result = engine.execute( query );

        assertHardestWorkingPropParts( result.javaIterator(), "Dalek 1", "shoulder", 15 );

    }

    private void assertHardestWorkingPropParts( Iterator<Map<String, Object>> results, Object... partsAndCounts )
    {
        for ( int index = 0; index < partsAndCounts.length; index = index + 3 )
        {
            Map<String, Object> row = results.next();
            assertEquals( partsAndCounts[index], row.get( "originalprop.prop" ) );
            assertEquals( partsAndCounts[index + 1], row.get( "part.type" ) );
            assertEquals( partsAndCounts[index + 2], row.get( "count(episode.title)" ) );
        }

        assertFalse( results.hasNext() );
    }
}
