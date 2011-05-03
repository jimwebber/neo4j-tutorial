package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.TraversalPosition;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.Traverser.Order;

/**
 * In this Koan we start using the simple traversal framework to find
 * interesting information from the graph.
 */
public class Koan05 {

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
    
    @Test
    public void shouldFindAllTheEpisodesTheMasterAndDavidTennantWereInTogether() {
        Node theMaster = universe.characterIndex.get("name", "Master").getSingle();
        Traverser t = null;

        // SNIPPET_START

        t = theMaster.traverse(Order.DEPTH_FIRST,
                StopEvaluator.END_OF_GRAPH,
                new ReturnableEvaluator() {
                    @Override
                    public boolean isReturnableNode(TraversalPosition currentPos) {
                        if(currentPos.currentNode().hasProperty("episode")) {
                            Node episode = currentPos.currentNode();
                            
                            for(Relationship r : episode.getRelationships(DoctorWhoUniverse.APPEARED_IN, Direction.INCOMING)) {
                                if(r.getStartNode().hasProperty("lastname") && r.getStartNode().getProperty("lastname").equals("Tennant")) {
                                    return true;
                                }
                            }
                        }
                        
                        return false;
                    }},
                DoctorWhoUniverse.APPEARED_IN,
                Direction.OUTGOING);

        // SNIPPET_END

        int numberOfEpisodesWithTennantVersusTheMaster = 4;
        assertEquals(numberOfEpisodesWithTennantVersusTheMaster, t.getAllNodes().size());
    }
}
