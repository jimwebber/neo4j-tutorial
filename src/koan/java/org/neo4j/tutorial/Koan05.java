package org.neo4j.tutorial;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.Traverser.Order;

/**
 * In this Koan we start using the simple traversal framework to find
 * interesting information from the graph.
 */
public class Koan05 {

    private DoctorWhoUniverse universe;

    @Before
    public void createADatabase() {

        universe = new DoctorWhoUniverse();
    }

    @Test
    public void shouldFindAllCompanions() {
        Node theDoctor = universe.theDoctor();
        Traverser t = null;

        // SNIPPET_START

        t = theDoctor.traverse(Order.DEPTH_FIRST,
                StopEvaluator.DEPTH_ONE,
                ReturnableEvaluator.ALL_BUT_START_NODE,
                DoctorWhoUniverse.COMPANION_OF,
                Direction.INCOMING);

        // SNIPPET_END

        Collection<Node> foundCompanions = t.getAllNodes();

        int knownNumberOfCompanions = 46;
        assertEquals(knownNumberOfCompanions, foundCompanions.size());
    }
}
