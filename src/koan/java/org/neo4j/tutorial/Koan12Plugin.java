package org.neo4j.tutorial;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;


// YOUR CODE GOES HERE
// SNIPPET_START
@Path("/{character}")
// SNIPPET_END
public class Koan12Plugin
{
    // YOUR CODE GOES HERE
    // SNIPPET_START

    // Alternative version using the core API

//    @Path("/homeplanet")
//    @GET
//    public String findHomePlanetFor(@PathParam("character") String character, @Context GraphDatabaseService db)
//    {
//        Node characterNode = db.index().forNodes("characters").get("character", character).getSingle();
//
//        if(characterNode != null && characterNode.hasRelationship(DoctorWhoRelationships.COMES_FROM, Direction.OUTGOING)) {
//            return (String) characterNode.getSingleRelationship(DoctorWhoRelationships.COMES_FROM, Direction.OUTGOING).getEndNode().getProperty("planet");
//        } else {
//            throw new NotFoundException(String.format("The specified character [%s] was not found in the database", character));
//        }
//    }

    @Path("/homeplanet")
    @GET
    public String findHomePlanetFor(@PathParam("character") String character, @Context GraphDatabaseService db)
    {
        ExecutionEngine engine = new ExecutionEngine(db);
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start char = node:characters(character = '" + character + "')"
                + "match (char)-[:COMES_FROM]->(planet)"
                + "return planet.planet";


        // SNIPPET_END

        ExecutionResult result = engine.execute(cql);
        return (String)result.javaColumnAs("planet.planet").next();
    }

    // SNIPPET_END
}
