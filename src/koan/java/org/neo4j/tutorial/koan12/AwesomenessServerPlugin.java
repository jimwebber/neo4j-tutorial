package org.neo4j.tutorial.koan12;

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
        AwesomenessRatingEngine ratingEngine = new AwesomenessRatingEngine( node.getGraphDatabase() );
        return ratingEngine.rateAwesomeness( node );
    }

    // SNIPPET_END
}
