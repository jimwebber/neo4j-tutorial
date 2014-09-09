package org.neo4j.tutorial;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;
import static org.neo4j.tutorial.SpeciesBuilder.species;

public class Species
{
    private final GraphDatabaseService db;
    private final ExecutionEngine engine;

    public Species( GraphDatabaseService db )
    {
        this.db = db;
        this.engine = new ExecutionEngine( db, DEV_NULL );
    }

    public void insert()
    {
        try ( Transaction tx = db.beginTx() )
        {
            species( "Timelord" )
                    .isEnemyOfSpecies( "Dalek" )
                    .isFrom( "Gallifrey" )
                    .fact( engine );
            species( "Abrobvian" )
                    .isEnemyOf( "Doctor" )
                    .isFrom( "Clom" )
                    .fact( engine );
            species( "Android" ).fact( engine );
            species( "Auton" )
                    .isEnemyOf( "Doctor" )
                    .isEnemyOfSpecies( "Human" )
                    .isFrom( "Polymos" )
                    .fact( engine );
            species( "Axon" )
                    .isEnemyOf( "Doctor" )
                    .isEnemyOfSpecies( "Human" )
                    .fact( engine );
            species( "Devil" )
                    .isEnemyOf( "Doctor" )
                    .isFrom( "Impossible Planet" )
                    .fact( engine );
            species( "Cyberman" )
                    .isEnemyOf( "Doctor" )
                    .isEnemyOfSpecies( "Dalek" )
                    .isFrom( "Mondas" )
                    .fact( engine );
            species( "Dalek" )
                    .isEnemyOf( "Doctor" )
                    .isEnemyOfSpecies( "Cyberman", "Thaal", "Mechonoids", "Human" )
                    .isFrom( "Skaro" )
                    .fact( engine );
            species( "Gargoyle" )
                    .isEnemyOf( "Doctor" )
                    .fact( engine );
            species( "Ice Warrior" )
                    .isEnemyOf( "Doctor" )
                    .isFrom( "Mars" )
                    .fact( engine );
            species( "Human" )
                    .isFrom( "Earth" )
                    .fact( engine );
            species( "Humanoid" )
                    .fact( engine );
            species( "Jagrafess" )
                    .isEnemyOf( "Doctor" )
                    .fact( engine );
            species( "Jagaroth" )
                    .fact( engine );
            species( "Kaled" )
                    .isEnemyOf( "Doctor" )
                    .isFrom( "Skaro" )
                    .fact( engine );
            species( "Kastrian" )
                    .isFrom( "Kastria" )
                    .fact( engine );
            species( "Mechonoids" )
                    .isFrom( "Mechanus" )
                    .fact( engine );
            species( "Ood" )
                    .isFrom( "Ood Sphere" )
                    .fact( engine );
            species( "Osiron" )
                    .isEnemyOf( "Doctor" )
                    .fact( engine );
            species( "Robotic Canine" ).fact( engine );
            species( "Sea Devil" )
                    .isEnemyOf( "Doctor" )
                    .isEnemyOfSpecies( "Human" )
                    .isFrom( "Earth" )
                    .fact( engine );
            species( "Silurian" )
                    .isEnemyOf( "Doctor" )
                    .isEnemyOfSpecies( "Human" )
                    .isFrom( "Earth" )
                    .fact( engine );
            species( "Skarasen" )
                    .isEnemyOf( "Doctor" )
                    .fact( engine );
            species( "Slitheen" )
                    .isEnemyOf( "Doctor" )
                    .isEnemyOfSpecies( "Human" )
                    .isFrom( "Raxacoricofallapatorius" )
                    .fact( engine );
            species( "Sontaran" )
                    .isEnemyOf( "Doctor", "Martha Jones" )
                    .isEnemyOfSpecies( "Human" )
                    .isFrom( "Sontar" )
                    .fact( engine );
            species( "Trion" )
                    .isFrom( "Trion" )
                    .fact( engine );
            species( "Vashta Nerada" )
                    .isEnemyOf( "Doctor", "Donna Noble" )
                    .fact( engine );
            species( "Voord" ).fact( engine );
            tx.success();
        }
    }
}
