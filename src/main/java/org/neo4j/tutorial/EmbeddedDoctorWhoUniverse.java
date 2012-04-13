package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class EmbeddedDoctorWhoUniverse
{

    private final GraphDatabaseService db;

    public EmbeddedDoctorWhoUniverse( DoctorWhoUniverseGenerator universe )
    {
        db = new GraphDatabaseFactory().newEmbeddedDatabase( universe.getDatabaseDirectory() );
    }

    public Node theDoctor()
    {
        return db.index()
            .forNodes( "characters" )
            .get( "character", "Doctor" )
            .getSingle();
    }

    public void stop()
    {
        if ( db != null )
        {
            db.shutdown();
        }
    }

    public GraphDatabaseService getDatabase()
    {
        return db;
    }
}
