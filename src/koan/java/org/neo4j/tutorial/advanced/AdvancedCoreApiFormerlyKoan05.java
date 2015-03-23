package org.neo4j.tutorial.advanced;

import java.util.HashSet;
import java.util.Set;

import org.junit.ClassRule;
import org.junit.Test;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.tutorial.DoctorWhoRelationships;
import org.neo4j.tutorial.DoctorWhoUniverseResource;

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

public class AdvancedCoreApiFormerlyKoan05
{
    @ClassRule
    static public DoctorWhoUniverseResource neo4jResource = new DoctorWhoUniverseResource();

    @Test
    public void shouldCountTheNumberOfDoctorsRegeneratedForms()
    {
        GraphDatabaseService database = neo4jResource.getGraphDatabaseService();

        try ( Transaction tx = database.beginTx() )
        {
            Node doctor = neo4jResource.theDoctor();

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

        GraphDatabaseService database = neo4jResource.getGraphDatabaseService();

        try ( Transaction tx = database.beginTx() )
        {

            // YOUR CODE GOES HERE
            // SNIPPET_START

            Node human = database.findNode(SPECIES, "species", "Human");


            for ( Relationship rel : neo4jResource.theDoctor()
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
        GraphDatabaseService database = neo4jResource.getGraphDatabaseService();

        try ( Transaction tx = database.beginTx() )
        {
            HashSet<Node> episodesWhereRoseFightsTheDaleks = new HashSet<>();

            // YOUR CODE GOES HERE
            // SNIPPET_START

            Node roseTyler = database.findNode(CHARACTER, "character", "Rose Tyler");
            Node daleks = database.findNode( SPECIES, "species", "Dalek" );


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
                    containsOnlyTitles( neo4jResource.getGraphDatabaseService(), "Army of Ghosts", "The Stolen Earth", "Doomsday",
                            "Journey's End", "Bad Wolf",
                            "The Parting of the Ways", "Dalek" ) );
        }
    }
}
