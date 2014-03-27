package org.neo4j.tutorial.advanced;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.Traversal;
import org.neo4j.tutorial.DoctorWhoLabels;
import org.neo4j.tutorial.DoctorWhoRelationships;
import org.neo4j.tutorial.DoctorWhoUniverseGenerator;
import org.neo4j.tutorial.EmbeddedDoctorWhoUniverse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import static org.neo4j.tutorial.matchers.ContainsOnlySpecificNodes.containsOnlySpecificNodes;
import static org.neo4j.tutorial.matchers.PathsMatcher.consistPreciselyOf;

/**
 * In this Koan we use some of the pre-canned graph algorithms that come with
 * Neo4j to gain more insight into the Doctor's universe.
 */
public class GraphAlgorithmsInJavaFormerlyKoan09
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
    public void shouldRevealTheEpisodesWhereRoseTylerFoughtTheDaleks()
    {
        GraphDatabaseService database = universe.getDatabase();

        try ( Transaction tx = database.beginTx() )
        {
            Node rose = database.findNodesByLabelAndProperty( DoctorWhoLabels.CHARACTER, "character",
                    "Rose Tyler" ).iterator().next();
            Node daleks = database.findNodesByLabelAndProperty( DoctorWhoLabels.SPECIES, "species",
                    "Dalek" ).iterator().next();
            Iterable<Path> paths = null;

            // YOUR CODE GOES HERE
            // SNIPPET_START

            PathFinder<Path> pathFinder = GraphAlgoFactory.pathsWithLength(
                    Traversal.expanderForTypes( DoctorWhoRelationships.APPEARED_IN, Direction.BOTH ), 2 );
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
            roseVersusDaleksEpisodes.add( universe.getDatabase().findNodesByLabelAndProperty( DoctorWhoLabels
                    .EPISODE, "title", title ).iterator().next() );
        }
        return roseVersusDaleksEpisodes;
    }

    @Test
    public void shouldFindTheNumberOfMasterRegenerationsTheEasyWay()
    {
        GraphDatabaseService database = universe.getDatabase();

        try ( Transaction tx = database.beginTx() )
        {
            Node delgado = database.findNodesByLabelAndProperty( DoctorWhoLabels.ACTOR, "actor",
                    "Roger Delgado" ).iterator().next();
            Node simm = database.findNodesByLabelAndProperty( DoctorWhoLabels.ACTOR, "actor",
                    "John Simm" ).iterator().next();
            Path path = null;

            // YOUR CODE GOES HERE
            // SNIPPET_START

            PathFinder<Path> pathFinder = GraphAlgoFactory.shortestPath(
                    Traversal.expanderForTypes( DoctorWhoRelationships.REGENERATED_TO, Direction.OUTGOING ), 100 );
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
    public void shouldRevealEpisodeWhenTennantRegeneratedToSmith()
    {
        GraphDatabaseService database = universe.getDatabase();
        try ( Transaction tx = database.beginTx() )
        {
            Node tennant = database.findNodesByLabelAndProperty( DoctorWhoLabels.ACTOR, "actor",
                    "David Tennant" ).iterator().next();
            Node smith = database.findNodesByLabelAndProperty( DoctorWhoLabels.ACTOR, "actor",
                    "Matt Smith" ).iterator().next();
            Path path = null;

            // YOUR CODE GOES HERE
            // SNIPPET_START

            PathFinder<Path> pathFinder = GraphAlgoFactory.pathsWithLength(
                    Traversal.expanderForTypes( DoctorWhoRelationships.APPEARED_IN, Direction.BOTH ), 2 );
            path = pathFinder.findSinglePath( tennant, smith );

            // SNIPPET_END

            tx.success();

            assertNotNull( path );
            Node endOfTimeEpisode = database.findNodesByLabelAndProperty( DoctorWhoLabels.EPISODE, "title",
                    "The End of Time" ).iterator().next();
            assertThat( path, containsOnlySpecificNodes( tennant, smith, endOfTimeEpisode ) );
        }
    }
}
