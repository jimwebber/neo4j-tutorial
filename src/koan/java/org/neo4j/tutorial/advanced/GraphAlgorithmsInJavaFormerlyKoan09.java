package org.neo4j.tutorial.advanced;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.*;
import org.neo4j.kernel.Traversal;
import org.neo4j.tutorial.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import static org.neo4j.graphdb.Direction.BOTH;
import static org.neo4j.graphdb.Direction.OUTGOING;
import static org.neo4j.tutorial.DoctorWhoLabels.ACTOR;
import static org.neo4j.tutorial.DoctorWhoLabels.EPISODE;
import static org.neo4j.tutorial.DoctorWhoRelationships.APPEARED_IN;
import static org.neo4j.tutorial.DoctorWhoRelationships.REGENERATED_TO;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificNodes.containsOnlySpecificNodes;
import static org.neo4j.tutorial.matchers.PathsMatcher.consistPreciselyOf;

/**
 * In this Koan we use some of the pre-canned graph algorithms that come with
 * Neo4j to gain more insight into the Doctor's universe.
 */
public class GraphAlgorithmsInJavaFormerlyKoan09
{
    @ClassRule
    static public DoctorWhoUniverseResource neo4jResource = new DoctorWhoUniverseResource();

    @Test
    public void shouldRevealTheEpisodesWhereRoseTylerFoughtTheDaleks()
    {
        GraphDatabaseService database = neo4jResource.getGraphDatabaseService();

        try ( Transaction tx = database.beginTx() )
        {
            Node rose = database.findNode(DoctorWhoLabels.CHARACTER, "character", "Rose Tyler");
            Node daleks = database.findNode(DoctorWhoLabels.SPECIES, "species", "Dalek");
            Iterable<Path> paths = null;

            // YOUR CODE GOES HERE
            // SNIPPET_START

            PathFinder<Path> pathFinder = GraphAlgoFactory.pathsWithLength(
                    PathExpanders.forType(APPEARED_IN) , 2 );
            paths = pathFinder.findAllPaths( rose, daleks );

            // SNIPPET_END

            tx.success();

            assertThat( paths, consistPreciselyOf( rose, knownRoseVersusDaleksEpisodes(), daleks ) );
        }
    }

    private HashSet<Node> knownRoseVersusDaleksEpisodes()
    {
        List<String> roseVersusDaleksEpisodeTitles = Arrays.asList( "Dalek", "Army of Ghosts", "Doomsday",
                "The Parting of the Ways", "The Stolen Earth",
                "Bad Wolf", "Journey's End" );
        HashSet<Node> roseVersusDaleksEpisodes = new HashSet<>();
        for ( String title : roseVersusDaleksEpisodeTitles )
        {
            roseVersusDaleksEpisodes.add( neo4jResource.getGraphDatabaseService().findNodesByLabelAndProperty(
                    EPISODE, "title", title ).iterator().next() );
        }
        return roseVersusDaleksEpisodes;
    }

    @Test
    public void shouldFindTheNumberOfMasterRegenerationsTheEasyWay()
    {
        GraphDatabaseService database = neo4jResource.getGraphDatabaseService();

        try ( Transaction tx = database.beginTx() )
        {
            Node delgado = database.findNode(ACTOR, "actor", "Roger Delgado");
            Node simm = database.findNode(ACTOR, "actor", "John Simm");
            Path path = null;

            // YOUR CODE GOES HERE
            // SNIPPET_START

            PathFinder<Path> pathFinder = GraphAlgoFactory.shortestPath(
                    PathExpanders.forTypeAndDirection(REGENERATED_TO, OUTGOING), 100);
            path = pathFinder.findSinglePath( delgado, simm );

            // SNIPPET_END

            tx.success();

            assertNotNull( path );
            int numberOfMasterRegenerations = 8;
            int numberOfActorsFound = path.length() + 1;
            assertEquals( numberOfMasterRegenerations, numberOfActorsFound );
        }
    }

    @Test
    public void shouldFindEpisodesWhereMattSmithAndDavidTennantAppeared()
    {
        GraphDatabaseService database = neo4jResource.getGraphDatabaseService();
        try ( Transaction tx = database.beginTx() )
        {
            Node tennant = database.findNode(ACTOR, "actor", "David Tennant");
            Node smith = database.findNode( ACTOR, "actor", "Matt Smith" );
            Iterable<Path> path = null;

            // YOUR CODE GOES HERE
            // SNIPPET_START

            PathFinder<Path> pathFinder = GraphAlgoFactory.pathsWithLength(
                    PathExpanders.forType(APPEARED_IN), 2 );

            path = pathFinder.findAllPaths( tennant, smith );

            System.out.println( path );

            // SNIPPET_END

            tx.success();

            assertNotNull(path);
            Node endOfTime = database.findNode(EPISODE, "title", "The End of Time");

            Node dayOfTheDoctor = database.findNode(EPISODE, "title", "The Day of the Doctor");

            assertThat( path, containsOnlySpecificNodes( tennant, smith, dayOfTheDoctor, endOfTime ) );
        }
    }
}
