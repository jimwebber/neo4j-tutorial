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
            character("Doctor").regenerationSequence("William Hartnell", "Patrick Troughton", "Jon Pertwee", "Tom Baker", "Peter Davison", "Colin Baker", "Sylvester McCoy", "Paul McGann", "Christopher Eccleston", "David Tennant", "Matt Smith").loves("Rose Tyler", "River Song").isA("Timelord").isFrom("Gallifrey").owns("Tarids", "Sonic Screwdriver").fact(universe);
            loadCompanions();
            loadEnemies();
            loadAllies();
            tx.success();
        } finally {
            tx.finish();
        }
    }

    private void loadEnemies() {
        character("Master").regenerationSequence("Roger Delgado", "Peter Pratt", "Geoffrey Beevers", "Anthony Ainley", "Gordon Tipple", "Eric Roberts", "Derek Jacobi", "John Simm").isEnemy().isA("Timelord").isFrom("Gallifrey").owns("Tardis").fact(universe);
        character("Helen A").isA("Human").isFrom("Terra Alpha").isEnemy().fact(universe);
        character("Abzorbaloff").isA("Abrobvian").isFrom("Clom").isEnemy().fact(universe);
        character("Beast").isA("Devil").isEnemy().fact(universe);
        character("Black Guardian").isEnemy().fact(universe);
        character("Bok").isA("Gargoyle").isEnemy().fact(universe);
        character("Cassandra").isA("Human").isFrom("Earth").isEnemy().fact(universe);
        character("Cybercontroller").isA("Cyberman").isFrom("Mondas").isEnemy().fact(universe);
        character("Cyberleader").isA("Cyberman").isFrom("Mondas").isEnemy().fact(universe);
        character("Daemon").isEnemy().fact(universe);
        character("Dalek Caan").isA("Dalek").isFrom("Skaro").isEnemy().fact(universe);
        character("Dalek Jast").isA("Dalek").isFrom("Skaro").isEnemy().fact(universe);
        character("Dalek Sec").isA("Dalek").isFrom("Skaro").isEnemy().fact(universe);
        character("Dalek Thay").isA("Dalek").isFrom("Skaro").isEnemy().fact(universe);
        character("Davros").isA("Kaled").isFrom("Skaro").isEnemy().fact(universe);
        character("Destroyer").isEnemy().fact(universe);
        character("Eldrad").isA("Kastrian").isFrom("Kastria").isEnemy().fact(universe);
        character("Empress of Racnoss").isEnemy().fact(universe);
        character("Fendahl").isEnemy().fact(universe);
        character("General Staal").isA("Sontaran").isFrom("Sontar").isEnemy().fact(universe);
        character("K1 Robot").isEnemy().fact(universe);
        character("Linx").isA("Sontaran").isFrom("Sontar").isEnemy().fact(universe);
        character("Miss Hartigan").isA("Human").isA("Cyberman").isFrom("Earth").isEnemy().fact(universe);
        character("Loch Ness Monster").isA("Skarasen").isEnemy().fact(universe);
        character("Macra").isEnemy().fact(universe);
        character("Morbious").isA("Timelord").isEnemy().fact(universe);
        character("Omega").isA("Timelord").isEnemy().fact(universe);
        character("Ogron").isEnemy().fact(universe);
        character("Pyrovile").isEnemy().fact(universe);
        character("Reaper").isEnemy().fact(universe);
        character("Scaroth").isA("Jagaroth").isEnemy().fact(universe);
        character("Stor").isA("Sontaran").isFrom("Sontar").isEnemy().fact(universe);
        character("Styre").isA("Sontaran").isFrom("Sontar").isEnemy().fact(universe);
        character("Sutekh").isA("Osiron").isEnemy().fact(universe);
        character("Terileptils").isEnemy().fact(universe);
        character("Yartek").isA("Voord").isEnemy().fact(universe);
    }

    private void loadAllies() {
        character("River Song").isA("Human").loves("Doctor").isAlly().fact(universe);
        character("Sergeant Benton").isA("Human").isFrom("Earth").isAlly().fact(universe);
        character("Mike Yates").isA("Human").isFrom("Earth").isAlly().fact(universe);
        character("Brigadier Lethbridge-Stewart").isA("Human").isFrom("Earth").isAlly().fact(universe);
        character("Professor Travers").isA("Human").isFrom("Earth").isAlly().fact(universe);
        character("Alpha Centauri").isA("Alpha Centauran").isFrom("Earth").isAlly().fact(universe);
        character("Duggan").isA("Human").isFrom("Earth").isAlly().fact(universe);
        character("Richard Mace").isA("Human").isFrom("Earth").isAlly().fact(universe);
        character("Chang Lee").isA("Human").isFrom("Earth").isAlly().fact(universe);
    }

    private void loadCompanions() {
        character("Susan Foreman").isA("Timelord").isFrom("Gallifrey").isCompanion().fact(universe);
        character("Romana").isA("Timelord").isFrom("Gallifrey").isCompanion().fact(universe);
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
