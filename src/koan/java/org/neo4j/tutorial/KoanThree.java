package org.neo4j.tutorial;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.tutorial.DoctorWhoUniverse;

/**
 * This Koan will introduce indexing based on the builtin index framework based
 * on Lucene. It'll give you a feeling for the wealth of bad guys the Doctor has
 * faced.
 */
public class KoanThree {

    private DoctorWhoUniverse universe;

    @Before
    public void createDatabase() throws Exception {
        universe = new DoctorWhoUniverse();
    }

    @Test
    public void shouldCreateAnIndexOfHumanCompanions() {
        HashSet<Node> humanCompanions = getHumanCompanions();
        
        System.out.println(humanCompanions.size());
        
        Index<Node> humanCompanionsIndex = null;
        
        // SNIPPET_START
        
        humanCompanionsIndex = universe.getDatabase().index().forNodes("human-companions");
        
        for(Node humanCompanion : humanCompanions) {
            humanCompanions.add(humanCompanion);
        }

        // SNIPPET_END
        
        assertEquals("human-companions", humanCompanionsIndex.getName());
        assertTrue(containsAllHumanCompanions(humanCompanionsIndex));
    }

    private boolean containsAllHumanCompanions(Index<Node> humanCompanionsIndex) {
        HashSet<Node> humanCompanions = getHumanCompanions();
        for(Node n : humanCompanions) {
            System.out.println(humanCompanionsIndex.get("name", n.getProperty("name")));
            if(!n.equals(humanCompanionsIndex.get("name", n.getProperty("name")))) {
                return false;
            }
        }
        
        return true;
    }

    private HashSet<Node> getHumanCompanions() {
        IndexHits<Node> indexHits = universe.speciesIndex.get("species", "Human");
        HashSet<Node> humanCompanions = new HashSet<Node>();

        for (Node n : indexHits) {
            if (n.getProperty("species").equals("Human")) {
                
                if (n.hasRelationship(DoctorWhoUniverse.COMPANION_OF, Direction.OUTGOING)) {
                    Relationship relationship = n.getSingleRelationship(DoctorWhoUniverse.COMPANION_OF, Direction.OUTGOING);
                    if (relationship.getEndNode().getProperty("name").equals("Doctor")) {
                        humanCompanions.add(n);
                    }
                }
            }
        }
        return humanCompanions;
    }

    @Test
    public void shouldFindSontaransSlitheenAndSiluriansAsSpeciesBeginningWithTheLetterSAndEndingWithTheLetterN() throws Exception {
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
}
