package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

import static org.junit.Assert.*;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificSpecies.containsOnlySpecies;
import static org.neo4j.tutorial.matchers.ContainsSpecificCompanions.contains;

/**
 * This Koan will introduce indexing based on the built-in index framework based
 * on Lucene. It'll give you a feeling for the wealth of bad guys the Doctor has
 * faced.
 */
public class Koan03
{

    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse(new DoctorWhoUniverseGenerator());
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Test
    public void shouldRetrieveCharactersIndexFromTheDatabase()
    {
        Index<Node> characters = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        characters = universe.getDatabase()
                             .index()
                             .forNodes("characters");

        // SNIPPET_END

        assertNotNull(characters);
        assertThat(
                characters,
                contains("Master", "River Song", "Rose Tyler", "Adam Mitchell", "Jack Harkness", "Mickey Smith",
                         "Donna Noble", "Martha Jones"));
    }

    @Test
    public void addingToAnIndexShouldBeHandledAsAMutatingOperation()
    {
        GraphDatabaseService db = universe.getDatabase();
        Node abigailPettigrew = createAbigailPettigrew(db);

        assertNull(db.index()
                     .forNodes("characters")
                     .get("character", "Abigail Pettigrew")
                     .getSingle());

        // YOUR CODE GOES HERE
        // SNIPPET_START

        Transaction transaction = db.beginTx();
        try
        {
            db.index()
              .forNodes("characters")
              .add(abigailPettigrew, "character", abigailPettigrew.getProperty("character"));
            transaction.success();
        } finally
        {
            transaction.finish();
        }

        // SNIPPET_END

        assertNotNull(db.index()
                        .forNodes("characters")
                        .get("character", "Abigail Pettigrew")
                        .getSingle());
    }

    @Test
    public void shouldFindSpeciesBeginningWithCapitalLetterSAndEndingWithLowerCaseLetterNUsingLuceneQuery() throws Exception
    {
        IndexHits<Node> species = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        species = universe.getDatabase()
                          .index()
                          .forNodes("species")
                          .query("species", "S*n");

        // SNIPPET_END

        assertThat(species, containsOnlySpecies("Silurian", "Slitheen", "Sontaran", "Skarasen"));
    }

    /**
     * In this example, it's more important to understand what you *don't* have
     * to do, rather than the work you explicitly have to do. Sometimes indexes
     * just do the right thing...
     */
    @Test
    public void shouldEnsureDatabaseAndIndexInSyncWhenCyberleaderIsDeleted() throws Exception
    {
        GraphDatabaseService db = universe.getDatabase();
        Node cyberleader = retriveCyberleaderFromIndex(db);

        // YOUR CODE GOES HERE
        // SNIPPET_START

        Transaction tx = db.beginTx();
        try
        {
            for (Relationship rel : cyberleader.getRelationships())
            {
                rel.delete();
            }
            cyberleader.delete();
            tx.success();
        } finally
        {
            tx.finish();
        }

        // SNIPPET_END

        assertNull("Cyberleader has not been deleted from the characters index.", retriveCyberleaderFromIndex(db));

        try
        {
            db.getNodeById(cyberleader.getId());
            fail("Cyberleader has not been deleted from the database.");
        } catch (NotFoundException nfe)
        {
        }
    }

    private Node retriveCyberleaderFromIndex(GraphDatabaseService db)
    {
        return db.index()
                 .forNodes("characters")
                 .get("character", "Cyberleader")
                 .getSingle();
    }

    private Node createAbigailPettigrew(GraphDatabaseService db)
    {
        Node abigailPettigrew;
        Transaction tx = db.beginTx();
        try
        {
            abigailPettigrew = db.createNode();
            abigailPettigrew.setProperty("character", "Abigail Pettigrew");
            tx.success();
        } finally
        {
            tx.finish();
        }
        return abigailPettigrew;
    }
}
