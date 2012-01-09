package org.neo4j.tutorial.koan13;

import org.neo4j.graphdb.Node;
import org.neo4j.server.plugins.PluginTarget;
import org.neo4j.server.plugins.ServerPlugin;
import org.neo4j.server.plugins.Source;

public class AwesomenessServerPlugin extends ServerPlugin
{
    public AwesomenessServerPlugin() {
        System.out.println("I AM INSTANTIATED");
    }

    @PluginTarget( Node.class )
    public Iterable<Node> foo( @Source Node node )
    {
        return null;
    }
}
