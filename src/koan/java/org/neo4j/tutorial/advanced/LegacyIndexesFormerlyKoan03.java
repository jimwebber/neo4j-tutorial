package org.neo4j.tutorial.advanced;

import org.junit.*;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.tutorial.DoctorWhoUniverseResource;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.neo4j.tutorial.matchers.ContainsOnlySpecificSpecies.containsOnlySpecies;
import static org.neo4j.tutorial.matchers.ContainsSpecificCompanions.contains;

/**
 * This Koan will introduce indexing based on the built-in index framework based
 * on Lucene. It'll give you a feeling for the wealth of bad guys the Doctor has
 * faced.
 */

@Ignore("Turn this into a Koan on legacy indexes towards the end of the course")
public class LegacyIndexesFormerlyKoan03
{

    @ClassRule
    static public DoctorWhoUniverseResource neo4jResource = new DoctorWhoUniverseResource();

    @Test
    public void shouldRetrieveCharactersIndexFromTheDatabase()
    {
        Index<Node> characters = null;

        try ( Transaction tx = neo4jResource.getGraphDatabaseService().beginTx() )
        {
            // YOUR CODE GOES HERE
            // SNIPPET_START

            characters = neo4jResource.getGraphDatabaseService()
                    .index()
                    .forNodes( "characters" );

            // SNIPPET_END

            assertNotNull( characters );
            assertThat(
                    characters,
                    contains( "Master", "River Song", "Rose Tyler", "Adam Mitchell", "Jack Harkness", "Mickey Smith",
                            "Donna Noble", "Martha Jones" ) );
            tx.success();
        }
    }

    @Test
    public void addingToALegacyIndexShouldBeHandledAsAMutatingOperation()
    {
        GraphDatabaseService database = neo4jResource.getGraphDatabaseService();

        Node abigailPettigrew = createAbigailPettigrew( database );


        // YOUR CODE GOES HERE
        // SNIPPET_START

        try ( Transaction transaction = database.beginTx() )
        {
            database.index()
                    .forNodes( "characters" )
                    .add( abigailPettigrew, "character", abigailPettigrew.getProperty( "character" ) );
            transaction.success();
        }


        // SNIPPET_END

        try ( Transaction transaction = database.beginTx() )
        {
            assertNotNull( database.index()
                    .forNodes( "characters" )
                    .get( "character", "Abigail Pettigrew" )
                    .getSingle() );
            transaction.success();
        }
    }

    @Test
    // TODO: UPGRADE TEST: should be handled by a cypher query with a regex
    public void shouldFindSpeciesBeginningWithCapitalLetterSAndEndingWithLowerCaseLetterNUsingLuceneQuery()
            throws Exception
    {
        GraphDatabaseService database = neo4jResource.getGraphDatabaseService();
        IndexHits<Node> species = null;

        //HINT: while the naming convention in the Doctor Who indexes is
        // Index name: 'actors', index key: 'actor'
        // For the species (due to the English langauage) it is
        // Index name: 'species', index key: 'species'

        // YOUR CODE GOES HERE
        // SNIPPET_START

        try ( Transaction tx = database.beginTx() )
        {
            species = database
                    .index()
                    .forNodes( "species" )
                    .query( "species", "S*n" );
            tx.success();
        }

        // SNIPPET_END

        try ( Transaction tx = database.beginTx() )
        {
            assertThat( species, containsOnlySpecies( "Silurian", "Slitheen", "Sontaran", "Skarasen" ) );
            tx.success();
        }
    }

    /**
     * In this example, it's more important to understand what you *don't* have
     * to do, rather than the work you explicitly have to do. Sometimes indexes
     * just do the right thing...
     */
    @Test
    public void shouldEnsureDatabaseAndIndexInSyncWhenCyberleaderIsDeleted() throws Exception
    {
        GraphDatabaseService database = neo4jResource.getGraphDatabaseService();
        Node cyberleader = retriveCyberleaderFromIndex( database );

        // YOUR CODE GOES HERE
        // SNIPPET_START

        try ( Transaction tx = database.beginTx() )
        {

            for ( Relationship rel : cyberleader.getRelationships() )
            {
                rel.delete();
            }
            cyberleader.delete();
            tx.success();

        }
        // SNIPPET_END

        assertNull( "Cyberleader has not been deleted from the characters index.",
                retriveCyberleaderFromIndex( database ) );

        assertTrue( "Cyberleader has not been deleted from the database.", cyberLeaderDeleted( database,
                cyberleader ) );
    }

    private boolean cyberLeaderDeleted( GraphDatabaseService database, Node cyberleader )
    {
        try ( Transaction tx = database.beginTx() )
        {
            database.getNodeById( cyberleader.getId() );
            return false;
        }
        catch ( NotFoundException nfe )
        {
            return true;
        }
    }

    private Node retriveCyberleaderFromIndex( GraphDatabaseService database )
    {
        try ( Transaction transaction = database.beginTx() )
        {
            Node cyberleader = database.index()
                    .forNodes( "characters" )
                    .get( "character", "Cyberleader" )
                    .getSingle();
            transaction.success();
            return cyberleader;
        }
    }

    private Node createAbigailPettigrew( GraphDatabaseService database )
    {
        Node abigailPettigrew;

        try ( Transaction tx = database.beginTx() )
        {
            abigailPettigrew = database.createNode();
            abigailPettigrew.setProperty( "character", "Abigail Pettigrew" );

            assertNull( database.index()
                    .forNodes( "characters" )
                    .get( "character", "Abigail Pettigrew" )
                    .getSingle() );

            tx.success();
        }

        return abigailPettigrew;
    }
}
