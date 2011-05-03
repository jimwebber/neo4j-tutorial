package org.neo4j.tutorial;

import static org.neo4j.tutorial.CharacterBuilder.character;

import org.neo4j.graphdb.Transaction;

public class Characters {

    private final DoctorWhoUniverse universe;

    public Characters(DoctorWhoUniverse universe) {
        this.universe = universe;
    }

    public void insert() {
        Transaction tx = universe.getDatabase().beginTx();
        try {
            character("Doctor").loves("Rose Tyler", "River Song").isA("Timelord").isFrom("Galifrey").owns("Tarids", "Sonic Screwdriver").fact(universe);
            loadCompanions();
            loadEnemies();
            loadAllies();
            tx.success();
        } finally {
            tx.finish();
        }
    }

    private void loadEnemies() {
        character("Master").isEnemy().isA("Timelord").isFrom("Gallifrey").owns("Tardis").fact(universe);
    }

    private void loadAllies() {
        character("River Song").isA("Human").loves("Doctor").fact(universe);
    }

    private void loadCompanions() {
        character("Susan Foreman").isA("Timelord").isFrom("Galifrey").isCompanion().fact(universe);
        character("Romana").isA("Timelord").isFrom("Galifrey").isCompanion().fact(universe);
        character("Barbara Wright").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Ian Chesterton").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Vicki").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Steven Taylor").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Katarina").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Sara Kingdom").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Dodo Chaplet").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Polly").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Ben Jackson").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Jamie McCrimmon").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Hamish Wilson").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Victoria Waterfield").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Zoe Heriot").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Liz Shaw").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Jo Grant").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Sarah Jane Smith").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Harry Sullivan").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Leela").isA("Human").isCompanion().fact(universe);
        character("K9").isA("Robotic Canine").isCompanion().fact(universe);
        character("Adric").isA("Humanoid").isFrom("Alzarius").isCompanion().fact(universe);
        character("Nyssa").isA("Humanoid").isCompanion().fact(universe);
        character("Tegan Jovanka").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Vislor Turlough").isA("Trion").isFrom("Trion").isCompanion().fact(universe);
        character("Kamelion").isA("Android").isFrom("Xeriphas").isCompanion().fact(universe);
        character("Peri Brown").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Melanie Bush").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Ace").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Grace Holloway").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Rose Tyler").isA("Human").isFrom("Earth").loves("Doctor").isCompanion().loves("Doctor").fact(universe);
        character("Adam Mitchell").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Jack Harkness").isA("Human").isCompanion().fact(universe);
        character("Mickey Smith").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Donna Noble").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Martha Jones").isA("Human").isFrom("Earth").loves("Doctor").isCompanion().fact(universe);
        character("Astrid Peth").isA("Human").isFrom("Sto").isCompanion().fact(universe);
        character("Jackson Lake").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Rosita Farisi").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Lady Christina de Souza").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Adelaide Brooke").isA("Human").isFrom("Earth").isCompanion().fact(universe);
        character("Wilfred Mott").isA("Human").isFrom("Earth").isCompanion().fact(universe);
    }

}
