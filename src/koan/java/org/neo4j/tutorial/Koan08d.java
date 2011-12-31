package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;

/**
 * In this Koan we focus on calling pre-canned graph algorithms from Cypher.
 */
@Ignore
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
    public void shouldFindTheEarliestEpisodeWhereFreemaAgyemanAndDavidTennantWorkedTogether() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start david=node:actors(actor = 'David Tennant'), freema=node:actors(actor = 'Freema Agyeman') "
                + "match p = allShortestPaths( (david)-[*..4]-(freema) )"
                + "return extract(n in nodes(p) : min(n.episode?))";

        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);

        System.out.println(result.dumpToString());

        //TODO: figure out if this is even possible in Cypher!
    }

    @Test
    public void shouldFindHowManyRegenerationsBetweenTomBakerAndChristopherEccleston() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine(universe.getDatabase());
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start eccleston=node:actors(actor = 'Christopher Eccleston'), baker = node:actors(actor = 'Tom Baker') "
                + "match p = shortestPath(baker-[:REGENERATED_TO*]->eccleston) "
                + "return nodes(p)";


        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);
        System.out.println(result.dumpToString());

        //TODO: figure out if this is even possible in Cypher!
    }
}
