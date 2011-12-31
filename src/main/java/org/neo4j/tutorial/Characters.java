package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import static org.neo4j.tutorial.CharacterBuilder.character;

class Characters
{

    private final GraphDatabaseService db;

    public Characters(GraphDatabaseService db)
    {
        this.db = db;
    }

    public void insert()
    {
        Transaction tx = db.beginTx();
        try
        {
            character("Doctor").regeneration("William Hartnell")
                    .regeneration("Patrick Troughton", 1966)
                    .regeneration("Jon Pertwee", 1970)
                    .regeneration("Tom Baker", 1974)
                    .regeneration("Peter Davison", 1981)
                    .regeneration("Colin Baker", 1984)
                    .regeneration("Sylvester McCoy", 1987)
                    .regeneration("Paul McGann", 1996)
                    .regeneration("Christopher Eccleston", 2005)
                    .regeneration("David Tennant", 2005)
                    .regeneration("Matt Smith", 2010)
                    .loves("Rose Tyler", "River Song")
                    .isA("Timelord")
                    .isFrom("Gallifrey")
                    .owns("Tardis", "Sonic Screwdriver")
                    .fact(db);
            loadCompanions();
            loadEnemies();
            loadAllies();
            tx.success();
        } finally
        {
            tx.finish();
        }
    }

    private void loadEnemies()
    {
        character("Master").regeneration("Roger Delgado", "Peter Pratt", "Geoffrey Beevers", "Anthony Ainley",
                                         "Gordon Tipple", "Eric Roberts", "Derek Jacobi", "John Simm")
                .isEnemy()
                .isA("Timelord")
                .isFrom("Gallifrey")
                .owns("Tardis")
                .fact(db);
        character("Rani").isA("Timelord")
                .isFrom("Gallifrey")
                .owns("Tardis")
                .fact(db);
        character("Meddling Monk").isA("Timelord")
                .isFrom("Gallifrey")
                .owns("Tardis")
                .fact(db);
        character("Helen A").isA("Human")
                .isFrom("Terra Alpha")
                .isEnemy()
                .fact(db);
        character("Abzorbaloff").isA("Abrobvian")
                .isFrom("Clom")
                .isEnemy()
                .fact(db);
        character("Beast").isA("Devil")
                .isEnemy()
                .fact(db);
        character("Black Guardian").isEnemy()
                .fact(db);
        character("Bok").isA("Gargoyle")
                .isEnemy()
                .fact(db);
        character("Cassandra").isA("Human")
                .isFrom("Earth")
                .isEnemy()
                .fact(db);
        character("Cybercontroller").isA("Cyberman")
                .isFrom("Mondas")
                .isEnemy()
                .fact(db);
        character("Cyberleader").isA("Cyberman")
                .isFrom("Mondas")
                .isEnemy()
                .fact(db);
        character("Daemon").isEnemy()
                .fact(db);
        character("Dalek Caan").isA("Dalek")
                .isFrom("Skaro")
                .isEnemy()
                .fact(db);
        character("Dalek Jast").isA("Dalek")
                .isFrom("Skaro")
                .isEnemy()
                .fact(db);
        character("Dalek Sec").isA("Dalek")
                .isFrom("Skaro")
                .isEnemy()
                .fact(db);
        character("Dalek Thay").isA("Dalek")
                .isFrom("Skaro")
                .isEnemy()
                .fact(db);
        character("Davros").isA("Kaled")
                .isFrom("Skaro")
                .isEnemy()
                .fact(db);
        character("Destroyer").isEnemy()
                .fact(db);
        character("Eldrad").isA("Kastrian")
                .isFrom("Kastria")
                .isEnemy()
                .fact(db);
        character("Empress of Racnoss").isEnemy()
                .fact(db);
        character("Fendahl").isEnemy()
                .fact(db);
        character("General Staal").isA("Sontaran")
                .isFrom("Sontar")
                .isEnemy()
                .fact(db);
        character("K1 Robot").isEnemy()
                .fact(db);
        character("Linx").isA("Sontaran")
                .isFrom("Sontar")
                .isEnemy()
                .fact(db);
        character("Miss Hartigan").isA("Human")
                .isA("Cyberman")
                .isFrom("Earth")
                .isEnemy()
                .fact(db);
        character("Loch Ness Monster").isA("Skarasen")
                .isEnemy()
                .fact(db);
        character("Morbious").isA("Timelord")
                .isEnemy()
                .fact(db);
        character("Omega").isA("Timelord")
                .isEnemy()
                .fact(db);
        character("Ogron").isEnemy()
                .fact(db);
        character("Pyrovile").isEnemy()
                .fact(db);
        character("Reaper").isEnemy()
                .fact(db);
        character("Scaroth").isA("Jagaroth")
                .isEnemy()
                .fact(db);
        character("Stor").isA("Sontaran")
                .isFrom("Sontar")
                .isEnemy()
                .fact(db);
        character("Styre").isA("Sontaran")
                .isFrom("Sontar")
                .isEnemy()
                .fact(db);
        character("Sutekh").isA("Osiron")
                .isEnemy()
                .fact(db);
        character("Terileptils").isEnemy()
                .fact(db);
        character("Yartek").isA("Voord")
                .isEnemy()
                .fact(db);
    }

    private void loadAllies()
    {
        character("River Song").isA("Human")
                .loves("Doctor")
                .isAlly()
                .wikipedia("http://en.wikipedia.org/wiki/River_Song_(Doctor_Who)")
                .fact(db);
        character("Sergeant Benton").isA("Human")
                .isFrom("Earth")
                .isAlly()
                .fact(db);
        character("Mike Yates").isA("Human")
                .isFrom("Earth")
                .isAlly()
                .fact(db);
        character("Brigadier Lethbridge-Stewart").isA("Human")
                .isFrom("Earth")
                .isAlly()
                .fact(db);
        character("Professor Travers").isA("Human")
                .isFrom("Earth")
                .isAlly()
                .fact(db);
        character("Alpha Centauri").isA("Alpha Centauran")
                .isFrom("Earth")
                .isAlly()
                .fact(db);
        character("Duggan").isA("Human")
                .isFrom("Earth")
                .isAlly()
                .fact(db);
        character("Richard Mace").isA("Human")
                .isFrom("Earth")
                .isAlly()
                .fact(db);
        character("Chang Lee").isA("Human")
                .isFrom("Earth")
                .isAlly()
                .fact(db);
    }

    private void loadCompanions()
    {
        character("Susan Foreman").isA("Timelord")
                .isFrom("Gallifrey")
                .isCompanion()
                .fact(db);
        character("Romana").isA("Timelord")
                .isFrom("Gallifrey")
                .isCompanion()
                .fact(db);
        character("Barbara Wright").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Ian Chesterton").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Vicki").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Steven Taylor").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Katarina").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Sara Kingdom").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Dodo Chaplet").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Polly").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Ben Jackson").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Jamie McCrimmon").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Hamish Wilson").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Victoria Waterfield").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Zoe Heriot").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Liz Shaw").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Jo Grant").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Sarah Jane Smith").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Harry Sullivan").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Leela").isA("Human")
                .isCompanion()
                .fact(db);
        character("K9").isA("Robotic Canine")
                .isCompanion()
                .fact(db);
        character("Adric").isA("Humanoid")
                .isFrom("Alzarius")
                .isCompanion()
                .fact(db);
        character("Nyssa").isA("Humanoid")
                .isCompanion()
                .fact(db);
        character("Tegan Jovanka").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Vislor Turlough").isA("Trion")
                .isFrom("Trion")
                .isCompanion()
                .fact(db);
        character("Kamelion").isA("Android")
                .isFrom("Xeriphas")
                .isCompanion()
                .fact(db);
        character("Peri Brown").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Melanie Bush").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Ace").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Grace Holloway").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Rose Tyler").isA("Human")
                .isFrom("Earth")
                .loves("Doctor")
                .isCompanion()
                .loves("Doctor")
                .fact(db);
        character("Adam Mitchell").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Jack Harkness").isA("Human")
                .isCompanion()
                .fact(db);
        character("Mickey Smith").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Donna Noble").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Martha Jones").isA("Human")
                .isFrom("Earth")
                .loves("Doctor")
                .isCompanion()
                .fact(db);
        character("Astrid Peth").isA("Human")
                .isFrom("Sto")
                .isCompanion()
                .fact(db);
        character("Jackson Lake").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Rosita Farisi").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Lady Christina de Souza").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Adelaide Brooke").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Wilfred Mott").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .fact(db);
        character("Amy Pond").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .wikipedia("http://en.wikipedia.org/wiki/Amy_Pond")
                .loves("Rory Williams")
                .fact(db);
        character("Rory Williams").isA("Human")
                .isFrom("Earth")
                .isCompanion()
                .wikipedia("http://en.wikipedia.org/wiki/Rory_Williams")
                .loves("Amy Pond")
                .fact(db);
    }

}
