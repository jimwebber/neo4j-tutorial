package org.neo4j.tutorial.security_rule;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import com.sun.jersey.api.NotFoundException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;

@Path("/{actor}")
public class SalaryUnmanagedExtension
{
    @GET
    @Path("/salary")
    public String findSalaryForActor( @PathParam("actor") String actor, @Context GraphDatabaseService db )
    {
        String cql = null;

        cql = "MATCH (actor:Actor) " +
                "WHERE has(actor.salary)" +
                "RETURN actor.salary as salary";

        Result result = db.execute( cql );
        Object salary = result.columnAs( "salary" ).next();

        if ( salary != null )
        {
            return String.valueOf( salary );
        }
        else
        {
            throw new NotFoundException(
                    String.format( "The specified actor's salary for [%s] was not found in the database", actor ) );
        }
    }
}
