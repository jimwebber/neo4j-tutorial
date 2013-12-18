package org.neo4j.tutorial;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;

public class DoctorWhoUniverseGenerator
{
    private final String dbDir = DatabaseHelper.createTempDatabaseDir().getAbsolutePath();
    private final GraphDatabaseService database;
    private ExecutionEngine engine;

    public DoctorWhoUniverseGenerator()
    {
        database = DatabaseHelper.createDatabase( dbDir );
        engine = new ExecutionEngine( database, DEV_NULL );


//        createActorsIndex();
//        createEpisodeIndex();
//        createCharactersIndex();
//        createSpeciesIndex();
//        createPlanetsIndex();

        addUniquenessConstraints();

        addCharacters();
        addActors();
        addEpisodes();
        addSpecies();
        addPlanets();
        addDalekProps();
    }

    private void addUniquenessConstraints()
    {
        engine.execute( "CREATE CONSTRAINT ON (a:Actor) ASSERT a.actor IS UNIQUE" );
        engine.execute( "CREATE CONSTRAINT ON (c:Character) ASSERT c.character IS UNIQUE" );
        engine.execute( "CREATE CONSTRAINT ON (e:Episode) ASSERT e.title IS UNIQUE" );
        engine.execute( "CREATE CONSTRAINT ON (p:Planet) ASSERT p.planet IS UNIQUE" );
        engine.execute( "CREATE CONSTRAINT ON (p:Planet) ASSERT p.planet IS UNIQUE" );
        engine.execute( "CREATE CONSTRAINT ON (s:Species) ASSERT s.species IS UNIQUE" );
    }

    private void createPlanetsIndex()
    {
        createIndex( "Planet", "planet" );
    }

    private void createSpeciesIndex()
    {
        createIndex( "Species", "species" );
    }

    private void createCharactersIndex()
    {
        createIndex( "Character", "character" );
    }

    private void createEpisodeIndex()
    {
        createIndex( "Episode", "episode" );
    }

    private void createActorsIndex()
    {
        createIndex( "Actor", "actor" );
    }

    private void createIndex( String label, String property )
    {
        engine.execute( String.format( "CREATE INDEX ON :%s(%s)", label, property ) );
    }

    private void addActors()
    {
        Actors actors = new Actors( database);
        actors.insert();
    }

    private void addEpisodes()
    {
        Episodes episodes = new Episodes( database );
        episodes.insert();
    }

    private void addCharacters()
    {
        Characters characters = new Characters( database );
        characters.insert();
    }

    private void addSpecies()
    {
        Species species = new Species( database );
        species.insert();
    }

    private void addPlanets()
    {
        Planets planets = new Planets( database );
        planets.insert();
    }

    private void addDalekProps()
    {
        DalekProps dalekProps = new DalekProps( database );
        dalekProps.insert();
    }

    public String getCleanlyShutdownDatabaseDirectory()
    {
        database.shutdown();
        return dbDir;
    }

    public GraphDatabaseService getDatabase()
    {
        return database;
    }
}
