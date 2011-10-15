package org.neo4j.tutorial.server;

import org.neo4j.server.AddressResolver;

public class LocalhostAddressResolver extends AddressResolver
{
    public String getHostname()
    {
        return "localhost";
    }
}
