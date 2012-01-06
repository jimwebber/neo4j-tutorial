package org.neo4j.tutorial.koan13;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.rest.security.SecurityRule;

import javax.servlet.http.HttpServletRequest;

public class UserNameAndPasswordForSalariesSecurityRule implements SecurityRule
{
    @Override
    public boolean isAuthorized(HttpServletRequest httpServletRequest, GraphDatabaseService graphDatabaseService)
    {
        return "Alice".equals(httpServletRequest.getHeader("X-Username")) && "1337".equals(httpServletRequest.getHeader("X-Password"));
    }

    @Override
    public String forUriPath()
    {
        return "/koan13";
    }

    @Override
    public String wwwAuthenticateHeader()
    {
        return "foobar";
    }
}
