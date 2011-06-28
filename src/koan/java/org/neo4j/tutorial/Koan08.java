package org.neo4j.tutorial;

import static org.junit.Assert.assertThat;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificSpecies.containsOnlySpecies;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificTitles.containsOnlyTitles;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificActors.containsOnlyActors;

import java.util.HashSet;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphmatching.CommonValueMatchers;
import org.neo4j.graphmatching.PatternMatch;
import org.neo4j.graphmatching.PatternMatcher;
import org.neo4j.graphmatching.PatternNode;

/**
 * In this Koan we use the graph-matching library to look for patterns in the
 * Doctor's universe.
 */
public class Koan08 {

    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception {
        universe = new EmbeddedDoctorWhoUniverse();
    }

    @AfterClass
    public static void closeTheDatabase() {
        universe.stop();    
    }

    @Test
    public void shouldFindEpisodesWhereTheDoctorFoughtTheCybermen() {
        HashSet<Node> cybermenEpisodes = new HashSet<Node>();

        // YOUR CODE GOES HERE
        // SNIPPET_START

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
        theCybermen.setAssociation(universe.getDatabase().index().forNodes("species").get("species", "Cyberman").getSingle());
        theCybermen.createRelationshipTo(anEpisode, DoctorWhoUniverse.APPEARED_IN);
        theCybermen.createRelationshipTo(theDoctor, DoctorWhoUniverse.ENEMY_OF);

        PatternMatcher matcher = PatternMatcher.getMatcher();
        final Iterable<PatternMatch> matches = matcher.match(theDoctor, universe.theDoctor());

        for (PatternMatch pm : matches) {
            cybermenEpisodes.add(pm.getNodeFor(anEpisode));
        }

        // SNIPPET_END

        assertThat(cybermenEpisodes, containsOnlyTitles(knownCybermenTitles()));
    }

    private String[] knownCybermenTitles() {
        return new String[] { "The Moonbase", "The Tomb of the Cybermen", "The Wheel in Space", "Revenge of the Cybermen", "Earthshock", "Silver Nemesis",
                "Rise of the Cybermen", "The Age of Steel", "Army of Ghosts", "Doomsday", "The Next Doctor", "The Pandorica Opens" };
    }
    
    @Test
    public void shouldFindDoctorsThatBattledTheCybermen() {
        HashSet<Node> cybermenEpisodes = new HashSet<Node>();

        // YOUR CODE GOES HERE
        // SNIPPET_START

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
        theCybermen.setAssociation(universe.getDatabase().index().forNodes("species").get("species", "Cyberman").getSingle());
        theCybermen.createRelationshipTo(anEpisode, DoctorWhoUniverse.APPEARED_IN);
        theCybermen.createRelationshipTo(theDoctor, DoctorWhoUniverse.ENEMY_OF);

        PatternMatcher matcher = PatternMatcher.getMatcher();
        final Iterable<PatternMatch> matches = matcher.match(theDoctor, universe.theDoctor());

        for (PatternMatch pm : matches) {
            cybermenEpisodes.add(pm.getNodeFor(aDoctorActor));
        }

        // SNIPPET_END

        assertThat(cybermenEpisodes, containsOnlyActors("David Tennant", "Matt Smith", "Patrick Troughton", "Tom Baker", "Peter Davison", "Sylvester McCoy"));
    }

    @Test
    public void shouldFindEnemySpeciesThatRoseTylerAndTheNinthDoctorEncountered() {
        HashSet<Node> enemySpeciesRoseAndTheNinthDoctorEncountered = new HashSet<Node>();

        // YOUR CODE GOES HERE
        // SNIPPET_START

        final PatternNode theDoctor = new PatternNode();
        theDoctor.setAssociation(universe.theDoctor());

        final PatternNode ecclestone = new PatternNode();
        ecclestone.setAssociation(universe.getDatabase().index().forNodes("actors").get("actor", "Christopher Eccleston").getSingle());

        final PatternNode roseTyler = new PatternNode();
        roseTyler.setAssociation(universe.getDatabase().index().forNodes("characters").get("name", "Rose Tyler").getSingle());

        final PatternNode anEpisode = new PatternNode();
        anEpisode.addPropertyConstraint("title", CommonValueMatchers.has());
        anEpisode.addPropertyConstraint("episode", CommonValueMatchers.has());

        final PatternNode anEnemySpecies = new PatternNode();
        anEnemySpecies.addPropertyConstraint("species", CommonValueMatchers.has());

        ecclestone.createRelationshipTo(anEpisode, DoctorWhoUniverse.APPEARED_IN);
        roseTyler.createRelationshipTo(anEpisode, DoctorWhoUniverse.APPEARED_IN);
        anEnemySpecies.createRelationshipTo(anEpisode, DoctorWhoUniverse.APPEARED_IN);
        anEnemySpecies.createRelationshipTo(theDoctor, DoctorWhoUniverse.ENEMY_OF);

        PatternMatcher matcher = PatternMatcher.getMatcher();
        final Iterable<PatternMatch> matches = matcher.match(theDoctor, universe.theDoctor());

        for (PatternMatch pm : matches) {
            enemySpeciesRoseAndTheNinthDoctorEncountered.add(pm.getNodeFor(anEnemySpecies));
        }

        // SNIPPET_END

        assertThat(enemySpeciesRoseAndTheNinthDoctorEncountered, containsOnlySpecies("Dalek", "Slitheen", "Auton"));
    }
}
