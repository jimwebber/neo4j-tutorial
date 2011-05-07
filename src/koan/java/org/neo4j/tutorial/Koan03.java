package org.neo4j.tutorial;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificSpecies.containsOnlySpecies;
import static org.neo4j.tutorial.matchers.ContainsSpecificCompanions.contains;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

/**
 * This Koan will introduce indexing based on the built-in index framework based
 * on Lucene. It'll give you a feeling for the wealth of bad guys the Doctor has
 * faced.
 */
public class Koan03 {

    private static DoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception {
        universe = new DoctorWhoUniverse();
    }
    
    @AfterClass
    public static void closeTheDatabase() {
        universe.stop();
    }

    @Test
    public void shouldRetrieveCharactersIndexFromTheDatabase() {
        Index<Node> characters = null;

        // SNIPPET_START

        characters = universe.getDatabase().index().forNodes("characters");

        // SNIPPET_END

        assertNotNull(characters);
        assertThat(characters, contains("Master", "River Song", "Rose Tyler", "Adam Mitchell", "Jack Harkness", "Mickey Smith", "Donna Noble", "Martha Jones"));
    }

    @Test
    public void addingToAnIndexShouldBeHandledAsAMutatingOperation() {
        Node abigailPettigrew = createNewCharacterNode("Abigail Pettigrew");

        GraphDatabaseService db = universe.getDatabase();
        // SNIPPET_START

        Transaction tx = db.beginTx();
        try {
            db.index().forNodes("characters").add(abigailPettigrew, "name", abigailPettigrew.getProperty("name"));
            tx.success();
        } finally {
            tx.finish();
        }

        // SNIPPET_END

        assertNotNull(db.index().forNodes("characters").get("name", "Abigail Pettigrew").getSingle());
    }

    @Test
    public void shouldFindSpeciesBeginningWithTheLetterSAndEndingWithTheLetterNUsingLuceneQuery() throws Exception {
        IndexHits<Node> species = null;

        // SNIPPET_START

        species = universe.getDatabase().index().forNodes("species").query("species", "S*n");

        // SNIPPET_END

        assertThat(species, containsOnlySpecies("Silurian", "Slitheen", "Sontaran", "Skarasen"));
    }

    @Test
    public void shouldKeepDatabaseAndIndexInSyncWhenCyberleaderIsDeleted() throws Exception {
        GraphDatabaseService db = universe.getDatabase();

        Index<Node> enemies = db.index().forNodes("characters");
        Node cyberleader = enemies.get("name", "Cyberleader").getSingle();

        // SNIPPET_START

        Transaction tx = db.beginTx();
        try {
            enemies.remove(cyberleader);
            for (Relationship rel : cyberleader.getRelationships()) {
                rel.delete();
            }
            cyberleader.delete();
            tx.success();
        } finally {
            tx.finish();
        }

        // SNIPPET_END

        assertNull("Cyberleader has not been deleted from the characters index.", enemies.get("name", "Cyberleader").getSingle());

        try {
            db.getNodeById(cyberleader.getId());
            fail("Cyberleader has not been deleted from the database.");
        } catch (NotFoundException nfe) {
        }
    }

    private Node createNewCharacterNode(String characterName) {
        Node character = null;
        GraphDatabaseService db = universe.getDatabase();
        Transaction tx = db.beginTx();
        try {
            character = db.createNode();
            character.setProperty("name", characterName);
            tx.success();
        } finally {
            tx.finish();
        }

        return character;
    }
}
