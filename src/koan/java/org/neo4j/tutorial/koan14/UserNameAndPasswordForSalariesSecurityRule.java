package org.neo4j.tutorial.koan14;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.rest.security.SecurityRule;

import javax.servlet.http.HttpServletRequest;

public class UserNameAndPasswordForSalariesSecurityRule implements SecurityRule
{
    @Override
    public boolean isAuthorized(HttpServletRequest httpServletRequest, GraphDatabaseService graphDatabaseService)
    {
        boolean loggedIn = false;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        loggedIn = "Alice".equals(httpServletRequest.getHeader("X-Username")) && "1337".equals(
                httpServletRequest.getHeader(
                        "X-Password"));
        // SNIPPET_END

        ExecutionEngine engine = new ExecutionEngine(graphDatabaseService);
        String cql = null;

        cql = "start char = node:characters(character = 'Rose Tyler')"
                + "match (char)-[:COMES_FROM]->(planet)"
                + "return planet.planet";

        ExecutionResult result = engine.execute(cql);
        String planet = (String) result.javaColumnAs("planet.planet").next();     
        
        System.out.println(planet);

        return loggedIn;
    }

    @Override
    public String forUriPath()
    {
        String uriPath = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        uriPath = "/koan14";

        // SNIPPET_END

        return uriPath;
    }

    @Override
    public String wwwAuthenticateHeader()
    {
        String message = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        message = "Unathorised without username and password";

        // SNIPPET_END

        return message;
    }
}
