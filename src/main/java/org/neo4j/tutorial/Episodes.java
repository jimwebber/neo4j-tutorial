package org.neo4j.tutorial;

import static org.neo4j.tutorial.EpisodeBuilder.episode;

import org.neo4j.graphdb.Transaction;

public class Episodes {

    private final DoctorWhoUniverse universe;

    public Episodes(DoctorWhoUniverse universe) {
        this.universe = universe;
    }

    public void insert() {
        Transaction tx = universe.getDatabase().beginTx();
        try {
            episode(1).title("An Unearthly Child").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemy("Stone Age Tribe").fact(universe);
            episode(2).title("The Daleks").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemySpecies("Dalek").fact(universe);
            episode(3).title("The Edge of Destruction").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").fact(universe);
            episode(4).title("Marco Polo").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemy("Tegana").fact(universe);
            episode(5).title("The Keys of Marinus").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemy("Yartek").fact(universe);
            episode(6).title("The Aztecs").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemy("Tlotoxl").fact(universe);
            episode(7).title("The Sensorites").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemySpecies("Sensorite").fact(universe);
            episode(8).title("The Reign of Terror").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemy("Robespierre").enemy("Napoleon").fact(universe);
            
            tx.success();
        } finally {
            tx.finish();
        }
    }

}
