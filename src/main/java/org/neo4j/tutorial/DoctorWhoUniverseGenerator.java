package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class DoctorWhoUniverseGenerator
{

    private final String dbDir = DatabaseHelper.createTempDatabaseDir()
            .getAbsolutePath();

    public DoctorWhoUniverseGenerator()
    {
        GraphDatabaseService db = DatabaseHelper.createDatabase( dbDir );
        addDoctorAsNodeOneForToolSupportReasons( db );
        addActors( db );
        addEpisodes( db );
        addCharacters( db );
        addSpecies( db );
        addPlanets( db );
        addDalekProps( db );
        db.shutdown();
    }

    private void addDoctorAsNodeOneForToolSupportReasons( GraphDatabaseService db )
    {
        final Transaction transaction = db.beginTx();
        try
        {
            final Node node = db.createNode();
            node.setProperty( "character", "Doctor" );
            db.index().forNodes( "characters" ).add( node, "character", "Doctor" );
            transaction.success();
        }
        finally
        {
            transaction.finish();
        }
    }

    private void addActors( GraphDatabaseService db )
    {
        Actors actors = new Actors( db );
        actors.insert();
    }

    private void addEpisodes( GraphDatabaseService db )
    {
        Episodes episodes = new Episodes( db );
        episodes.insert();
    }

    private void addCharacters( GraphDatabaseService db )
    {
        Characters characters = new Characters( db );
        characters.insert();
    }

    private void addSpecies( GraphDatabaseService db )
    {
        Species species = new Species( db );
        species.insert();
    }

    private void addPlanets( GraphDatabaseService db )
    {
        Planets planets = new Planets( db );
        planets.insert();
    }

    private void addDalekProps( GraphDatabaseService db )
    {
        DalekProps dalekProps = new DalekProps( db );
        dalekProps.insert();
    }

    public final String getDatabaseDirectory()
    {
        return dbDir;
    }
}
