package org.neo4j.tutorial;

import static org.junit.Assert.assertThat;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificActors.containsOnlyActors;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificSpecies.containsOnlySpecies;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificTitles.containsOnlyTitles;

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
public class Koan10
{

    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator() );
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Test
    public void shouldFindEpisodesWhereTheDoctorFoughtTheCybermen()
    {
        HashSet<Node> cybermenEpisodes = new HashSet<Node>();

        // YOUR CODE GOES HERE
        // SNIPPET_START

        final PatternNode theDoctor = new PatternNode();
        theDoctor.setAssociation( universe.theDoctor() );

        final PatternNode anEpisode = new PatternNode();
        anEpisode.addPropertyConstraint( "title", CommonValueMatchers.has() );
        anEpisode.addPropertyConstraint( "episode", CommonValueMatchers.has() );

        final PatternNode aDoctorActor = new PatternNode();
        aDoctorActor.createRelationshipTo( theDoctor, DoctorWhoRelationships.PLAYED );
        aDoctorActor.createRelationshipTo( anEpisode, DoctorWhoRelationships.APPEARED_IN );
        aDoctorActor.addPropertyConstraint( "actor", CommonValueMatchers.has() );

        final PatternNode theCybermen = new PatternNode();
        theCybermen.setAssociation( universe.getDatabase()
                .index()
                .forNodes( "species" )
                .get( "species", "Cyberman" )
                .getSingle() );
        theCybermen.createRelationshipTo( anEpisode, DoctorWhoRelationships.APPEARED_IN );
        theCybermen.createRelationshipTo( theDoctor, DoctorWhoRelationships.ENEMY_OF );

        PatternMatcher matcher = PatternMatcher.getMatcher();
        final Iterable<PatternMatch> matches = matcher.match( theDoctor, universe.theDoctor() );

        for ( PatternMatch pm : matches )
        {
            cybermenEpisodes.add( pm.getNodeFor( anEpisode ) );
        }

        // SNIPPET_END

        assertThat( cybermenEpisodes, containsOnlyTitles( knownCybermenTitles() ) );
    }

    private String[] knownCybermenTitles()
    {
        return new String[] { "The Moonbase", "The Tomb of the Cybermen", "The Wheel in Space",
                "Revenge of the Cybermen", "Earthshock", "Silver Nemesis", "Rise of the Cybermen", "The Age of Steel",
                "Army of Ghosts", "Doomsday", "The Next Doctor", "The Pandorica Opens", "A Good Man Goes to War", "Closing Time" };
    }

    @Test
    public void shouldFindDoctorsThatBattledTheCybermen()
    {
        HashSet<Node> doctorActors = new HashSet<Node>();
        Node cybermenNode = universe.getDatabase()
                .index()
                .forNodes( "species" )
                .get( "species", "Cyberman" )
                .getSingle();

        // YOUR CODE GOES HERE
        // SNIPPET_START

        final PatternNode theDoctor = new PatternNode();
        theDoctor.setAssociation( universe.theDoctor() );

        final PatternNode anEpisode = new PatternNode();
        anEpisode.addPropertyConstraint( "title", CommonValueMatchers.has() );
        anEpisode.addPropertyConstraint( "episode", CommonValueMatchers.has() );

        final PatternNode aDoctorActor = new PatternNode();
        aDoctorActor.createRelationshipTo( theDoctor, DoctorWhoRelationships.PLAYED );
        aDoctorActor.createRelationshipTo( anEpisode, DoctorWhoRelationships.APPEARED_IN );
        aDoctorActor.addPropertyConstraint( "actor", CommonValueMatchers.has() );

        final PatternNode theCybermen = new PatternNode();
        theCybermen.setAssociation( cybermenNode );
        theCybermen.createRelationshipTo( anEpisode, DoctorWhoRelationships.APPEARED_IN );
        theCybermen.createRelationshipTo( theDoctor, DoctorWhoRelationships.ENEMY_OF );

        PatternMatcher matcher = PatternMatcher.getMatcher();
        final Iterable<PatternMatch> matches = matcher.match( theDoctor, universe.theDoctor() );

        for ( PatternMatch pm : matches )
        {
            doctorActors.add( pm.getNodeFor( aDoctorActor ) );
        }

        // SNIPPET_END

        assertThat(
                doctorActors,
                containsOnlyActors( "David Tennant", "Matt Smith", "Patrick Troughton", "Tom Baker", "Peter Davison",
                        "Sylvester McCoy" ) );
    }

    @Test
    public void shouldFindEnemySpeciesThatRoseTylerAndTheNinthDoctorEncountered()
    {
        HashSet<Node> enemySpeciesRoseAndTheNinthDoctorEncountered = new HashSet<Node>();
        Node ninthDoctorNode = universe.getDatabase()
                .index()
                .forNodes( "actors" )
                .get( "actor", "Christopher Eccleston" )
                .getSingle();
        Node roseTylerNode = universe.getDatabase()
                .index()
                .forNodes( "characters" )
                .get( "character", "Rose Tyler" )
                .getSingle();

        // YOUR CODE GOES HERE
        // SNIPPET_START

        final PatternNode theDoctor = new PatternNode();
        theDoctor.setAssociation( universe.theDoctor() );

        final PatternNode ecclestone = new PatternNode();
        ecclestone.setAssociation( ninthDoctorNode );

        final PatternNode roseTyler = new PatternNode();
        roseTyler.setAssociation( roseTylerNode );

        final PatternNode anEpisode = new PatternNode();
        anEpisode.addPropertyConstraint( "title", CommonValueMatchers.has() );
        anEpisode.addPropertyConstraint( "episode", CommonValueMatchers.has() );

        final PatternNode anEnemySpecies = new PatternNode();
        anEnemySpecies.addPropertyConstraint( "species", CommonValueMatchers.has() );

        ecclestone.createRelationshipTo( anEpisode, DoctorWhoRelationships.APPEARED_IN );
        roseTyler.createRelationshipTo( anEpisode, DoctorWhoRelationships.APPEARED_IN );
        anEnemySpecies.createRelationshipTo( anEpisode, DoctorWhoRelationships.APPEARED_IN );
        anEnemySpecies.createRelationshipTo( theDoctor, DoctorWhoRelationships.ENEMY_OF );

        PatternMatcher matcher = PatternMatcher.getMatcher();
        final Iterable<PatternMatch> matches = matcher.match( theDoctor, universe.theDoctor() );

        for ( PatternMatch pm : matches )
        {
            enemySpeciesRoseAndTheNinthDoctorEncountered.add( pm.getNodeFor( anEnemySpecies ) );
        }

        // SNIPPET_END

        assertThat( enemySpeciesRoseAndTheNinthDoctorEncountered, containsOnlySpecies( "Dalek", "Slitheen", "Auton" ) );
    }
}
