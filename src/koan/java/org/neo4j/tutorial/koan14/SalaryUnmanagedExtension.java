package org.neo4j.tutorial.koan14;

import com.sun.jersey.api.NotFoundException;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;


@Path("/{actor}")
public class SalaryUnmanagedExtension
{
    @GET
    @Path("/salary")
    public String findSalaryForActor(@PathParam("actor") String actor, @Context GraphDatabaseService db)
    {
        ExecutionEngine engine = new ExecutionEngine(db);
        String cql = null;

        cql = "start actor = node:actors(actor = '" + actor + "')"
                + "return actor.salary? as salary";

        ExecutionResult result = engine.execute(cql);
        Object salary = result.javaColumnAs("salary").next();

        if (salary != null)
        {
            return String.valueOf(salary);
        }
        else
        {
            throw new NotFoundException(
                    String.format("The specified actor's salary for [%s] was not found in the database", actor));
        }
    }
}
