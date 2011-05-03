package org.neo4j.tutorial;

import org.neo4j.graphdb.Transaction;

public class Friendlies {

    private final DoctorWhoUniverse universe;

    public Friendlies(DoctorWhoUniverse universe) {
        this.universe = universe;
    }

    public void insertAndIndex() {
        Transaction tx = universe.getDatabase().beginTx();
        try {
            

            tx.success();
        } finally {
            tx.finish();
        }
    }
}
