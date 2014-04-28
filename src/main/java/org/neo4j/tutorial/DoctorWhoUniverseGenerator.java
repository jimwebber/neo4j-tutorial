package org.neo4j.tutorial;

import java.io.File;
import java.io.IOException;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;

public class DoctorWhoUniverseGenerator
{
    private final static File cachedCleanDatabaseDirectory = new File( ".doctorwhouniverse" );
    private final String currentDbDir;
    private GraphDatabaseService database;
    private ExecutionEngine engine;

    public DoctorWhoUniverseGenerator() throws IOException
    {
        if ( cachedDatabaseExists() )
        {
            currentDbDir = copyDatabaseFromCachedDirectoryToTempDirectory();
            database = DatabaseHelper.createDatabase( currentDbDir );
            return;
        }
        else
        {
            currentDbDir = DatabaseHelper.createTempDatabaseDir().getAbsolutePath();
            database = DatabaseHelper.createDatabase( currentDbDir );
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

            database.shutdown();

            createDatabaseCache( currentDbDir );

            database = DatabaseHelper.createDatabase( currentDbDir );
        }
    }

    private void createDatabaseCache( String currentDbDir ) throws IOException
    {
        org.apache.commons.io.FileUtils.copyDirectory( new File( currentDbDir ), cachedCleanDatabaseDirectory );
    }

    private boolean cachedDatabaseExists()
    {
        return false; // for debugging
//        return cachedCleanDatabaseDirectory.exists() && cachedCleanDatabaseDirectory.isDirectory();
    }

    private String copyDatabaseFromCachedDirectoryToTempDirectory() throws IOException
    {
        String tmpDir = DatabaseHelper.createTempDatabaseDir().getAbsolutePath();
        org.apache.commons.io.FileUtils.copyDirectory( cachedCleanDatabaseDirectory, new File( tmpDir ) );
        return tmpDir;
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
        Actors actors = new Actors( database );
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
        return currentDbDir;
    }

    public GraphDatabaseService getDatabase()
    {
        return database;
    }
}
