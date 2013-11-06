package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class EmbeddedDoctorWhoUniverse
{

    private final GraphDatabaseService database;

    public EmbeddedDoctorWhoUniverse( DoctorWhoUniverseGenerator universe )
    {
        database = new GraphDatabaseFactory().newEmbeddedDatabase( universe.getDatabaseDirectory() );
    }

    public Node theDoctor()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node node = database.index()
                    .forNodes( "characters" )
                    .get( "character", "Doctor" )
                    .getSingle();

            tx.success();
            return node;
        }
    }

    public void stop()
    {
        if ( database != null )
        {
            database.shutdown();
        }
    }

    public GraphDatabaseService getDatabase()
    {
        return database;
    }
}
