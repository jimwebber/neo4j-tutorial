package org.neo4j.tutorial;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;
import static org.neo4j.tutorial.CharacterBuilder.character;

class Characters
{
    private final GraphDatabaseService db;
    private final ExecutionEngine engine;

    public Characters( GraphDatabaseService db )
    {
        this.db = db;
        this.engine = new ExecutionEngine( db, DEV_NULL );
    }

    public void insert()
    {
        try ( Transaction tx = db.beginTx() )
        {
            loadTheDoctor();
            loadCompanions();
            loadEnemies();
            loadAllies();

            tx.success();
        }
    }

    private void loadTheDoctor()
    {
        character( "Doctor" )
                .regeneration( "William Hartnell" )
                .regeneration( "Patrick Troughton", 1966 )
                .regeneration( "Jon Pertwee", 1970 )
                .regeneration( "Tom Baker", 1974 )
                .regeneration( "Peter Davison", 1981 )
                .regeneration( "Colin Baker", 1984 )
                .regeneration( "Sylvester McCoy", 1987 )
                .regeneration( "Paul McGann", 1996 )
                .regeneration( "John Hurt", 2013 )
                .regeneration( "Christopher Eccleston", 2005 )
                .regeneration( "David Tennant", 2005 )
                .regeneration( "Matt Smith", 2010 )
                .regeneration( "Peter Capaldi", 2013 )
                .loves( "Rose Tyler", "River Song" )
                .isA( "Timelord" )
                .isFrom( "Gallifrey" )
                .owns( "Tardis", "Sonic Screwdriver" )
                .fact( engine );
    }

    private void loadEnemies()
    {
        character( "Rassilon" )
                .isEnemy()
                .isA( "Timelord" )
                .isFrom( "Gallifrey" )
                .fact( engine );
        character( "Master" )
                .regeneration( "Roger Delgado", "Peter Pratt", "Geoffrey Beevers", "Anthony Ainley",
                        "Gordon Tipple", "Eric Roberts", "Derek Jacobi", "John Simm" )
                .isEnemy()
                .isA( "Timelord" )
                .isFrom( "Gallifrey" )
                .owns( "Tardis" )
                .fact( engine );
        character( "Rani" )
                .isA( "Timelord" )
                .isFrom( "Gallifrey" )
                .owns( "Tardis" )
                .fact( engine );
        character( "Meddling Monk" )
                .isA( "Timelord" )
                .isFrom( "Gallifrey" )
                .owns( "Tardis" )
                .fact( engine );
        character( "Helen A" )
                .isA( "Human" )
                .isFrom( "Terra Alpha" )
                .isEnemy()
                .fact( engine );
        character( "Abzorbaloff" )
                .isA( "Abrobvian" )
                .isFrom( "Clom" )
                .isEnemy()
                .fact( engine );
        character( "Beast" )
                .isA( "Devil" )
                .isEnemy()
                .fact( engine );
        character( "Black Guardian" )
                .isEnemy()
                .fact( engine );
        character( "Bok" )
                .isA( "Gargoyle" )
                .isEnemy()
                .fact( engine );
        character( "Cassandra" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isEnemy()
                .fact( engine );
        character( "Cybercontroller" )
                .isA( "Cyberman" )
                .isFrom( "Mondas" )
                .isEnemy()
                .fact( engine );
        character( "Cyberleader" )
                .isA( "Cyberman" )
                .isFrom( "Mondas" )
                .isEnemy()
                .fact( engine );
        character( "Daemon" )
                .isEnemy()
                .fact( engine );
        character( "Dalek Caan" )
                .isA( "Dalek" )
                .isFrom( "Skaro" )
                .isEnemy()
                .fact( engine );
        character( "Dalek Jast" )
                .isA( "Dalek" )
                .isFrom( "Skaro" )
                .isEnemy()
                .fact( engine );
        character( "Dalek Sec" )
                .isA( "Dalek" )
                .isFrom( "Skaro" )
                .isEnemy()
                .fact( engine );
        character( "Dalek Thay" )
                .isA( "Dalek" )
                .isFrom( "Skaro" )
                .isEnemy()
                .fact( engine );
        character( "Davros" )
                .isA( "Kaled" )
                .isFrom( "Skaro" )
                .isEnemy()
                .fact( engine );
        character( "Destroyer" )
                .isEnemy()
                .fact( engine );
        character( "Eldrad" )
                .isA( "Kastrian" )
                .isFrom( "Kastria" )
                .isEnemy()
                .fact( engine );
        character( "Empress of Racnoss" )
                .isEnemy()
                .fact( engine );
        character( "Fendahl" )
                .isEnemy()
                .fact( engine );
        character( "General Staal" )
                .isA( "Sontaran" )
                .isFrom( "Sontar" )
                .isEnemy()
                .fact( engine );
        character( "Grand Marshall Skaldak" )
                .isA( "Ice Warrior" )
                .isFrom( "Mars" )
                .isEnemy()
                .fact( engine );
        character( "K1 Robot" )
                .isEnemy()
                .fact( engine );
        character( "Linx" )
                .isA( "Sontaran" )
                .isFrom( "Sontar" )
                .isEnemy()
                .fact( engine );
        character( "Miss Hartigan" )
                .isA( "Human" )
                .isA( "Cyberman" )
                .isFrom( "Earth" )
                .isEnemy()
                .fact( engine );
        character( "Linx" )
                .isA( "Sontaran" )
                .isFrom( "Sontar" )
                .isEnemy()
                .fact( engine );
        character( "Loch Ness Monster" )
                .isA( "Skarasen" )
                .isEnemy()
                .fact( engine );
        character( "Morbius" )
                .isA( "Timelord" )
                .isEnemy()
                .fact( engine );
        character( "Omega" )
                .isA( "Timelord" )
                .isEnemy()
                .fact( engine );
        character( "Ogron" )
                .isEnemy()
                .fact( engine );
        character( "Pyrovile" )
                .isEnemy()
                .fact( engine );
        character( "Reaper" )
                .isEnemy()
                .fact( engine );
        character( "Scaroth" )
                .isA( "Jagaroth" )
                .isEnemy()
                .fact( engine );
        character( "Stor" )
                .isA( "Sontaran" )
                .isFrom( "Sontar" )
                .isEnemy()
                .fact( engine );
        character( "Styre" )
                .isA( "Sontaran" )
                .isFrom( "Sontar" )
                .isEnemy()
                .fact( engine );
        character( "Sutekh" )
                .isA( "Osiron" )
                .isEnemy()
                .fact( engine );
        character( "Terileptils" )
                .isEnemy()
                .fact( engine );
        character( "Yartek" )
                .isA( "Voord" )
                .isEnemy()
                .fact( engine );
        character( "Darla von Karlsen" )
                .isA( "Human" )
                .isA( "Dalek" )
                .isEnemy()
                .fact( engine );
        character( "Harvey" )
                .isA( "Human" )
                .isA( "Dalek" )
                .isEnemy()
                .fact( engine );
        character( "Solomon" )
                .isA( "Humanoid" )
                .isEnemy()
                .fact( engine );
    }

    private void loadAllies()
    {
        character( "Riddell" )
                .isA( "Human" )
                .isAlly()
                .fact( engine );
        character( "Brian Williams" )
                .isA( "Human" )
                .fatherOf( "Rory Williams" )
                .isAlly()
                .firstAppearedIn( 226, "Asylum of the Daleks" )
                .fact( engine );
        character( "Queen Nefertiti" )
                .isA( "Human" )
                .isAlly()
                .fact( engine );
        character( "River Song" )
                .isA( "Human" )
                .loves( "Doctor" )
                .isAlly()
                .wikipedia( "http://en.wikipedia.org/wiki/River_Song_(Doctor_Who)" )
                .fact( engine );
        character( "Sergeant Benton" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isAlly()
                .fact( engine );
        character( "Mike Yates" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isAlly()
                .fact( engine );
        character( "Brigadier Lethbridge-Stewart" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isAlly()
                .firstAppearedIn( 41, "The Web of Fear" )
                .diedIn( 223, "Closing Time" )
                .fact( engine );
        character( "Professor Travers" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isAlly()
                .fact( engine );
        character( "Alpha Centauri" )
                .isA( "Alpha Centauran" )
                .isFrom( "Earth" )
                .isAlly()
                .fact( engine );
        character( "Duggan" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isAlly()
                .fact( engine );
        character( "Richard Mace" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isAlly()
                .fact( engine );
        character( "Chang Lee" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isAlly()
                .fact( engine );
        character( "Oswin Oswald" )
                .isA( "Human" )
                .isA( "Dalek" )
                .isAlly()
                .fact( engine );
        character( "Clara Oswin Oswald" )
                .isA( "Human" )
                .isAlly()
                .fact( engine );

        character( "War Doctor" )
                .firstAppearedIn( 239, "The Name of the Doctor" )
                .wikipedia( "http://en.wikipedia.org/wiki/War_Doctor" )
                .playedBy( "John Hurt" )
                .fact( engine );
    }

    private void loadCompanions()
    {
        character( "Susan Foreman" )
                .isA( "Timelord" )
                .isFrom( "Gallifrey" )
                .isCompanion()
                .fact( engine );
        character( "Romana" )
                .isA( "Timelord" )
                .isFrom( "Gallifrey" )
                .isCompanion()
                .fact( engine );
        character( "Barbara Wright" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Ian Chesterton" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Vicki" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Steven Taylor" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Katarina" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Sara Kingdom" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Dodo Chaplet" ).isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Polly" ).isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Ben Jackson" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Jamie McCrimmon" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Hamish Wilson" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Victoria Waterfield" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Zoe Heriot" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Liz Shaw" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Jo Grant" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Sarah Jane Smith" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Harry Sullivan" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Leela" )
                .isA( "Human" )
                .isCompanion()
                .fact( engine );
        character( "K9" )
                .isA( "Robotic Canine" )
                .isCompanion()
                .fact( engine );
        character( "Adric" )
                .isA( "Humanoid" )
                .isFrom( "Alzarius" )
                .isCompanion()
                .fact( engine );
        character( "Nyssa" )
                .isA( "Humanoid" )
                .isCompanion()
                .fact( engine );
        character( "Tegan Jovanka" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Vislor Turlough" )
                .isA( "Trion" )
                .isFrom( "Trion" )
                .isCompanion()
                .fact( engine );
        character( "Kamelion" )
                .isA( "Android" )
                .isFrom( "Xeriphas" )
                .isCompanion()
                .fact( engine );
        character( "Peri Brown" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Melanie Bush" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Ace" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Grace Holloway" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Rose Tyler" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .loves( "Doctor" )
                .isCompanion()
                .loves( "Doctor" )
                .fact( engine );
        character( "Adam Mitchell" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Jack Harkness" )
                .isA( "Human" )
                .isCompanion()
                .fact( engine );
        character( "Mickey Smith" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Donna Noble" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Martha Jones" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .loves( "Doctor" )
                .isCompanion()
                .fact( engine );
        character( "Astrid Peth" )
                .isA( "Human" )
                .isFrom( "Sto" )
                .isCompanion()
                .fact( engine );
        character( "Jackson Lake" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Rosita Farisi" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Lady Christina de Souza" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Adelaide Brooke" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Craig Owens" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Wilfred Mott" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .fact( engine );
        character( "Amy Pond" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .wikipedia( "http://en.wikipedia.org/wiki/Amy_Pond" )
                .loves( "Rory Williams" )
                .firstAppearedIn( 203, "The Eleventh Hour" )
                .diedIn( 229, "The Power of Three" )
                .fact( engine );
        character( "Rory Williams" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .wikipedia( "http://en.wikipedia.org/wiki/Rory_Williams" )
                .loves( "Amy Pond" )
                .firstAppearedIn( 203, "The Eleventh Hour" )
                .diedIn( 229, "The Power of Three" )
                .fact( engine );
        character( "Clara Oswald" )
                .isA( "Human" )
                .isFrom( "Earth" )
                .isCompanion()
                .wikipedia( "http://en.wikipedia.org/wiki/Clara_Oswald" )
                .firstAppearedIn( 226, "Asylum of the Daleks" )
                .fact( engine );
    }
}
