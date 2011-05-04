package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;

/**
 * In this Koan we start using the new traversal framework to find interesting
 * information from the graph about the Doctor's love life.
 */
public class Koan06 {

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
    public void shouldFindTheLoveRivalOfRiver() throws Exception {
//        Node riverSong = universe.characterIndex.get("name", "River Song").getSingle();
//        
//        Traverser traverser = Traversal.description().expand(Traversal.expanderForTypes(DoctorWhoUniverse.LOVES, Direction.BOTH)).depthFirst()..evaluator(new Evaluator() {
//            
//            @Override
//            public Evaluation evaluate(Path path) {
//                // TODO Auto-generated method stub
//                return null;
//            }
//        }).uniqueness(Uniqueness.NODE_GLOBAL).traverse(riverSong);
    }
}
