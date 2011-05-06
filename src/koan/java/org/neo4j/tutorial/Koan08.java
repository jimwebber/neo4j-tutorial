package org.neo4j.tutorial;

import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphmatching.CommonValueMatchers;
import org.neo4j.graphmatching.PatternMatch;
import org.neo4j.graphmatching.PatternMatcher;
import org.neo4j.graphmatching.PatternNode;
import org.neo4j.graphmatching.ValueMatcher;
import org.neo4j.helpers.collection.IterableWrapper;

/**
 * In this Koan we use the graph-matching library to look for patterns in the
 * Doctor's universe.
 */
public class Koan08 {

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
    public void shouldFindDoctorsThatFoughtMoreThanOneEnemyConcurrently() {
        final PatternNode theDoctor = new PatternNode();
        theDoctor.setAssociation(universe.theDoctor());

        final PatternNode anEpisode = new PatternNode();
        anEpisode.addPropertyConstraint("title", CommonValueMatchers.has());
        anEpisode.addPropertyConstraint("episode", CommonValueMatchers.has());

        int numberOfConcurrentEnemies = 2;
        for (int i = 0; i < numberOfConcurrentEnemies; i++) {
            final PatternNode enemy = new PatternNode();

            theDoctor.createRelationshipTo(enemy, DoctorWhoUniverse.ENEMY_OF, Direction.OUTGOING);
            enemy.createRelationshipTo(anEpisode, DoctorWhoUniverse.APPEARED_IN, Direction.OUTGOING);
        }

        PatternMatcher matcher = PatternMatcher.getMatcher();
        final Iterable<PatternMatch> matches = matcher.match(theDoctor, universe.theDoctor());

        IterableWrapper<Node, PatternMatch> nodes = new IterableWrapper<Node, PatternMatch>(matches) {
            @Override
            protected Node underlyingObjectToObject(PatternMatch match) {
                return match.getNodeFor(anEpisode);
            }
        };

        for (Node n : nodes) {
            new DatabaseHelper(universe.getDatabase()).dumpNode(n);
        }

        // Set<Node> myResultNodes = new HashSet<Node>();

        // for (Node n : nodes) {
        // myResultNodes.add(n);
        // }
        //
        // for (Node n : myResultNodes) {
        // new DatabaseHelper(universe.getDatabase()).dumpNode(n);
        // }
    }
}
