package org.neo4j.tutorial;

import org.junit.ClassRule;
import org.junit.Test;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;

import static junit.framework.Assert.assertEquals;

/**
 * In this Koan we focus on paths in Cypher.
 */
public class Koan10
{
    @ClassRule
    static public DoctorWhoUniverseResource neo4jResource = new DoctorWhoUniverseResource();

    @Test
    public void shouldFindHowManyRegenerationsBetweenTomBakerAndChristopherEccleston() throws Exception
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH path = (baker:Actor {actor: 'Tom Baker'})-[:REGENERATED_TO*]->(eccleston:Actor {actor: 'Christopher Eccleston'}) " +
                "RETURN LENGTH(path) as regenerations";

        // SNIPPET_END

        Result result = db.execute(cql);

        assertEquals( 6, result.columnAs( "regenerations" ).next() );
    }

    @Test
    public void shouldFindTheLongestContinuousStoryArcWithTheMaster() throws Exception
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "MATCH (master:Character {character: 'Master'})-[:APPEARED_IN]->(first:Episode), storyArcs = (first:Episode)-[:NEXT*]->()" +
                "WHERE ALL(ep in nodes(storyArcs) WHERE master-[:APPEARED_IN]->ep)" +
                "RETURN LENGTH(storyArcs) as noOfPathHops\n" +
                "ORDER BY noOfPathHops DESC LIMIT 1";


        // SNIPPET_END

        Result result = db.execute( cql );

        // noOfPathHops is one less than the number of episodes in a story arc
        final int noOfStories = 5;
        assertEquals( noOfStories - 1, result.columnAs( "noOfPathHops" ).next() );
    }
}
