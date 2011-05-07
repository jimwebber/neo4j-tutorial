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
import org.neo4j.graphmatching.PatternGroup;
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
    public void shouldFindEpisodesWhereTennantPlayedTheDoctor() {
        final PatternNode tennant = new PatternNode();
        Node realTennant = universe.actorIndex.get("actor", "David Tennant").getSingle();
        tennant.setAssociation(realTennant);

        final PatternNode anEpisode = new PatternNode();
        anEpisode.addPropertyConstraint("title", CommonValueMatchers.has());
        anEpisode.addPropertyConstraint("episode", CommonValueMatchers.has());

        tennant.createRelationshipTo(anEpisode, DoctorWhoUniverse.APPEARED_IN);

        PatternMatcher matcher = PatternMatcher.getMatcher();
        final Iterable<PatternMatch> matches = matcher.match(tennant, realTennant);

        IterableWrapper<Node, PatternMatch> nodes = new IterableWrapper<Node, PatternMatch>(matches) {
            @Override
            protected Node underlyingObjectToObject(PatternMatch match) {
                return match.getNodeFor(anEpisode);
            }
        };

        for (Node n : nodes) {
            new DatabaseHelper(universe.getDatabase()).dumpNode(n);
        }

    }

    @Test
    public void shouldFindEpisodesWhereTheDoctorFoughtTheCybermen() {
        final PatternNode theDoctor = new PatternNode();
        theDoctor.setAssociation(universe.theDoctor());

        final PatternNode anEpisode = new PatternNode();
        anEpisode.addPropertyConstraint("title", CommonValueMatchers.has());
        anEpisode.addPropertyConstraint("episode", CommonValueMatchers.has());

        final PatternNode aDoctorActor = new PatternNode();
        aDoctorActor.createRelationshipTo(theDoctor, DoctorWhoUniverse.PLAYED);
        aDoctorActor.createRelationshipTo(anEpisode, DoctorWhoUniverse.APPEARED_IN);
        aDoctorActor.addPropertyConstraint("actor", CommonValueMatchers.has());

        final PatternNode theCybermen = new PatternNode();
        theCybermen.setAssociation(universe.speciesIndex.get("species", "Cyberman").getSingle());
        theCybermen.createRelationshipTo(anEpisode, DoctorWhoUniverse.APPEARED_IN);
        theCybermen.createRelationshipTo(theDoctor, DoctorWhoUniverse.ENEMY_OF);

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
    }

    @Test
    public void shouldFindEpisodesWithMultipleEnemySpecies() {
        
        final PatternNode theDoctor = new PatternNode();
        theDoctor.setAssociation(universe.theDoctor());

        final PatternNode anEpisode = new PatternNode();
        anEpisode.addPropertyConstraint("title", CommonValueMatchers.has());
        anEpisode.addPropertyConstraint("episode", CommonValueMatchers.has());

        final PatternNode aDoctorActor = new PatternNode();
        aDoctorActor.createRelationshipTo(theDoctor, DoctorWhoUniverse.PLAYED);
        aDoctorActor.createRelationshipTo(anEpisode, DoctorWhoUniverse.APPEARED_IN);
        aDoctorActor.addPropertyConstraint("actor", CommonValueMatchers.has());

        final PatternNode anEnemy = new PatternNode();
        anEnemy.createRelationshipTo(anEpisode, DoctorWhoUniverse.APPEARED_IN);
        anEnemy.createRelationshipTo(theDoctor, DoctorWhoUniverse.ENEMY_OF);
        anEnemy.setAssociation(universe.speciesIndex.get("species", "Cyberman").getSingle());
        
        final PatternNode aSecondEnemy = new PatternNode();
        aSecondEnemy.createRelationshipTo(anEpisode, DoctorWhoUniverse.APPEARED_IN);
        aSecondEnemy.createRelationshipTo(theDoctor, DoctorWhoUniverse.ENEMY_OF);
        aSecondEnemy.setAssociation(universe.speciesIndex.get("species", "Dalek").getSingle());

        
       
        


        PatternMatcher matcher = PatternMatcher.getMatcher();
        final Iterable<PatternMatch> matches = matcher.match(theDoctor, universe.theDoctor());
        
        
        
        for(PatternMatch pm : matches) {
            
            new DatabaseHelper(universe.getDatabase()).dumpNode(pm.getNodeFor(anEpisode));
        }
        
       
        
        

//        IterableWrapper<Node, PatternMatch> nodes = new IterableWrapper<Node, PatternMatch>(matches) {
//            @Override
//            protected Node underlyingObjectToObject(PatternMatch match) {
//                return match.getNodeFor(anEpisode);
//            }
//        };

        
        
//        for (Node n : nodes) {
//            new DatabaseHelper(universe.getDatabase()).dumpNode(n);
//        }
    }
}
