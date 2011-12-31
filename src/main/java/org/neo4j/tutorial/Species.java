package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import static org.neo4j.tutorial.SpeciesBuilder.species;

public class Species
{

    private final GraphDatabaseService db;

    public Species(GraphDatabaseService db)
    {
        this.db = db;
    }

    public void insert()
    {
        Transaction tx = db.beginTx();
        try
        {
            species("Timelord").isEnemyOfSpecies("Dalek")
                    .isFrom("Gallifrey")
                    .fact(db);
            species("Abrobvian").isEnemyOf("Doctor")
                    .isFrom("Clom")
                    .fact(db);
            species("Android").fact(db);
            species("Auton").isEnemyOf("Doctor")
                    .isEnemyOfSpecies("Human")
                    .isFrom("Polymos")
                    .fact(db);
            species("Axon").isEnemyOf("Doctor")
                    .isEnemyOfSpecies("Human")
                    .fact(db);
            species("Devil").isEnemyOf("Doctor", "Rose Tyler")
                    .isFrom("Impossible Planet")
                    .fact(db);
            species("Cyberman").isEnemyOf("Doctor")
                    .isEnemyOfSpecies("Dalek")
                    .isFrom("Mondas")
                    .fact(db);
            species("Dalek").isEnemyOf("Doctor")
                    .isEnemyOfSpecies("Cyberman", "Thaal", "Mechonoids", "Human")
                    .isFrom("Skaro")
                    .fact(db);
            species("Gargoyle").isEnemyOf("Doctor")
                    .fact(db);
            species("Ice Warrior").isEnemyOf("Doctor")
                    .isFrom("Mars")
                    .fact(db);
            species("Human").isFrom("Earth")
                    .fact(db);
            species("Humanoid").fact(db);
            species("Jagrafess").isEnemyOf("Doctor")
                    .fact(db);
            species("Jagaroth").fact(db);
            species("Kaled").isEnemyOf("Doctor")
                    .isFrom("Skaro")
                    .fact(db);
            species("Kastrian").isFrom("Kastria")
                    .fact(db);
            species("Mechonoids").isFrom("Mechanus")
                    .fact(db);
            species("Ood").isFrom("Ood Sphere")
                    .fact(db);
            species("Osiron").isEnemyOf("Doctor")
                    .fact(db);
            species("Robotic Canine").fact(db);
            species("Sea Devil").isEnemyOf("Doctor")
                    .isEnemyOfSpecies("Human")
                    .isFrom("Earth")
                    .fact(db);
            species("Silurian").isEnemyOf("Doctor")
                    .isEnemyOfSpecies("Human")
                    .isFrom("Earth")
                    .fact(db);
            species("Skarasen").isEnemyOf("Doctor")
                    .fact(db);
            species("Slitheen").isEnemyOf("Doctor")
                    .isEnemyOfSpecies("Human")
                    .isFrom("Raxacoricofallapatorius")
                    .fact(db);
            species("Sontaran").isEnemyOf("Doctor", "Martha Jones")
                    .isEnemyOfSpecies("Human")
                    .isFrom("Sontar")
                    .fact(db);
            species("Trion").isFrom("Trion")
                    .fact(db);
            species("Vashta Nerada").isEnemyOf("Doctor", "Donna Noble")
                    .fact(db);
            species("Voord").fact(db);
            tx.success();
        } finally
        {
            tx.finish();
        }
    }

}
