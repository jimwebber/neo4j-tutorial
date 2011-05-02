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
            season01();
            season02();
            season02();
            
            tx.success();
        } finally {
            tx.finish();
        }
    }

    private void season02() {
        episode(9).title("Planet of Giants").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(10).title("The Dalek Invasion of Earth").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(11).title("The Rescue").doctor("William", "Hartnell").companion("Vicki", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(12).title("The Romans").doctor("William", "Hartnell").companion("Vicki", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(13).title("The Web Planet").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(14).title("The Crusade").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(15).title("The Space Museum").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(16).title("The Chase").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright", "Steven Taylor").enemySpecies("Dalek").fact(universe);
        episode(17).title("The Time Meddler").doctor("William", "Hartnell").companion("Vicki", "Steven Taylor").fact(universe);
    }

    private void season01() {
        episode(1).title("An Unearthly Child").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemy("Stone Age Tribe").fact(universe);
        episode(2).title("The Daleks").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemySpecies("Dalek").fact(universe);
        episode(3).title("The Edge of Destruction").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").fact(universe);
        episode(4).title("Marco Polo").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemy("Tegana").fact(universe);
        episode(5).title("The Keys of Marinus").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemy("Yartek").fact(universe);
        episode(6).title("The Aztecs").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemy("Tlotoxl").fact(universe);
        episode(7).title("The Sensorites").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemySpecies("Sensorite").fact(universe);
        episode(8).title("The Reign of Terror").doctor("William", "Hartnell").companion("Susan Foreman", "Ian Chesterton", "Barbara Wright").enemy("Robespierre").enemy("Napoleon").fact(universe);
    }

}
