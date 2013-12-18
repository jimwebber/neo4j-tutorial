package org.neo4j.tutorial;

import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.neo4j.graphdb.Direction.INCOMING;
import static org.neo4j.graphdb.Direction.OUTGOING;
import static org.neo4j.tutorial.DoctorWhoLabels.CHARACTER;
import static org.neo4j.tutorial.DoctorWhoLabels.SPECIES;
import static org.neo4j.tutorial.DoctorWhoRelationships.APPEARED_IN;
import static org.neo4j.tutorial.matchers.ContainsOnlyHumanCompanions.containsOnlyHumanCompanions;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificTitles.containsOnlyTitles;

/**
 * In this Koan we start to mix indexing and core API to perform more targeted
 * graph operations. We'll mix indexes and core graph operations to explore the
 * Doctor's universe.
 */

// TODO: Move this to the core API koans

public class Koan05
{

    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator().getDatabase() );
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Test
    public void shouldCountTheNumberOfDoctorsRegeneratedForms()
    {
        GraphDatabaseService database = universe.getDatabase();

        try ( Transaction tx = database.beginTx() )
        {
            Node doctor = universe.theDoctor();

            int numberOfRegenerations = 0;

            // YOUR CODE GOES HERE
            // SNIPPET_START


            for ( Relationship relationship : doctor.getRelationships( INCOMING,
                    DoctorWhoRelationships.PLAYED ) )
            {
                if ( relationship.getStartNode().hasRelationship( INCOMING,
                        DoctorWhoRelationships.REGENERATED_TO ) )
                {
                    numberOfRegenerations++;
                }
            }

            // SNIPPET_END

            assertEquals( 12, numberOfRegenerations );
            tx.success();
        }
    }

    @Test
    public void shouldFindHumanCompanionsUsingCoreApi()
    {
        Set<Node> humanCompanions = new HashSet<>();

        GraphDatabaseService database = universe.getDatabase();

        try ( Transaction tx = database.beginTx() )
        {

            // YOUR CODE GOES HERE
            // SNIPPET_START

            Node human = database.findNodesByLabelAndProperty( SPECIES, "species",
                    "Human" ).iterator().next();


            for ( Relationship rel : universe.theDoctor()
                    .getRelationships( INCOMING,
                            DoctorWhoRelationships.COMPANION_OF ) )
            {
                Node companionNode = rel.getStartNode();

                if ( companionNode.hasRelationship( OUTGOING, DoctorWhoRelationships.IS_A ) )
                {
                    for ( Relationship companionNodeRelationship : companionNode.getRelationships(
                            DoctorWhoRelationships.IS_A, OUTGOING ) )
                    {
                        Node endNode = companionNodeRelationship.getEndNode();

                        if ( endNode.equals( human ) )
                        {
                            humanCompanions.add( companionNode );
                        }
                    }
                }
            }

            // SNIPPET_END

            int numberOfKnownHumanCompanions = 40;
            assertEquals( numberOfKnownHumanCompanions, humanCompanions.size() );
            assertThat( humanCompanions, containsOnlyHumanCompanions() );
            tx.success();
        }
    }

    @Test
    public void shouldFindAllEpisodesWhereRoseTylerFoughtTheDaleks()
    {
        GraphDatabaseService database = universe.getDatabase();

        try ( Transaction tx = database.beginTx() )
        {
            HashSet<Node> episodesWhereRoseFightsTheDaleks = new HashSet<>();

            // YOUR CODE GOES HERE
            // SNIPPET_START

            Node roseTyler = database.findNodesByLabelAndProperty( CHARACTER, "character",
                    "Rose Tyler" ).iterator().next();
            Node daleks = database.findNodesByLabelAndProperty( SPECIES, "species", "Dalek" ).iterator().next();


            for ( Relationship roseAppearedIn : roseTyler.getRelationships( OUTGOING, APPEARED_IN ) )
            {
                for ( Relationship appearedInEpisode : roseAppearedIn.getEndNode().getRelationships( INCOMING,
                        APPEARED_IN ) )
                {
                    if ( appearedInEpisode.getStartNode().equals( daleks ) )
                    {
                        episodesWhereRoseFightsTheDaleks.add( appearedInEpisode.getEndNode() );
                    }
                }
            }

            // SNIPPET_END

            tx.success();
            assertThat( episodesWhereRoseFightsTheDaleks,
                    containsOnlyTitles( universe.getDatabase(), "Army of Ghosts", "The Stolen Earth", "Doomsday",
                            "Journey's End", "Bad Wolf",
                            "The Parting of the Ways", "Dalek" ) );
        }
    }
}
