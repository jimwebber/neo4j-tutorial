package org.neo4j.tutorial.koan14;

import org.neo4j.server.rest.security.SecurityRule;

import javax.servlet.http.HttpServletRequest;

public class UserNameAndPasswordForSalariesSecurityRule implements SecurityRule
{
    @Override
    public boolean isAuthorized(HttpServletRequest httpServletRequest)
    {
        boolean loggedIn = false;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        loggedIn = "Alice".equals(httpServletRequest.getHeader("X-Username")) && "1337".equals(
                httpServletRequest.getHeader(
                        "X-Password"));
        // SNIPPET_END

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
