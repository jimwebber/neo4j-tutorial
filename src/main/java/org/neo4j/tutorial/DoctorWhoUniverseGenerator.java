package org.neo4j.tutorial;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;

public class DoctorWhoUniverseGenerator
{
    private final String dbDir = DatabaseHelper.createTempDatabaseDir().getAbsolutePath();
    private final GraphDatabaseService database;

    public DoctorWhoUniverseGenerator()
    {
        database = DatabaseHelper.createDatabase( dbDir );
        addDoctorAsNodeOneForToolSupportReasons();
        addActors();
        addEpisodes();
        addCharacters();
        addSpecies();
        addPlanets();
        addDalekProps();

        createActorsIndex();
        createEpisodeIndex();
        createCharactersIndex();
        createSpeciesIndex();
        createPlanetsIndex();

        database.shutdown();
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
        ExecutionEngine engine = new ExecutionEngine( database, DEV_NULL );
        engine.execute( String.format( "CREATE INDEX ON :%s(%s)", label, property ) );
    }

    private void addDoctorAsNodeOneForToolSupportReasons()
    {
        try ( Transaction transaction = database.beginTx() )
        {
            final Node node = database.createNode();
            node.setProperty( "character", "Doctor" );
            database.index().forNodes( "characters" ).add( node, "character", "Doctor" );
            transaction.success();
        }
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

    public final String getDatabaseDirectory()
    {
        return dbDir;
    }
}
