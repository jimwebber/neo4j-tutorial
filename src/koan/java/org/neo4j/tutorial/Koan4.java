package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.IndexHits;

/**
 * In this Koan we start to mix indexing and core API to perform more targeted
 * graph operations.
 * 
 */
public class Koan4 {

    private DoctorWhoUniverse universe;

    @Before
    public void createADatabase() {
        
        universe = new DoctorWhoUniverse();
    }

    @Test
    public void givenTheCompanionsIndexShouldFindHumanCompanionsUsingCoreApi() {
        IndexHits<Node> companions = universe.getDatabase().index().forNodes("companions").query("name", "*");
        
        HashSet<Node> humanCompanions = new HashSet<Node>();

        for (Node n : companions) {
            
            
            if (n.hasRelationship(DoctorWhoUniverse.IS_A, Direction.OUTGOING)) {
                Relationship relationship = n.getSingleRelationship(DoctorWhoUniverse.IS_A, Direction.OUTGOING);
                if (relationship.getEndNode().getProperty("species").equals("Human")) {
                    humanCompanions.add(n);
                }
            }
        }
        
        int numberOfKnownHumanCompanions = 35;
        assertEquals(numberOfKnownHumanCompanions, humanCompanions.size());
    }
}
