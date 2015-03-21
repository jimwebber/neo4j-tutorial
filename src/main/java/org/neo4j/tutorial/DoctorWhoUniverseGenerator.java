package org.neo4j.tutorial;

import java.io.File;
import java.io.IOException;

import org.neo4j.graphdb.GraphDatabaseService;

public class DoctorWhoUniverseGenerator
{
    private final static File cachedCleanDatabaseDirectory = new File( ".doctorwhouniverse" );
    private final String currentDbDir;
    private GraphDatabaseService database;

    public DoctorWhoUniverseGenerator() throws IOException
    {
        if ( cachedDatabaseExists() )
        {
            currentDbDir = copyDatabaseFromCachedDirectoryToTempDirectory();
            database = DatabaseHelper.createDatabase( currentDbDir );
        }
        else
        {
            currentDbDir = DatabaseHelper.createTempDatabaseDir().getAbsolutePath();
            database = DatabaseHelper.createDatabase( currentDbDir );

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
//        return false; // for debugging
        return cachedCleanDatabaseDirectory.exists() && cachedCleanDatabaseDirectory.isDirectory();
    }

    private String copyDatabaseFromCachedDirectoryToTempDirectory() throws IOException
    {
        String tmpDir = DatabaseHelper.createTempDatabaseDir().getAbsolutePath();
        org.apache.commons.io.FileUtils.copyDirectory( cachedCleanDatabaseDirectory, new File( tmpDir ) );
        return tmpDir;
    }

    private void addUniquenessConstraints()
    {
        database.execute( "CREATE CONSTRAINT ON (a:Actor) ASSERT a.actor IS UNIQUE" );
        database.execute( "CREATE CONSTRAINT ON (c:Character) ASSERT c.character IS UNIQUE" );
        database.execute( "CREATE CONSTRAINT ON (e:Episode) ASSERT e.title IS UNIQUE" );
        database.execute( "CREATE CONSTRAINT ON (p:Planet) ASSERT p.planet IS UNIQUE" );
        database.execute( "CREATE CONSTRAINT ON (p:Planet) ASSERT p.planet IS UNIQUE" );
        database.execute( "CREATE CONSTRAINT ON (s:Species) ASSERT s.species IS UNIQUE" );
    }

    private void createIndex( String label, String property )
    {
        database.execute( String.format( "CREATE INDEX ON :%s(%s)", label, property ) );
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
