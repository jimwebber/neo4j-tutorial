package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;

public class DoctorWhoUniverseGenerator
{

    private final String dbDir = DatabaseHelper.createTempDatabaseDir()
                                               .getAbsolutePath();

    public DoctorWhoUniverseGenerator()
    {
        GraphDatabaseService db = DatabaseHelper.createDatabase(dbDir);
        addCharacters(db);
        addActors(db);
        addSpecies(db);
        addPlanets(db);
        addEpisodes(db);
        addDalekProps(db);
        db.shutdown();
    }
    private void addActors(GraphDatabaseService db)
    {
        Actors actors = new Actors(db);
        actors.insert();
    }
    
    private void addEpisodes(GraphDatabaseService db)
    {
        Episodes episodes = new Episodes(db);
        episodes.insert();
    }

    private void addCharacters(GraphDatabaseService db)
    {
        Characters characters = new Characters(db);
        characters.insert();
    }

    private void addSpecies(GraphDatabaseService db)
    {
        Species species = new Species(db);
        species.insert();
    }

    private void addPlanets(GraphDatabaseService db)
    {
        Planets planets = new Planets(db);
        planets.insert();
    }

    private void addDalekProps(GraphDatabaseService db)
    {
        DalekProps dalekProps = new DalekProps(db);
        dalekProps.insert();
    }

    public final String getDatabaseDirectory()
    {
        return dbDir;
    }
}
