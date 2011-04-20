package org.neo4j.tutorial;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

/**
 * This Koan will introduce indexing based on the builtin index framework based
 * on Lucene. It'll give you a feeling for the wealth of bad guys the Doctor has
 * faced.
 */
public class Koan3 {

    private DoctorWhoUniverse universe;

    @Before
    public void createDatabase() throws Exception {
        universe = new DoctorWhoUniverse();
    }

    @Test
    public void shouldRetrieveCompanionsIndexFromTheDatabase() {
        Index<Node> companions = null;

        // SNIPPET_START

        companions = universe.getDatabase().index().forNodes("companions");

        // SNIPPET_END

        assertNotNull(companions);
        assertTrue(indexContains(companions, "Rose Tyler", "Adam Mitchell", "Jack Harkness", "Mickey Smith", "Donna Noble", "Martha Jones"));
    }

    private boolean indexContains(Index<Node> companions, String... names) {
        for (String name : names) {
            if(companions.get("name", name).getSingle() == null) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void shouldKeepDatabaseAndIndexInSync() throws Exception {

    }

    @Test
    public void addingToAnIndexShouldBeHandledAsAMutatingOperation() {
        Node nixon = createNewCharacterNode("Richard Nixon");

        GraphDatabaseService db = universe.getDatabase();
        // SNIPPET_START

        Transaction tx = db.beginTx();
        try {
            db.index().forNodes("characters").add(nixon, "name", nixon.getProperty("name"));
            tx.success();
        } finally {
            tx.finish();
        }

        // SNIPPET_END

        assertNotNull(db.index().forNodes("characters").get("name", "Richard Nixon").getSingle());
    }

    @Test
    public void shouldFindSpeciesBeginningWithTheLetterSAndEndingWithTheLetterNUsingLuceneQuery() throws Exception {
        IndexHits<Node> indexHits = null;

        // SNIPPET_START

        indexHits = universe.getDatabase().index().forNodes("species").query("species", "S*n");

        // SNIPPET_END

        assertTrue(containsOnlySontaranSlitheenAndSilurian(indexHits));
    }

    private boolean containsOnlySontaranSlitheenAndSilurian(IndexHits<Node> indexHits) {
        boolean foundSilurian = false;
        boolean foundSlitheen = false;
        boolean foundSontaran = false;

        for (Node n : indexHits) {
            String property = (String) n.getProperty("species");

            if (property.equals("Silurian")) {
                foundSilurian = true;
            }
            if (property.equals("Sontaran")) {
                foundSontaran = true;
            }
            if (property.equals("Slitheen")) {
                foundSlitheen = true;
            }

            if (foundSilurian && foundSontaran && foundSlitheen) {
                return true;
            }
        }

        return false;
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
