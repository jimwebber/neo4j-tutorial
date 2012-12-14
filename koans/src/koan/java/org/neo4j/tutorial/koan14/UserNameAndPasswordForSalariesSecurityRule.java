package org.neo4j.tutorial.koan14;

import javax.servlet.http.HttpServletRequest;

import org.neo4j.server.rest.security.SecurityRule;

public class UserNameAndPasswordForSalariesSecurityRule implements SecurityRule
{
    public boolean isAuthorized( HttpServletRequest httpServletRequest )
    {
        boolean loggedIn = false;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        loggedIn = "Alice".equals( httpServletRequest.getHeader( "X-Username" ) ) && "1337".equals(
                httpServletRequest.getHeader(
                        "X-Password" ) );
        // SNIPPET_END

        return loggedIn;
    }

    public String forUriPath()
    {
        String uriPath = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        uriPath = "/koan14";

        // SNIPPET_END

        return uriPath;
    }

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
