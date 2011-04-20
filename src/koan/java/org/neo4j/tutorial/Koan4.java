package org.neo4j.tutorial;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.IndexHits;

public class Koan4 {
    
    private DoctorWhoUniverse universe;

    @Before
    public void createDatabase() throws Exception {
        universe = new DoctorWhoUniverse();
    }
    
    @Test
    public void shouldFindHumanCompanionsUsingCoreApi() {
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
    }
}
