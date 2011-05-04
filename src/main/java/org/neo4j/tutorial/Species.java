package org.neo4j.tutorial;

import static org.neo4j.tutorial.SpeciesBuilder.species;

import org.neo4j.graphdb.Transaction;

public class Species {

    private final DoctorWhoUniverse universe;

    public Species(DoctorWhoUniverse universe) {
        this.universe = universe;
    }

    public void insert() {
        Transaction tx = universe.getDatabase().beginTx();
        try {
            species("Timelord").isEnemyOfSpecies("Dalek").isFrom("Gallifrey").fact(universe);
            species("Abrobvian").isEnemyOf("Doctor").isFrom("Clom").fact(universe);
            species("Android").fact(universe);
            species("Auton").isEnemyOf("Doctor").isEnemyOfSpecies("Human").isFrom("Polymos").fact(universe);
            species("Axon").isEnemyOf("Doctor").isEnemyOfSpecies("Human").fact(universe);
            species("Devil").isEnemyOf("Doctor", "Rose Tyler").isFrom("Impossible Planet").fact(universe);
            species("Cyberman").isEnemyOf("Doctor").isEnemyOfSpecies("Dalek").isFrom("Mondas").fact(universe);
            species("Dalek").isEnemyOf("Doctor").isEnemyOfSpecies("Cyberman", "Thaal", "Mechonoids", "Human").isFrom("Skaro").fact(universe);
            species("Gargoyle").isEnemyOf("Doctor").fact(universe);
            species("Ice Warrior").isEnemyOf("Doctor").isFrom("Mars").fact(universe);
            species("Human").isFrom("Earth").fact(universe);
            species("Humanoid").fact(universe);
            species("Jagrafess").isEnemyOf("Doctor").fact(universe);
            species("Jagaroth").fact(universe);
            species("Kaled").isEnemyOf("Doctor").isFrom("Skaro").fact(universe);
            species("Kastrian").isFrom("Kastria").fact(universe);
            species("Mechonoids").isFrom("Mechanus").fact(universe);
            species("Ood").isFrom("Ood Sphere").fact(universe);
            species("Osiron").isEnemyOf("Doctor").fact(universe);
            species("Robotic Canine").fact(universe);
            species("Sea Devil").isEnemyOf("Doctor").isEnemyOfSpecies("Human").isFrom("Earth").fact(universe);
            species("Silurian").isEnemyOf("Doctor").isEnemyOfSpecies("Human").isFrom("Earth").fact(universe);
            species("Skarasen").isEnemyOf("Doctor").fact(universe);
            species("Slitheen").isEnemyOf("Doctor").isEnemyOfSpecies("Human").isFrom("Raxacoricofallapatorius").fact(universe);
            species("Sontaran").isEnemyOf("Doctor", "Martha Jones").isEnemyOfSpecies("Human").isFrom("Sontar").fact(universe);
            species("Trion").isFrom("Trion").fact(universe);
            species("Vashta Nerada").isEnemyOf("Doctor", "Donna Noble").fact(universe);
            species("Voord").fact(universe);
            tx.success();
        } finally {
            tx.finish();
        }
    }

}
