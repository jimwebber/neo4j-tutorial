package org.neo4j.tutorial;

import java.io.File;
import java.io.IOException;

import org.neo4j.graphdb.GraphDatabaseService;

public class DoctorWhoUniverseGenerator implements GraphDatabaseGenerator
{
    private final static File cachedCleanDatabaseDirectory = new File( ".doctorwhouniverse" );
    private GraphDatabaseService database;

    public DoctorWhoUniverseGenerator()
    {
        //database = DatabaseHelper.createDatabase( generate() );
    }

    private void createDatabaseCache( String currentDbDir )
    {
        try {
            org.apache.commons.io.FileUtils.copyDirectory( new File( currentDbDir ), cachedCleanDatabaseDirectory );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean cachedDatabaseExists()
    {
//        return false; // for debugging
        return cachedCleanDatabaseDirectory.exists() && cachedCleanDatabaseDirectory.isDirectory();
    }

    private String copyDatabaseFromCachedDirectoryToTempDirectory()
    {
        try {
            String tmpDir = DatabaseHelper.createTempDatabaseDir().getAbsolutePath();
            org.apache.commons.io.FileUtils.copyDirectory( cachedCleanDatabaseDirectory, new File( tmpDir ) );
            return tmpDir;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generate() {
        String currentDbDir;
        if ( cachedDatabaseExists() )
        {
            currentDbDir = copyDatabaseFromCachedDirectoryToTempDirectory();
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

        }
        return currentDbDir;
    }

    private void addUniquenessConstraints()
    {
        database.execute( "CREATE CONSTRAINT ON (a:Actor) ASSERT a.actor IS UNIQUE" );
        database.execute( "CREATE CONSTRAINT ON (c:Character) ASSERT c.character IS UNIQUE" );
        database.execute( "CREATE CONSTRAINT ON (e:Episode) ASSERT e.title IS UNIQUE" );
        database.execute( "CREATE CONSTRAINT ON (p:Planet) ASSERT p.planet IS UNIQUE" );
        database.execute( "CREATE CONSTRAINT ON (p:Planet) ASSERT p.planet IS UNIQUE" );
        database.execute("CREATE CONSTRAINT ON (s:Species) ASSERT s.species IS UNIQUE");
    }

    private void createIndex( String label, String property )
    {
        database.execute(String.format("CREATE INDEX ON :%s(%s)", label, property));
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

    public GraphDatabaseService getDatabase()
    {
        return database;
    }
}
