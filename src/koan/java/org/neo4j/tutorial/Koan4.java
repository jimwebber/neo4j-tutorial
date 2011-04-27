package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.neo4j.tutorial.matchers.ContainsOnlyHumanCompanions.containsOnlyHumanCompanions;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.IndexHits;

/**
 * In this Koan we start to mix indexing and core API to perform more targeted
 * graph operations. We'll mix indexes and core graph operations to find out
 * just how many companions the Doctor has taken over the years.
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
        IndexHits<Node> companions = null;

        // SNIPPET_START

        companions = universe.getDatabase().index().forNodes("companions").query("name", "*");

        // SNIPPET_END

        HashSet<Node> humanCompanions = new HashSet<Node>();

        // SNIPPET_START

        for (Node n : companions) {

            if (n.hasRelationship(DoctorWhoUniverse.IS_A, Direction.OUTGOING)) {
                Relationship relationship = n.getSingleRelationship(DoctorWhoUniverse.IS_A, Direction.OUTGOING);
                if (relationship.getEndNode().getProperty("species").equals("Human")) {
                    humanCompanions.add(n);
                }
            }
        }

        // SNIPPET_END

        int numberOfKnownHumanCompanions = 35;
        assertEquals(numberOfKnownHumanCompanions, humanCompanions.size());
        assertThat(humanCompanions, containsOnlyHumanCompanions());
    }
}
