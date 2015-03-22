package org.neo4j.tutorial.advanced;

import java.util.HashSet;
import java.util.Set;

import org.junit.*;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.AutoIndexer;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.tutorial.DoctorWhoUniverseResource;

import static org.junit.Assert.assertThat;

import static org.neo4j.tutorial.matchers.CharacterAutoIndexContainsSpecificCharacters.containsSpecificCharacters;

/**
 * After having done the hard work of managing an index for ourselves in the
 * previous Koan, this Koan will introduce auto-indexing which, in exchange for
 * following some conventions, will handle the lifefcycle of nodes and
 * relationships in the indexes automatically.
 */

@Ignore("Convert this into a legacy auto index test towards the end of the koans, or better still consider deleting")
public class LegacyAutoIndexesFormerlyKoan04
{
    @ClassRule
    static public DoctorWhoUniverseResource neo4jResource = new DoctorWhoUniverseResource();

    @Test
    public void shouldCreateAnAutoIndexForAllTheCharacters()
    {
        GraphDatabaseService database = neo4jResource.getGraphDatabaseService();
        Set<String> allCharacterNames = getAllCharacterNames();
        AutoIndexer<Node> charactersAutoIndex = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        try ( Transaction tx = database.beginTx() )
        {
            charactersAutoIndex = database
                    .index()
                    .getNodeAutoIndexer();
            charactersAutoIndex.startAutoIndexingProperty( "character" );
            charactersAutoIndex.setEnabled( true );
            tx.success();
        }

        // SNIPPET_END

        try ( Transaction tx = database.beginTx() )
        {
            for ( String characterName : allCharacterNames )
            {
                Node n = database
                        .createNode();
                n.setProperty( "character", characterName );
            }

            assertThat( charactersAutoIndex, containsSpecificCharacters( allCharacterNames ) );
            tx.success();
        }
    }

    private Set<String> getAllCharacterNames()
    {
        try ( Transaction tx = neo4jResource.getGraphDatabaseService().beginTx() )
        {
            Index<Node> characters = neo4jResource.getGraphDatabaseService()
                    .index()
                    .forNodes( "characters" );
            IndexHits<Node> results = characters.query( "character", "*" );

            HashSet<String> characterNames = new HashSet<>();

            for ( Node character : results )
            {
                characterNames.add( (String) character.getProperty( "character" ) );
            }

            tx.success();
            return characterNames;
        }
    }
}
