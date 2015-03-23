package org.neo4j.tutorial.advanced;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Test;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.TraversalPosition;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.Traverser.Order;
import org.neo4j.tutorial.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * In this Koan we start using the simple traversal framework to find
 * interesting information from the graph.
 */
// TODO: consider deleting this, the API is deprecated anyway.
public class OldTraversalFrameworkFormerlyKoan06
{
    @ClassRule
    static public DoctorWhoUniverseResource neo4jResource = new DoctorWhoUniverseResource();

    @Test
    public void shouldFindAllCompanions()
    {
        Node theDoctor = neo4jResource.theDoctor();
        Traverser t = null;

        GraphDatabaseService database = neo4jResource.getGraphDatabaseService();

        try ( Transaction tx = database.beginTx() )
        {

            // YOUR CODE GOES HERE
            // SNIPPET_START

            t = theDoctor.traverse( Order.DEPTH_FIRST, StopEvaluator.DEPTH_ONE, ReturnableEvaluator.ALL_BUT_START_NODE,
                    DoctorWhoRelationships.COMPANION_OF, Direction.INCOMING );

            // SNIPPET_END

            Collection<Node> foundCompanions = t.getAllNodes();

            int knownNumberOfCompanions = 47;
            assertEquals( knownNumberOfCompanions, foundCompanions.size() );
            tx.success();
        }
    }

    @Test
    public void shouldFindAllDalekProps()
    {
        GraphDatabaseService database = neo4jResource.getGraphDatabaseService();

        try ( Transaction tx = database.beginTx() )
        {
            Node theDaleks = database.findNodesByLabelAndProperty( DoctorWhoLabels.SPECIES, "species",
                    "Dalek" ).iterator().next();

            Traverser t = null;

            // YOUR CODE GOES HERE
            // SNIPPET_START

            t = theDaleks.traverse( Order.DEPTH_FIRST, StopEvaluator.END_OF_GRAPH, new ReturnableEvaluator()
            {
                public boolean isReturnableNode( TraversalPosition currentPos )
                {
                    return currentPos.currentNode()
                            .hasLabel( DoctorWhoLabels.PROP );
                }
            }, DoctorWhoRelationships.APPEARED_IN, Direction.BOTH, DoctorWhoRelationships.USED_IN, Direction.INCOMING,
                    DoctorWhoRelationships.MEMBER_OF, Direction.INCOMING );

            // SNIPPET_END

            assertCollectionContainsAllDalekProps( t.getAllNodes() );
            tx.success();
        }
    }

    private void assertCollectionContainsAllDalekProps( Collection<Node> nodes )
    {
        String[] dalekProps = new String[]{"Dalek One-7", "Imperial 4", "Imperial 3", "Imperial 2", "Imperial 1",
                "Supreme Dalek", "Remembrance 3", "Remembrance 2", "Remembrance 1", "Dalek V-VI", "Goon IV", "Goon II",
                "Goon I", "Dalek Six-5", "Dalek Seven-2", "Dalek V-5", "Dalek Seven-V", "Dalek Six-Ex",
                "Dalek Seven-8", "Dalek 8", "Dalek 7", "Dalek Five-6", "Dalek Two-1", "Dalek 2", "Dalek 1", "Dalek 6",
                "Dalek 5", "Dalek 4", "Dalek 3", "Dalek IV-Ex", "Dalek Seven-II", "Necros 3", "Necros 2", "Necros 1",
                "Goon III", "Goon VII", "Goon VI", "Goon V", "Gold Movie Dalek", "Dalek Six-7", "Dalek One-5"};

        List<String> propList = new ArrayList<>();
        for ( Node n : nodes )
        {
            propList.add( n.getProperty( "prop" ).toString() );
        }

        assertEquals( dalekProps.length, propList.size() );
        for ( String prop : dalekProps )
        {
            assertTrue( propList.contains( prop ) );
        }
    }

    @Test
    public void shouldFindAllTheEpisodesTheMasterAndDavidTennantWereInTogether()
    {
        GraphDatabaseService database = neo4jResource.getGraphDatabaseService();

        try ( Transaction tx = database.beginTx() )
        {
            Node theMaster = database.findNode(DoctorWhoLabels.CHARACTER, "character", "Master");
            Traverser t = null;


            // YOUR CODE GOES HERE
            // SNIPPET_START

            t = theMaster.traverse( Order.DEPTH_FIRST, StopEvaluator.END_OF_GRAPH, new ReturnableEvaluator()
            {
                public boolean isReturnableNode( TraversalPosition currentPos )
                {
                    if ( currentPos.currentNode().hasLabel( DoctorWhoLabels.EPISODE ) )
                    {
                        Node episode = currentPos.currentNode();

                        for ( Relationship r : episode.getRelationships( DoctorWhoRelationships.APPEARED_IN,
                                Direction.INCOMING ) )
                        {
                            if ( r.getStartNode().hasLabel( DoctorWhoLabels.ACTOR ) &&
                                    r.getStartNode().getProperty( "actor" )
                                            .equals( "David Tennant" ) )
                            {
                                return true;
                            }
                        }
                    }

                    return false;
                }
            }, DoctorWhoRelationships.APPEARED_IN, Direction.OUTGOING );

            // SNIPPET_END

            int numberOfEpisodesWithTennantVersusTheMaster = 4;
            assertEquals( numberOfEpisodesWithTennantVersusTheMaster, t.getAllNodes()
                    .size() );
            tx.success();
        }
    }
}
