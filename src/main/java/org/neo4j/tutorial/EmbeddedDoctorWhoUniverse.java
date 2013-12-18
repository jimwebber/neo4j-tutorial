package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;

import static org.neo4j.tutorial.DoctorWhoLabels.CHARACTER;

public class EmbeddedDoctorWhoUniverse
{
    private GraphDatabaseService database;

    public EmbeddedDoctorWhoUniverse( GraphDatabaseService graphDatabaseService )
    {
        database = graphDatabaseService;
    }

    public Node theDoctor()
    {
        try ( Transaction tx = database.beginTx() )
        {
            final ResourceIterable<Node> nodes = database.findNodesByLabelAndProperty(
                    CHARACTER, "character", "Doctor" );

            tx.success();

            return nodes.iterator().next();
        }
    }

    public void stop()
    {
        if ( database != null )
        {
            database.shutdown();
        }

        database = null;
    }

    public GraphDatabaseService getDatabase()
    {
        return database;
    }
}
