package org.neo4j.tutorial;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;
import static org.neo4j.tutorial.EpisodeBuilder.episode;

public class Episodes
{
    private final GraphDatabaseService db;
    private final ExecutionEngine engine;

    public Episodes( GraphDatabaseService db )
    {
        this.db = db;
        this.engine = new ExecutionEngine( db, DEV_NULL );
    }

    public void insert()
    {
        try ( Transaction tx = db.beginTx() )
        {
            season01();
            season02();
            season03();
            season04();
            season05();
            season06();
            season07();
            season08();
            season09();
            season10();
            season11();
            season12();
            season13();
            season14();
            season15();
            season16();
            season17();
            season18();
            season19();
            season20();
            episode( 129 ).title( "The Five Doctors" )
                    .doctor( "Richard Hurndall" )
                    .doctor( "William Hartnell" )
                    .doctor( "Patrick Troughton" )
                    .doctor( "Jon Pertwee" )
                    .doctor( "Tom Baker" )
                    .doctor( "Peter Davison" )
                    .companion( "Tegan Jovanka", "Vislor Turlough", "Susan Foreman", "Sarah Jane Smith", "Romana" )
                    .enemy( "Master" )
                    .enemySpecies( "Dalek" )
                    .fact( engine );
            season21();
            season22();
            season23();
            season24();
            season25();
            season26();
            episode( 156 ).title( "Doctor Who" )
                    .doctor( "Paul McGann" )
                    .doctor( "Sylvester McCoy" )
                    .companion( "Grace Holloway" )
                    .enemy( "Master" )
                    .fact( engine );
            season27();
            episode( 167 ).title( "The Christmas Invasion" )
                    .doctor( "David Tennant" )
                    .companion( "Rose Tyler" )
                    .enemySpecies( "Sycorax" )
                    .fact( engine );
            season28();
            episode( 178 ).title( "The Runaway Bride" )
                    .doctor( "David Tennant" )
                    .companion( "Donna Noble" )
                    .enemy( "Empress of Racnoss" )
                    .fact( engine );
            season29();
            episode( 188 ).title( "Voyage of the Damned" )
                    .doctor( "David Tennant" )
                    .companion( "Astrid Peth" )
                    .fact( engine );
            season30();
            season31();
            episode( 213 ).title( "A Christmas Carol" )
                    .doctor( "Matt Smith" )
                    .companion( "Amy Pond", "Rory Williams" )
                    .enemy( "Kazran Sardick" )
                    .fact( engine );
            season32();
            season33();
            season34();


            EpisodeBuilder.reset();

            tx.success();
        }
    }

    private void season34()
    {
        episode( 232 ).title( "The Bells of Saint John" )
                .doctor( "Matt Smith" )
                .companion( "Clara Oswald" )
                .enemy( "The Great Intelligence" )
                .fact( engine );
        episode( 233 ).title( "The Rings of Akhaten" )
                .doctor( "Matt Smith" )
                .companion( "Clara Oswald" )
                .fact( engine );
        episode( 234 ).title( "The Cold War" )
                .doctor( "Matt Smith" )
                .companion( "Clara Oswald" )
                .enemy( "Grand Marshall Skaldak" )
                .fact( engine );
        episode( 235 ).title( "Hide" )
                .doctor( "Matt Smith" )
                .companion( "Clara Oswald" )
                .allies( "Professor Alec Palmer", "Emma Grayling" )
                .fact( engine );
        episode( 236 ).title( "Journey to the Centre of the TARDIS" )
                .doctor( "Matt Smith" )
                .companion( "Clara Oswald" )
                .enemy( "Time Zombie" )
                .fact( engine );
        episode( 237 ).title( "The Crimson Horror" )
                .doctor( "Matt Smith" )
                .companion( "Clara Oswald" )
                .enemy( "Mrs Gillyflower" )
                .fact( engine );
        episode( 238 ).title( "Nightmare in Silver" )
                .doctor( "Matt Smith" )
                .companion( "Clara Oswald" )
                .enemySpecies( "Cyberman" )
                .fact( engine );
        episode( 239 ).title( "The Name of the Doctor" )
                .doctor( "Matt Smith", "John Hurt" )
                .companion( "Clara Oswald", "River Song" )
                .allies( "Madame Vastra", "Jenny Flint", "Strax" )
                .enemy( "The Great Intelligence" )
                .others( "War Doctor" )
                .fact( engine );
        episode( "Special" ).title( "The Night of the Doctor" )
                .doctor( "Paul McGann, John Hurt" )
                .allies( "Sisterhood of Karn" )
                .others( "War Doctor" )
                .fact( engine );
        episode( 240 ).title( "The Day of the Doctor" )
                .doctor( "David Tennant, Matt Smith, John Hurt" )
                .companion( "Clara Oswald" )
                .enemySpecies( "Zygon" )
                .others( "War Doctor" )
                .fact( engine );
        episode( 241 ).title( "The Time of the Doctor" )
                .doctor( "Matt Smith, Peter Capaldi" )
                .companion( "Clara Oswald" )
                .allies( "Tasha Lem" )
                .enemySpecies( "Cyberman", "Dalek" )
                .alliedSpecies( "The Silence" )
                .others( "War Doctor" )
                .fact( engine );
    }

    private void season33()
    {
        episode( 225 ).title( "The Doctor, The Widow, and The Wardrobe" )
                .doctor( "Matt Smith" )
                .alliedSpecies( "Human" )
                .fact( engine );

        episode( 226 ).title( "Asylum of the Daleks" )
                .doctor( "Matt Smith" )
                .allies( "Oswin Oswald" )
                .companion( "Amy Pond", "Rory Williams" )
                .enemySpecies( "Dalek" )
                .enemy( "Darla von Karlsen", "Harvey" )
                .fact( engine );

        episode( 227 ).title( "Dinosaurs on a Spaceship" )
                .doctor( "Matt Smith" )
                .allies( "Queen Nefertiti", "Riddell", "Brian Williams" )
                .companion( "Amy Pond", "Rory Williams" )
                .enemy( "Solomon" )
                .fact( engine );

        episode( 228 ).title( "A Town Called Mercy" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .others( "Gunslinger", "Kahler-Jex" )
                .fact( engine );

        episode( 229 ).title( "The Power of Three" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .enemy( "Shakri" )
                .fact( engine );

        episode( 230 ).title( "The Angels Take Manhattan" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .enemySpecies( "Weeping Angel" )
                .fact( engine );

        episode( 231 ).title( "The Snowmen" )
                .doctor( "Matt Smith" )
                .companion( "Clara Oswald" )
                .enemy( "The Great Intelligence" )
                .fact( engine );
    }

    private void season32()
    {
        episode( "214a" ).title( "The Impossible Astronaut" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams", "River Song" )
                .allies( "Richard Nixon", "Canton Everett Delaware III" )
                .enemySpecies( "The Silence" )
                .fact( engine );
        episode( "214b" ).title( "Day of the Moon" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams", "River Song" )
                .enemySpecies( "The Silence" )
                .fact( engine );
        episode( 215 ).title( "The Curse of the Black Spot" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .allies( "Captain Avery" )
                .fact( engine );
        episode( 216 ).title( "The Doctor's Wife" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .enemy( "House" )
                .fact( engine );
        episode( 217 ).title( "The Rebel Flesh" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .fact( engine );
        episode( 217 ).title( "The Almost People" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .fact( engine );
        episode( "218" ).title( "A Good Man Goes to War" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams", "River Song" )
                .allies( "Commander Strax", "Madame Vastra", "Jenny", "Dorium Maldovar" )
                .alliedSpecies( "Judoon", "Silurian" )
                .enemySpecies( "Cyberman" )
                .enemy( "Madame Kovarian" )
                .fact( engine );
        episode( "219" ).title( "Let's Kill Hitler!" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .enemy( "River Song" )
                .fact( engine );
        episode( 220 ).title( "Night Terrors" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .allies( "Alex" )
                .enemySpecies( "Peg Dolls" )
                .fact( engine );
        episode( 221 ).title( "The Girl Who Waited" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .enemySpecies( "Handbots" )
                .fact( engine );
        episode( 222 ).title( "The God Complex" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .enemy( "Minotaur" )
                .fact( engine );
        episode( 223 ).title( "Closing Time" )
                .doctor( "Matt Smith" )
                .companion( "Craig Owens" )
                .enemySpecies( "Cyberman" )
                .fact( engine );
        episode( 224 ).title( "The Wedding of River Song" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams", "River Song" )
                .allies( "Winston Churchill", "Charles Dickens", "Dorium Maldovar" )
                .enemySpecies( "The Silence", "Dalek" )
                .enemy( "Madame Kovarian" )
                .fact( engine );

    }

    private void season31()
    {
        episode( 199 ).title( "The Next Doctor" )
                .doctor( "David Tennant" )
                .companion( "Jackson Lake", "Rosita Farisi" )
                .enemy( "Miss Hartigan" )
                .enemySpecies( "Cyberman" )
                .fact( engine );
        episode( 200 ).title( "Planet of the Dead" )
                .doctor( "David Tennant" )
                .companion( "Lady Christina de Souza" )
                .fact( engine );
        episode( 201 ).title( "The Waters of Mars" )
                .doctor( "David Tennant" )
                .companion( "Adelaide Brooke" )
                .fact( engine );
        episode( 202 ).title( "The End of Time" )
                .doctor( "David Tennant" )
                .doctor( "Matt Smith" )
                .companion( "Wilfred Mott" )
                .enemy( "Master", "Lord President" )
                .fact( engine );
        episode( 203 ).title( "The Eleventh Hour" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond" )
                .enemy( "Prisoner Zero" )
                .fact( engine );
        episode( 204 ).title( "The Beast Below" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond" )
                .enemy( "Prisoner Zero" )
                .fact( engine );
        episode( 206 ).title( "Victory of the Daleks" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 206 ).title( "The Time of Angels" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond" )
                .enemySpecies( "Weeping Angel" )
                .fact( engine );
        episode( 206 ).title( "Flesh and Stone" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond" )
                .enemySpecies( "Weeping Angel" )
                .fact( engine );
        episode( 207 ).title( "The Vampires of Venice" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond" )
                .enemy( "Signora Calvierri" )
                .fact( engine );
        episode( 208 ).title( "Amy's Choice" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .enemySpecies( "Eknodine" )
                .fact( engine );
        episode( 209 ).title( "The Hungry Earth" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .enemySpecies( "Silurian" )
                .fact( engine );
        episode( 209 ).title( "Cold Blood" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .enemySpecies( "Silurian" )
                .fact( engine );
        episode( 210 ).title( "Vincent and the Doctor" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond" )
                .fact( engine );
        episode( 211 ).title( "The Lodger" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond" )
                .fact( engine );
        episode( 212 ).title( "The Pandorica Opens" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .enemySpecies( "Dalek", "Auton", "Cyberman", "Sontaran", "Judoon", "Sycorax", "Hoix", "Silurian",
                        "Roboform" )
                .fact( engine );
        episode( 212 ).title( "The Big Bang" )
                .doctor( "Matt Smith" )
                .companion( "Amy Pond", "Rory Williams" )
                .fact( engine );
    }

    private void season30()
    {
        episode( 189 ).title( "Partners in Crime" )
                .doctor( "David Tennant" )
                .companion( "Donna Noble" )
                .enemy( "Miss Foster" )
                .fact( engine );
        episode( 190 ).title( "The Fires of Pompeii" )
                .doctor( "David Tennant" )
                .companion( "Donna Noble" )
                .enemy( "Pyrovile" )
                .fact( engine );
        episode( 191 ).title( "Planet of the Ood" )
                .doctor( "David Tennant" )
                .companion( "Donna Noble" )
                .fact( engine );
        episode( 192 ).title( "The Sontaran Stratagem" )
                .doctor( "David Tennant" )
                .companion( "Donna Noble", "Martha Jones" )
                .enemy( "General Staal" )
                .enemySpecies( "Sontaran" )
                .fact( engine );
        episode( 192 ).title( "The Poison Sky" )
                .doctor( "David Tennant" )
                .companion( "Donna Noble", "Martha Jones" )
                .enemy( "General Staal" )
                .enemySpecies( "Sontaran" )
                .fact( engine );
        episode( 193 ).title( "The Doctor's Daughter" )
                .doctor( "David Tennant" )
                .companion( "Donna Noble", "Martha Jones" )
                .enemy( "General Cobb" )
                .fact( engine );
        episode( 194 ).title( "The Unicorn and the Wasp" )
                .doctor( "David Tennant" )
                .companion( "Donna Noble" )
                .fact( engine );
        episode( 195 ).title( "Silence in the Library" )
                .doctor( "David Tennant" )
                .companion( "Donna Noble", "River Song" )
                .enemySpecies( "Vashta Nerada" )
                .fact( engine );
        episode( 195 ).title( "Forest of the Dead" )
                .doctor( "David Tennant" )
                .companion( "Donna Noble", "River Song" )
                .enemySpecies( "Vashta Nerada" )
                .fact( engine );
        episode( 196 ).title( "Midnight" )
                .doctor( "David Tennant" )
                .companion( "Donna Noble" )
                .fact( engine );
        episode( 197 ).title( "Turn Left" )
                .doctor( "David Tennant" )
                .companion( "Donna Noble", "Rose Tyler" )
                .fact( engine );
        episode( 198 ).title( "The Stolen Earth" )
                .doctor( "David Tennant" )
                .companion( "Donna Noble", "Rose Tyler", "Martha Jones", "Jack Harkness", "Sarah Jane Smith" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 198 ).title( "Journey's End" )
                .doctor( "David Tennant" )
                .companion( "Donna Noble", "Rose Tyler", "Martha Jones", "Jack Harkness", "Sarah Jane Smith", "K9" )
                .enemySpecies( "Dalek" )
                .fact( engine );
    }

    private void season29()
    {
        episode( 179 ).title( "Smith and Jones" )
                .doctor( "David Tennant" )
                .companion( "Martha Jones" )
                .enemySpecies( "Plasmavore" )
                .fact( engine );
        episode( 180 ).title( "The Shakespeare Code" )
                .doctor( "David Tennant" )
                .companion( "Martha Jones" )
                .enemySpecies( "Carrionite" )
                .fact( engine );
        episode( 181 ).title( "Gridlock" )
                .doctor( "David Tennant" )
                .companion( "Martha Jones" )
                .enemySpecies( "Macra" )
                .fact( engine );
        episode( 182 ).title( "Daleks in Manhattan" )
                .doctor( "David Tennant" )
                .companion( "Martha Jones" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 182 ).title( "Evolution of the Daleks" )
                .doctor( "David Tennant" )
                .companion( "Martha Jones" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 183 ).title( "The Lazarus Experiment" )
                .doctor( "David Tennant" )
                .companion( "Martha Jones" )
                .enemy( "Lazarus" )
                .fact( engine );
        episode( 184 ).title( "42" )
                .doctor( "David Tennant" )
                .companion( "Martha Jones" )
                .fact( engine );
        episode( 185 ).title( "Human Nature" )
                .doctor( "David Tennant" )
                .companion( "Martha Jones" )
                .enemy( "Family of Blood" )
                .fact( engine );
        episode( 185 ).title( "Family of Blood" )
                .doctor( "David Tennant" )
                .companion( "Martha Jones" )
                .enemy( "Family of Blood" )
                .fact( engine );
        episode( 186 ).title( "Blink" )
                .doctor( "David Tennant" )
                .companion( "Martha Jones" )
                .enemySpecies( "Weeping Angel" )
                .fact( engine );
        episode( 187 ).title( "Utopia" )
                .doctor( "David Tennant" )
                .companion( "Martha Jones", "Jack Harkness" )
                .enemy( "Master" )
                .fact( engine );
        episode( 187 ).title( "The Sound of Drums" )
                .doctor( "David Tennant" )
                .companion( "Martha Jones", "Jack Harkness" )
                .enemy( "Master" )
                .fact( engine );
        episode( 187 ).title( "Last of the Time Lords" )
                .doctor( "David Tennant" )
                .companion( "Martha Jones", "Jack Harkness" )
                .enemy( "Master" )
                .fact( engine );
    }

    private void season28()
    {
        episode( 168 ).title( "New Earth" )
                .doctor( "David Tennant" )
                .companion( "Rose Tyler" )
                .fact( engine );
        episode( 169 ).title( "Tooth and Claw" )
                .doctor( "David Tennant" )
                .companion( "Rose Tyler" )
                .fact( engine );
        episode( 170 ).title( "School Reunion" )
                .doctor( "David Tennant" )
                .companion( "Rose Tyler", "Mickey Smith", "Sarah Jane Smith", "K9" )
                .enemySpecies( "Krillitane" )
                .fact( engine );
        episode( 171 ).title( "The Girl in the Fireplace" )
                .doctor( "David Tennant" )
                .companion( "Rose Tyler", "Mickey Smith" )
                .enemySpecies( "Clockwork Android" )
                .fact( engine );
        episode( 172 ).title( "Rise of the Cybermen" )
                .doctor( "David Tennant" )
                .companion( "Rose Tyler", "Mickey Smith" )
                .enemySpecies( "Cyberman" )
                .fact( engine );
        episode( 172 ).title( "The Age of Steel" )
                .doctor( "David Tennant" )
                .companion( "Rose Tyler", "Mickey Smith" )
                .enemySpecies( "Cyberman" )
                .fact( engine );
        episode( 173 ).title( "The Idiot's Lantern" )
                .doctor( "David Tennant" )
                .companion( "Rose Tyler" )
                .enemy( "The Wire" )
                .fact( engine );
        episode( 174 ).title( "The Impossible Planet" )
                .doctor( "David Tennant" )
                .companion( "Rose Tyler" )
                .enemy( "Beast" )
                .fact( engine );
        episode( 174 ).title( "The Satan Pit" )
                .doctor( "David Tennant" )
                .companion( "Rose Tyler" )
                .enemy( "Beast" )
                .fact( engine );
        episode( 175 ).title( "Love & Monsters" )
                .doctor( "David Tennant" )
                .companion( "Rose Tyler" )
                .enemy( "Abzorbaloff" )
                .fact( engine );
        episode( 176 ).title( "Fear Her" )
                .doctor( "David Tennant" )
                .companion( "Rose Tyler" )
                .fact( engine );
        episode( 177 ).title( "Army of Ghosts" )
                .doctor( "David Tennant" )
                .companion( "Rose Tyler" )
                .others( "Adeola Oshodi" )
                .enemySpecies( "Cyberman", "Dalek" )
                .fact( engine );
        episode( 177 ).title( "Doomsday" )
                .doctor( "David Tennant" )
                .companion( "Rose Tyler" )
                .enemySpecies( "Cyberman", "Dalek" )
                .fact( engine );
    }

    private void season27()
    {
        episode( 157 ).title( "Rose" )
                .doctor( "Christopher Eccleston" )
                .companion( "Rose Tyler" )
                .enemySpecies( "Auton" )
                .fact( engine );
        episode( 158 ).title( "The End of the World" )
                .doctor( "Christopher Eccleston" )
                .companion( "Rose Tyler" )
                .enemy( "Cassandra" )
                .fact( engine );
        episode( 159 ).title( "The Unquiet Dead" )
                .doctor( "Christopher Eccleston" )
                .companion( "Rose Tyler" )
                .enemy( "Gabriel Sneed" )
                .fact( engine );
        episode( 160 ).title( "Aliens of London" )
                .doctor( "Christopher Eccleston" )
                .companion( "Rose Tyler" )
                .enemySpecies( "Slitheen" )
                .fact( engine );
        episode( 160 ).title( "World War Three" )
                .doctor( "Christopher Eccleston" )
                .companion( "Rose Tyler" )
                .enemySpecies( "Slitheen" )
                .fact( engine );
        episode( 161 ).title( "Dalek" )
                .doctor( "Christopher Eccleston" )
                .companion( "Rose Tyler" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 162 ).title( "The Long Game" )
                .doctor( "Christopher Eccleston" )
                .enemy( "The Editor" )
                .companion( "Rose Tyler" )
                .fact( engine );
        episode( 163 ).title( "Father's Day" )
                .doctor( "Christopher Eccleston" )
                .companion( "Rose Tyler" )
                .fact( engine );
        episode( 164 ).title( "The Empty Child" )
                .doctor( "Christopher Eccleston" )
                .companion( "Rose Tyler", "Jack Harkness" )
                .fact( engine );
        episode( 164 ).title( "The Doctor Dances" )
                .doctor( "Christopher Eccleston" )
                .companion( "Rose Tyler", "Jack Harkness" )
                .fact( engine );
        episode( 165 ).title( "Boom Town" )
                .doctor( "Christopher Eccleston" )
                .companion( "Rose Tyler", "Jack Harkness" )
                .enemySpecies( "Slitheen" )
                .fact( engine );
        episode( 166 ).title( "Bad Wolf" )
                .doctor( "Christopher Eccleston" )
                .companion( "Rose Tyler", "Jack Harkness" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 166 ).title( "The Parting of the Ways" )
                .doctor( "Christopher Eccleston" )
                .doctor( "David Tennant" )
                .companion( "Rose Tyler", "Jack Harkness" )
                .enemySpecies( "Dalek" )
                .fact( engine );
    }

    private void season26()
    {
        episode( 152 ).title( "Battlefield" )
                .doctor( "Sylvester McCoy" )
                .companion( "Ace" )
                .fact( engine );
        episode( 153 ).title( "Ghost Light" )
                .doctor( "Sylvester McCoy" )
                .companion( "Ace" )
                .enemy( "Josiah Samuel Smith" )
                .fact( engine );
        episode( 154 ).title( "The Curse of Fenric" )
                .doctor( "Sylvester McCoy" )
                .companion( "Ace" )
                .enemy( "Fenric" )
                .fact( engine );
        episode( 155 ).title( "Survival" )
                .doctor( "Sylvester McCoy" )
                .companion( "Ace" )
                .enemy( "Master" )
                .fact( engine );
    }

    private void season25()
    {
        episode( 148 ).title( "Remembrance of the Daleks" )
                .doctor( "Sylvester McCoy" )
                .companion( "Ace" )
                .enemy( "Davros" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 149 ).title( "The Happiness Patrol" )
                .doctor( "Sylvester McCoy" )
                .companion( "Ace" )
                .enemy( "Helen A" )
                .fact( engine );
        episode( 150 ).title( "Silver Nemesis" )
                .doctor( "Sylvester McCoy" )
                .companion( "Ace" )
                .enemySpecies( "Cyberman" )
                .fact( engine );
        episode( 151 ).title( "The Greatest Show in the Galaxy" )
                .doctor( "Sylvester McCoy" )
                .companion( "Ace" )
                .fact( engine );
    }

    private void season24()
    {
        episode( 144 ).title( "Time and the Rani" )
                .doctor( "Colin Baker" )
                .doctor( "Sylvester McCoy" )
                .companion( "Melanie Bush" )
                .enemy( "Rani" )
                .fact( engine );
        episode( 145 ).title( "Paradise Towers" )
                .doctor( "Sylvester McCoy" )
                .companion( "Melanie Bush" )
                .enemy( "Kroagnon" )
                .fact( engine );
        episode( 146 ).title( "Delta and the Bannermen" )
                .doctor( "Sylvester McCoy" )
                .companion( "Melanie Bush" )
                .enemy( "Gavrok" )
                .fact( engine );
        episode( 147 ).title( "Dragonfire" )
                .doctor( "Sylvester McCoy" )
                .companion( "Melanie Bush", "Ace" )
                .enemy( "Kane" )
                .fact( engine );
    }

    private void season23()
    {
        episode( 143 ).title( "The Mysterious Planet" )
                .doctor( "Colin Baker" )
                .companion( "Peri Brown" )
                .fact( engine );
        episode( 143 ).title( "Mindwarp" )
                .doctor( "Colin Baker" )
                .companion( "Peri Brown" )
                .fact( engine );
        episode( 143 ).title( "Terror of the Vervoids" )
                .doctor( "Colin Baker" )
                .companion( "Melanie Bush" )
                .fact( engine );
        episode( 143 ).title( "The Ultimate Foe" )
                .doctor( "Colin Baker" )
                .companion( "Melanie Bush" )
                .enemy( "Master" )
                .fact( engine );
    }

    private void season22()
    {
        episode( 136 ).title( "The Twin Dilemma" )
                .doctor( "Colin Baker" )
                .companion( "Peri Brown" )
                .fact( engine );
        episode( 137 ).title( "Attack of the Cybermen" )
                .doctor( "Colin Baker" )
                .companion( "Peri Brown" )
                .fact( engine );
        episode( 138 ).title( "Vengeance on Varos" )
                .doctor( "Colin Baker" )
                .companion( "Peri Brown" )
                .fact( engine );
        episode( 139 ).title( "The Mark of the Rani" )
                .doctor( "Colin Baker" )
                .companion( "Peri Brown" )
                .enemy( "Master", "Rani" )
                .fact( engine );
        episode( 140 ).title( "The Two Doctors" )
                .doctor( "Colin Baker" )
                .doctor( "Patrick Troughton" )
                .companion( "Peri Brown", "Jamie McCrimmon" )
                .enemy( "Shockeye", "Chessene", "Dastari" )
                .enemySpecies( "Sontaran" )
                .fact( engine );
        episode( 141 ).title( "Timelash" )
                .doctor( "Colin Baker" )
                .companion( "Peri Brown" )
                .enemy( "Borad" )
                .fact( engine );
        episode( 142 ).title( "Revelation of the Daleks" )
                .doctor( "Colin Baker" )
                .companion( "Peri Brown" )
                .enemySpecies( "Dalek" )
                .fact( engine );
    }

    private void season21()
    {
        episode( 130 ).title( "Warriors of the Deep" )
                .doctor( "Peter Davison" )
                .companion( "Tegan Jovanka", "Vislor Turlough" )
                .enemySpecies( "Silurian", "Sea Devil" )
                .fact( engine );
        episode( 131 ).title( "The Awakening" )
                .doctor( "Peter Davison" )
                .companion( "Tegan Jovanka", "Vislor Turlough" )
                .enemy( "Malus" )
                .fact( engine );
        episode( 132 ).title( "Frontios" )
                .doctor( "Peter Davison" )
                .companion( "Tegan Jovanka", "Vislor Turlough" )
                .enemySpecies( "Tractator" )
                .fact( engine );
        episode( 133 ).title( "Resurrection of the Daleks" )
                .doctor( "Peter Davison" )
                .companion( "Tegan Jovanka", "Vislor Turlough" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 134 ).title( "Planet of Fire" )
                .doctor( "Peter Davison" )
                .companion( "Tegan Jovanka", "Vislor Turlough", "Peri Brown" )
                .enemy( "Master" )
                .fact( engine );
        episode( 135 ).title( "The Caves of Androzani" )
                .doctor( "Peter Davison" )
                .doctor( "Colin Baker" )
                .companion( "Peri Brown" )
                .enemy( "Master" )
                .fact( engine );
    }

    private void season20()
    {
        episode( 123 ).title( "Arc of Infinity" )
                .doctor( "Peter Davison" )
                .companion( "Nyssa", "Tegan Jovanka" )
                .enemy( "Omega" )
                .fact( engine );
        episode( 124 ).title( "Snakedance" )
                .doctor( "Peter Davison" )
                .companion( "Nyssa", "Tegan Jovanka" )
                .enemy( "Mara" )
                .fact( engine );
        episode( 125 ).title( "Mawdryn Undead" )
                .doctor( "Peter Davison" )
                .companion( "Nyssa", "Tegan Jovanka", "Vislor Turlough" )
                .enemy( "Mawdryn", "Black Guardian" )
                .fact( engine );
        episode( 126 ).title( "Terminus" )
                .doctor( "Peter Davison" )
                .companion( "Nyssa", "Tegan Jovanka", "Vislor Turlough" )
                .enemy( "Vanir" )
                .fact( engine );
        episode( 127 ).title( "Enlightenment" )
                .doctor( "Peter Davison" )
                .companion( "Tegan Jovanka", "Vislor Turlough" )
                .enemy( "Black Guardian" )
                .fact( engine );
        episode( 128 ).title( "The King's Demons" )
                .doctor( "Peter Davison" )
                .companion( "Tegan Jovanka", "Vislor Turlough", "Kamelion" )
                .enemy( "Master" )
                .fact( engine );
    }

    private void season19()
    {
        episode( 116 ).title( "Castrovalva" )
                .doctor( "Peter Davison" )
                .companion( "Adric", "Nyssa", "Tegan Jovanka" )
                .enemy( "Master" )
                .fact( engine );
        episode( 117 ).title( "Four to Doomsday" )
                .doctor( "Peter Davison" )
                .companion( "Adric", "Nyssa", "Tegan Jovanka" )
                .enemy( "Monarch" )
                .fact( engine );
        episode( 118 ).title( "Kinda" )
                .doctor( "Peter Davison" )
                .companion( "Adric", "Nyssa", "Tegan Jovanka" )
                .enemy( "Mara" )
                .fact( engine );
        episode( 119 ).title( "The Visitation" )
                .doctor( "Peter Davison" )
                .companion( "Adric", "Nyssa", "Tegan Jovanka" )
                .enemy( "Terileptils" )
                .fact( engine );
        episode( 120 ).title( "Black Orchid" )
                .doctor( "Peter Davison" )
                .companion( "Adric", "Nyssa", "Tegan Jovanka" )
                .enemy( "George Cranleigh" )
                .fact( engine );
        episode( 121 ).title( "Earthshock" )
                .doctor( "Peter Davison" )
                .companion( "Adric", "Nyssa", "Tegan Jovanka" )
                .enemySpecies( "Cyberman" )
                .fact( engine );
        episode( 122 ).title( "Time-Flight" )
                .doctor( "Peter Davison" )
                .companion( "Nyssa", "Tegan Jovanka" )
                .enemy( "Master" )
                .fact( engine );
    }

    private void season18()
    {
        episode( 109 ).title( "The Leisure Hive" )
                .doctor( "Tom Baker" )
                .companion( "Romana", "K9" )
                .fact( engine );
        episode( 110 ).title( "Meglos" )
                .doctor( "Tom Baker" )
                .companion( "Romana", "K9" )
                .enemy( "Meglos" )
                .fact( engine );
        episode( 111 ).title( "Full Circle" )
                .doctor( "Tom Baker" )
                .companion( "Romana", "K9", "Adric" )
                .enemySpecies( "Marshman" )
                .fact( engine );
        episode( 112 ).title( "State of Decay" )
                .doctor( "Tom Baker" )
                .companion( "Romana", "K9", "Adric" )
                .enemy( "Zargo", "Camilla", "Aukon" )
                .fact( engine );
        episode( 113 ).title( "Warriors' Gate" )
                .doctor( "Tom Baker" )
                .companion( "Romana", "K9", "Adric" )
                .fact( engine );
        episode( 114 ).title( "The Keeper of Traken" )
                .doctor( "Tom Baker" )
                .companion( "Adric" )
                .enemy( "Master" )
                .fact( engine );
        episode( 115 ).title( "Logopolis" )
                .doctor( "Tom Baker" )
                .companion( "Adric", "Nyssa", "Tegan Jovanka" )
                .enemy( "Master" )
                .fact( engine );
    }

    private void season17()
    {
        episode( 104 ).title( "Destiny of the Daleks" )
                .doctor( "Tom Baker" )
                .companion( "Romana" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 105 ).title( "City of Death" )
                .doctor( "Tom Baker" )
                .companion( "Romana" )
                .enemy( "Scaroth" )
                .fact( engine );
        episode( 106 ).title( "The Creature from the Pit" )
                .doctor( "Tom Baker" )
                .companion( "Romana", "K9" )
                .enemy( "Erato", "Lady Adrasta" )
                .fact( engine );
        episode( 107 ).title( "Nightmare of Eden" )
                .doctor( "Tom Baker" )
                .companion( "Romana", "K9" )
                .enemySpecies( "Mandrel" )
                .fact( engine );
        episode( 108 ).title( "The Horns of Nimon" )
                .doctor( "Tom Baker" )
                .companion( "Romana", "K9" )
                .enemySpecies( "Nimon" )
                .fact( engine );
    }

    private void season16()
    {
        episode( 98 ).title( "The Ribos Operation" )
                .doctor( "Tom Baker" )
                .companion( "Romana", "K9" )
                .enemy( "Graff Vynda-K", "Black Guardian" )
                .fact( engine );
        episode( 99 ).title( "The Pirate Planet" )
                .doctor( "Tom Baker" )
                .companion( "Romana", "K9" )
                .enemy( "Pirate Captain" )
                .fact( engine );
        episode( 100 ).title( "The Stones of Blood" )
                .doctor( "Tom Baker" )
                .companion( "Romana", "K9" )
                .enemy( "de Vries" )
                .enemySpecies( "Ogri" )
                .fact( engine );
        episode( 101 ).title( "The Androids of Tara" )
                .doctor( "Tom Baker" )
                .companion( "Romana", "K9" )
                .enemy( "Count Grendel of Gracht" )
                .fact( engine );
        episode( 102 ).title( "The Power of Kroll" )
                .doctor( "Tom Baker" )
                .companion( "Romana" )
                .enemy( "Kroll" )
                .fact( engine );
        episode( 103 ).title( "The Armageddon Factor" )
                .doctor( "Tom Baker" )
                .companion( "Romana", "K9" )
                .enemy( "The Shadow", "Black Guardian" )
                .fact( engine );
    }

    private void season15()
    {
        episode( 92 ).title( "Horror of Fang Rock" )
                .doctor( "Tom Baker" )
                .companion( "Leela" )
                .fact( engine );
        episode( 93 ).title( "The Invisible Enemy" )
                .doctor( "Tom Baker" )
                .companion( "Leela", "K9" )
                .enemy( "Nucleus" )
                .fact( engine );
        episode( 94 ).title( "Image of the Fendahl" )
                .doctor( "Tom Baker" )
                .companion( "Leela" )
                .enemy( "Fendahl" )
                .fact( engine );
        episode( 95 ).title( "The Sun Makers" )
                .doctor( "Tom Baker" )
                .companion( "Leela", "K9" )
                .enemy( "Collector" )
                .fact( engine );
        episode( 96 ).title( "Underworld" )
                .doctor( "Tom Baker" )
                .companion( "Leela", "K9" )
                .enemy( "Oracle" )
                .fact( engine );
        episode( 97 ).title( "The Invasion of Time" )
                .doctor( "Tom Baker" )
                .companion( "Leela", "K9" )
                .enemySpecies( "Sontaran" )
                .enemy( "Stor" )
                .enemySpecies( "Sontaran" )
                .fact( engine );
    }

    private void season14()
    {
        episode( 86 ).title( "The Masque of Mandragora" )
                .doctor( "Tom Baker" )
                .companion( "Sarah Jane Smith" )
                .enemy( "Count Federico", "Captain Rossini" )
                .fact( engine );
        episode( 87 ).title( "The Hand of Fear" )
                .doctor( "Tom Baker" )
                .companion( "Sarah Jane Smith" )
                .enemy( "Eldrad" )
                .fact( engine );
        episode( 88 ).title( "The Deadly Assassin" )
                .doctor( "Tom Baker" )
                .enemy( "Master" )
                .fact( engine );
        episode( 89 ).title( "The Face of Evil" )
                .doctor( "Tom Baker" )
                .companion( "Leela" )
                .enemy( "Xoanon" )
                .fact( engine );
        episode( 90 ).title( "The Robots of Death" )
                .doctor( "Tom Baker" )
                .companion( "Leela" )
                .fact( engine );
        episode( 91 ).title( "The Talons of Weng-Chiang" )
                .doctor( "Tom Baker" )
                .companion( "Leela" )
                .enemy( "Li H'sen Chang" )
                .fact( engine );
    }

    private void season13()
    {
        episode( 80 ).title( "Terror of the Zygons" )
                .doctor( "Tom Baker" )
                .companion( "Sarah Jane Smith", "Harry Sullivan" )
                .enemy( "Skarasen" )
                .enemySpecies( "Zygon" )
                .fact( engine );
        episode( 81 ).title( "Planet of Evil" )
                .doctor( "Tom Baker" )
                .companion( "Sarah Jane Smith" )
                .fact( engine );
        episode( 82 ).title( "Pyramids of Mars" )
                .doctor( "Tom Baker" )
                .companion( "Sarah Jane Smith" )
                .enemy( "Sutekh" )
                .fact( engine );
        episode( 83 ).title( "The Android Invasion" )
                .doctor( "Tom Baker" )
                .companion( "Sarah Jane Smith" )
                .enemySpecies( "Android" )
                .fact( engine );
        episode( 84 ).title( "The Brain of Morbius" )
                .doctor( "Tom Baker" )
                .companion( "Sarah Jane Smith" )
                .enemy( "Morbius", "Doctor Solon" )
                .fact( engine );
        episode( 85 ).title( "The Seeds of Doom" )
                .doctor( "Tom Baker" )
                .companion( "Sarah Jane Smith" )
                .enemy( "Harrison Chase" )
                .fact( engine );
    }

    private void season12()
    {
        episode( 75 ).title( "Robot" )
                .doctor( "Tom Baker" )
                .companion( "Sarah Jane Smith", "Harry Sullivan" )
                .enemy( "K1 Robot" )
                .fact( engine );
        episode( 76 ).title( "The Ark in Space" )
                .doctor( "Tom Baker" )
                .companion( "Sarah Jane Smith", "Harry Sullivan" )
                .fact( engine );
        episode( 77 ).title( "The Sontaran Experiment" )
                .doctor( "Tom Baker" )
                .companion( "Sarah Jane Smith", "Harry Sullivan" )
                .enemy( "Styre" )
                .enemySpecies( "Sontaran" )
                .fact( engine );
        episode( 78 ).title( "Genesis of the Daleks" )
                .doctor( "Tom Baker" )
                .companion( "Sarah Jane Smith", "Harry Sullivan" )
                .enemy( "Davros" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 79 ).title( "Revenge of the Cybermen" )
                .doctor( "Tom Baker" )
                .companion( "Sarah Jane Smith", "Harry Sullivan" )
                .enemy( "Cyberleader" )
                .enemySpecies( "Cyberman" )
                .fact( engine );
    }

    private void season11()
    {
        episode( 70 ).title( "The Time Warrior" )
                .doctor( "Jon Pertwee" )
                .companion( "Sarah Jane Smith" )
                .enemy( "Linx" )
                .enemySpecies( "Sontaran" )
                .fact( engine );
        episode( 71 ).title( "Invasion of the Dinosaurs" )
                .doctor( "Jon Pertwee" )
                .companion( "Sarah Jane Smith" )
                .fact( engine );
        episode( 72 ).title( "Death to the Daleks" )
                .doctor( "Jon Pertwee" )
                .companion( "Sarah Jane Smith" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 73 ).title( "The Monster of Peladon" )
                .doctor( "Jon Pertwee" )
                .companion( "Sarah Jane Smith" )
                .enemy( "Chancellor Ortron" )
                .fact( engine );
        episode( 74 ).title( "Planet of the Spiders" )
                .doctor( "Jon Pertwee" )
                .doctor( "Tom Baker" )
                .companion( "Sarah Jane Smith" )
                .fact( engine );
    }

    private void season10()
    {
        episode( 65 ).title( "The Three Doctors" )
                .doctor( "Jon Pertwee" )
                .doctor( "Patrick Troughton" )
                .doctor( "William Hartnell" )
                .companion( "Jo Grant" )
                .enemy( "Omega" )
                .fact( engine );
        episode( 66 ).title( "Carnival of Monsters" )
                .doctor( "Jon Pertwee" )
                .companion( "Jo Grant" )
                .fact( engine );
        episode( 67 ).title( "Frontier in Space" )
                .doctor( "Jon Pertwee" )
                .companion( "Jo Grant" )
                .enemy( "Master" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 68 ).title( "Planet of the Daleks" )
                .doctor( "Jon Pertwee" )
                .companion( "Jo Grant" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 69 ).title( "The Green Death" )
                .doctor( "Jon Pertwee" )
                .companion( "Jo Grant" )
                .enemy( "BOSS" )
                .fact( engine );
    }

    private void season09()
    {
        episode( 60 ).title( "Day of the Daleks" )
                .doctor( "Jon Pertwee" )
                .companion( "Jo Grant" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 61 ).title( "The Curse of Peladon" )
                .doctor( "Jon Pertwee" )
                .companion( "Jo Grant" )
                .fact( engine );
        episode( 62 ).title( "The Sea Devils" )
                .doctor( "Jon Pertwee" )
                .companion( "Jo Grant" )
                .enemy( "Master" )
                .enemySpecies( "Sea Devil" )
                .fact( engine );
        episode( 63 ).title( "The Mutants" )
                .doctor( "Jon Pertwee" )
                .enemy( "The Marshal" )
                .companion( "Jo Grant" )
                .fact( engine );
        episode( 64 ).title( "The Time Monster" )
                .doctor( "Jon Pertwee" )
                .enemy( "Master" )
                .companion( "Jo Grant" )
                .fact( engine );
    }

    private void season08()
    {
        episode( 55 ).title( "Terror of the Autons" )
                .doctor( "Jon Pertwee" )
                .companion( "Jo Grant" )
                .enemy( "Master" )
                .enemySpecies( "Auton" )
                .fact( engine );
        episode( 56 ).title( "The Mind of Evil" )
                .doctor( "Jon Pertwee" )
                .companion( "Jo Grant" )
                .enemy( "Master" )
                .fact( engine );
        episode( 57 ).title( "The Claws of Axos" )
                .doctor( "Jon Pertwee" )
                .companion( "Jo Grant" )
                .enemy( "Master" )
                .enemySpecies( "Axon" )
                .fact( engine );
        episode( 58 ).title( "Colony in Space" )
                .doctor( "Jon Pertwee" )
                .companion( "Jo Grant" )
                .enemy( "Master" )
                .fact( engine );
        episode( 59 ).title( "The Daemons" )
                .doctor( "Jon Pertwee" )
                .companion( "Jo Grant" )
                .enemy( "Bok", "Master" )
                .fact( engine );
    }

    private void season07()
    {
        episode( 51 ).title( "Spearhead from Space" )
                .doctor( "Jon Pertwee" )
                .companion( "Liz Shaw" )
                .enemySpecies( "Auton" )
                .fact( engine );
        episode( 52 ).title( "Doctor Who and the Silurians" )
                .doctor( "Jon Pertwee" )
                .companion( "Liz Shaw" )
                .enemySpecies( "Silurian" )
                .fact( engine );
        episode( 53 ).title( "The Ambassadors of Death" )
                .doctor( "Jon Pertwee" )
                .companion( "Liz Shaw" )
                .enemy( "Reegan" )
                .fact( engine );
        episode( 54 ).title( "Inferno" )
                .doctor( "Jon Pertwee" )
                .companion( "Liz Shaw" )
                .fact( engine );
    }

    private void season06()
    {
        episode( 44 ).title( "The Dominators" )
                .doctor( "Patrick Troughton" )
                .companion( "Jamie McCrimmon", "Zoe Heriot" )
                .enemySpecies( "Dominator", "Quark" )
                .fact( engine );
        episode( 45 ).title( "The Mind Robber" )
                .doctor( "Patrick Troughton" )
                .companion( "Jamie McCrimmon", "Zoe Heriot" )
                .enemy( "Master" )
                .fact( engine );
        episode( 46 ).title( "The Invasion" )
                .doctor( "Patrick Troughton" )
                .companion( "Jamie McCrimmon", "Zoe Heriot" )
                .fact( engine );
        episode( 47 ).title( "The Krotons" )
                .doctor( "Patrick Troughton" )
                .companion( "Jamie McCrimmon", "Zoe Heriot" )
                .enemySpecies( "Kroton" )
                .fact( engine );
        episode( 48 ).title( "The Seeds of Death" )
                .doctor( "Patrick Troughton" )
                .companion( "Jamie McCrimmon", "Zoe Heriot" )
                .enemySpecies( "Ice Warrior" )
                .fact( engine );
        episode( 49 ).title( "The Space Pirates" )
                .doctor( "Patrick Troughton" )
                .companion( "Jamie McCrimmon", "Zoe Heriot" )
                .enemy( "Caven", "Dervish" )
                .fact( engine );
        episode( 50 ).title( "The War Games" )
                .doctor( "Patrick Troughton" )
                .companion( "Jamie McCrimmon", "Zoe Heriot" )
                .enemy( "War Chief" )
                .enemySpecies( "Dalek" )
                .fact( engine );
    }

    private void season05()
    {
        episode( 37 ).title( "The Tomb of the Cybermen" )
                .doctor( "Patrick Troughton" )
                .companion( "Jamie McCrimmon", "Victoria Waterfield" )
                .enemySpecies( "Cyberman" )
                .fact( engine );
        episode( 38 ).title( "The Abominable Snowmen" )
                .doctor( "Patrick Troughton" )
                .companion( "Jamie McCrimmon", "Victoria Waterfield" )
                .fact( engine );
        episode( 39 ).title( "The Ice Warriors" )
                .doctor( "Patrick Troughton" )
                .companion( "Jamie McCrimmon", "Victoria Waterfield" )
                .enemySpecies( "Ice Warrior" )
                .fact( engine );
        episode( 40 ).title( "The Enemy of the World" )
                .doctor( "Patrick Troughton" )
                .companion( "Jamie McCrimmon", "Victoria Waterfield" )
                .fact( engine );
        episode( 41 ).title( "The Web of Fear" )
                .doctor( "Patrick Troughton" )
                .companion( "Jamie McCrimmon", "Victoria Waterfield" )
                .fact( engine );
        episode( 42 ).title( "Fury from the Deep" )
                .doctor( "Patrick Troughton" )
                .companion( "Jamie McCrimmon", "Victoria Waterfield" )
                .fact( engine );
        episode( 43 ).title( "The Wheel in Space" )
                .doctor( "Patrick Troughton" )
                .companion( "Jamie McCrimmon", "Victoria Waterfield" )
                .enemySpecies( "Cyberman" )
                .fact( engine );
    }

    private void season04()
    {
        episode( 30 ).title( "The Power of the Daleks" )
                .doctor( "Patrick Troughton" )
                .companion( "Polly", "Ben Jackson" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 31 ).title( "The Highlanders" )
                .doctor( "Patrick Troughton" )
                .companion( "Polly", "Ben Jackson", "Jamie McCrimmon" )
                .fact( engine );
        episode( 32 ).title( "The Underwater Menace" )
                .doctor( "Patrick Troughton" )
                .companion( "Polly", "Ben Jackson", "Jamie McCrimmon" )
                .enemy( "Zaroff" )
                .fact( engine );
        episode( 33 ).title( "The Moonbase" )
                .doctor( "Patrick Troughton" )
                .companion( "Polly", "Ben Jackson", "Jamie McCrimmon" )
                .enemySpecies( "Cyberman" )
                .fact( engine );
        episode( 34 ).title( "The Macra Terror" )
                .doctor( "Patrick Troughton" )
                .companion( "Polly", "Ben Jackson", "Jamie McCrimmon" )
                .enemySpecies( "Macra" )
                .fact( engine );
        episode( 35 ).title( "The Faceless Ones" )
                .doctor( "Patrick Troughton" )
                .companion( "Polly", "Ben Jackson", "Jamie McCrimmon" )
                .fact( engine );
        episode( 36 ).title( "The Evil of the Daleks" )
                .doctor( "Patrick Troughton" )
                .companion( "Jamie McCrimmon", "Victoria Waterfield" )
                .enemySpecies( "Dalek" )
                .fact( engine );
    }

    private void season03()
    {
        episode( 18 ).title( "Galaxy 4" )
                .doctor( "William Hartnell" )
                .companion( "Vicki", "Steven Taylor" )
                .enemySpecies( "Drahvin" )
                .fact( engine );
        episode( 19 ).title( "Mission to the Unknown" )
                .fact( engine );
        episode( 20 ).title( "The Myth Makers" )
                .doctor( "William Hartnell" )
                .companion( "Vicki", "Steven Taylor", "Katarina" )
                .fact( engine );
        episode( 21 ).title( "The Daleks' Master Plan" )
                .doctor( "William Hartnell" )
                .companion( "Steven Taylor", "Katarina", "Sara Kingdom" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 22 ).title( "The Massacre of St Bartholomew's Eve" )
                .doctor( "William Hartnell" )
                .companion( "Dodo Chaplet", "Steven Taylor" )
                .fact( engine );
        episode( 23 ).title( "The Ark" )
                .doctor( "William Hartnell" )
                .companion( "Dodo Chaplet", "Steven Taylor" )
                .fact( engine );
        episode( 24 ).title( "The Celestial Toymaker" )
                .doctor( "William Hartnell" )
                .companion( "Dodo Chaplet", "Steven Taylor" )
                .enemy( "The Toymaker" )
                .fact( engine );
        episode( 25 ).title( "The Gunfighters" )
                .doctor( "William Hartnell" )
                .companion( "Dodo Chaplet", "Steven Taylor" )
                .fact( engine );
        episode( 26 ).title( "The Savages" )
                .doctor( "William Hartnell" )
                .companion( "Dodo Chaplet", "Steven Taylor" )
                .fact( engine );
        episode( 27 ).title( "The War Machines" )
                .doctor( "William Hartnell" )
                .companion( "Dodo Chaplet", "Steven Taylor", "Polly" )
                .enemy( "WOTAN" )
                .fact( engine );
        episode( 28 ).title( "The Smugglers" )
                .doctor( "William Hartnell" )
                .companion( "Polly", "Ben Jackson" )
                .fact( engine );
        episode( 29 ).title( "The Tenth Planet" )
                .doctor( "William Hartnell" )
                .doctor( "Patrick Troughton" )
                .companion( "Polly", "Ben Jackson" )
                .enemySpecies( "Cyberman" )
                .fact( engine );
    }

    private void season02()
    {
        episode( 9 ).title( "Planet of Giants" )
                .doctor( "William Hartnell" )
                .companion( "Susan Foreman", "Ian Chesterton", "Barbara Wright" )
                .fact( engine );
        episode( 10 ).title( "The Dalek Invasion of Earth" )
                .doctor( "William Hartnell" )
                .companion( "Susan Foreman", "Ian Chesterton", "Barbara Wright" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 11 ).title( "The Rescue" )
                .doctor( "William Hartnell" )
                .companion( "Vicki", "Ian Chesterton", "Barbara Wright" )
                .fact( engine );
        episode( 12 ).title( "The Romans" )
                .doctor( "William Hartnell" )
                .companion( "Vicki", "Ian Chesterton", "Barbara Wright" )
                .fact( engine );
        episode( 13 ).title( "The Web Planet" )
                .doctor( "William Hartnell" )
                .companion( "Susan Foreman", "Ian Chesterton", "Barbara Wright" )
                .fact( engine );
        episode( 14 ).title( "The Crusade" )
                .doctor( "William Hartnell" )
                .companion( "Susan Foreman", "Ian Chesterton", "Barbara Wright" )
                .fact( engine );
        episode( 15 ).title( "The Space Museum" )
                .doctor( "William Hartnell" )
                .companion( "Susan Foreman", "Ian Chesterton", "Barbara Wright" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 16 ).title( "The Chase" )
                .doctor( "William Hartnell" )
                .companion( "Susan Foreman", "Ian Chesterton", "Barbara Wright", "Steven Taylor" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 17 ).title( "The Time Meddler" )
                .doctor( "William Hartnell" )
                .companion( "Vicki", "Steven Taylor" )
                .enemy( "Meddling Monk" )
                .fact( engine );
    }

    private void season01()
    {
        episode( 1 ).title( "An Unearthly Child" )
                .doctor( "William Hartnell" )
                .companion( "Susan Foreman", "Ian Chesterton", "Barbara Wright" )
                .enemy( "Stone Age Tribe" )
                .fact( engine );
        episode( 2 ).title( "The Daleks" )
                .doctor( "William Hartnell" )
                .companion( "Susan Foreman", "Ian Chesterton", "Barbara Wright" )
                .enemySpecies( "Dalek" )
                .fact( engine );
        episode( 3 ).title( "The Edge of Destruction" )
                .doctor( "William Hartnell" )
                .companion( "Susan Foreman", "Ian Chesterton", "Barbara Wright" )
                .fact( engine );
        episode( 4 ).title( "Marco Polo" )
                .doctor( "William Hartnell" )
                .companion( "Susan Foreman", "Ian Chesterton", "Barbara Wright" )
                .enemy( "Tegana" )
                .fact( engine );
        episode( 5 ).title( "The Keys of Marinus" )
                .doctor( "William Hartnell" )
                .companion( "Susan Foreman", "Ian Chesterton", "Barbara Wright" )
                .enemy( "Yartek" )
                .fact( engine );
        episode( 6 ).title( "The Aztecs" )
                .doctor( "William Hartnell" )
                .companion( "Susan Foreman", "Ian Chesterton", "Barbara Wright" )
                .enemy( "Tlotoxl" )
                .fact( engine );
        episode( 7 ).title( "The Sensorites" )
                .doctor( "William Hartnell" )
                .companion( "Susan Foreman", "Ian Chesterton", "Barbara Wright" )
                .enemySpecies( "Sensorite" )
                .fact( engine );
        episode( 8 ).title( "The Reign of Terror" )
                .doctor( "William Hartnell" )
                .companion( "Susan Foreman", "Ian Chesterton", "Barbara Wright" )
                .enemy( "Robespierre" )
                .enemy( "Napoleon" )
                .fact( engine );
    }

}
