package org.neo4j.tutorial.koan12;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import com.sun.jersey.api.NotFoundException;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.impl.util.StringLogger;


@Path("/{character}")
public class HomePlanetUnmanagedExtension
{
    // YOUR CODE GOES HERE
    // SNIPPET_START

    @GET
    @Path("/homeplanet")
    public String findHomePlanetFor( @PathParam("character") String character, @Context GraphDatabaseService db )
    {
        ExecutionEngine engine = new ExecutionEngine( db, StringLogger.DEV_NULL );
        String cql = null;

        cql = "start char = node:characters(character = '" + character + "')"
                + "match (char)-[:COMES_FROM]->(planet)"
                + "return planet.planet";

        ExecutionResult result = engine.execute( cql );
        String planet = (String) result.javaColumnAs( "planet.planet" ).next();

        if ( planet != null )
        {
            return planet;
        }
        else
        {
            throw new NotFoundException(
                    String.format( "The specified character [%s] was not found in the database", character ) );
        }
    }

    // SNIPPET_END
}
