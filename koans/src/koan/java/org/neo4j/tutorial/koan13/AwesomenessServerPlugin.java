package org.neo4j.tutorial.koan13;

import org.neo4j.graphdb.Node;
import org.neo4j.server.plugins.PluginTarget;
import org.neo4j.server.plugins.ServerPlugin;
import org.neo4j.server.plugins.Source;
import org.neo4j.tutorial.AwesomenessRatingEngine;

public class AwesomenessServerPlugin extends ServerPlugin
{
    // YOUR CODE GOES HERE
    // SNIPPET_START

    @PluginTarget(Node.class)
    public double awesomeness( @Source Node node )
    {
        AwesomenessRatingEngine ratingEngine = new AwesomenessRatingEngine();
        return ratingEngine.rateAwesomeness( node.getGraphDatabase(), node.getId() );
    }

    // SNIPPET_END
}
