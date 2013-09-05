package org.neo4j.tutorial;

import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.AutoIndexer;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

import static org.junit.Assert.assertThat;

import static org.neo4j.tutorial.matchers.CharacterAutoIndexContainsSpecificCharacters.containsSpecificCharacters;

/**
 * After having done the hard work of managing an index for ourselves in the
 * previous Koan, this Koan will introduce auto-indexing which, in exchange for
 * following some conventions, will handle the lifefcycle of nodes and
 * relationships in the indexes automatically.
 */
public class Koan04
{

    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator() );
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Test
    public void shouldCreateAnAutoIndexForAllTheCharacters()
    {

        Set<String> allCharacterNames = getAllCharacterNames();
        AutoIndexer<Node> charactersAutoIndex = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        charactersAutoIndex = universe.getDatabase()
                .index()
                .getNodeAutoIndexer();
        charactersAutoIndex.startAutoIndexingProperty( "character" );
        charactersAutoIndex.setEnabled( true );

        // SNIPPET_END

        Transaction tx = universe.getDatabase()
                .beginTx();

        try
        {
            for ( String characterName : allCharacterNames )
            {
                Node n = universe.getDatabase()
                        .createNode();
                n.setProperty( "character", characterName );
            }
            tx.success();
        }
        finally
        {
            tx.finish();
        }

        assertThat( charactersAutoIndex, containsSpecificCharacters( allCharacterNames ) );
    }

    private Set<String> getAllCharacterNames()
    {
        Index<Node> characters = universe.getDatabase()
                .index()
                .forNodes( "characters" );
        IndexHits<Node> results = characters.query( "character", "*" );

        HashSet<String> characterNames = new HashSet<String>();

        for ( Node character : results )
        {
            characterNames.add( (String) character.getProperty( "character" ) );
        }

        return characterNames;
    }
}
