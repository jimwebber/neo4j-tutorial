package org.neo4j.tutorial;

import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificNode.contains;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.kernel.Traversal;

/**
 * In this Koan we use some of the pre-canned graph algorithms that come with
 * Neo4j to gain more insight into the Doctor's universe.
 */
public class Koan07 {

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
    public void shouldFindTheNumberOfMasterRegenerationsTheEasyWay() {
        Node delgado = universe.actorIndex.get("actor", "Roger Delgado").getSingle();
        Node simm = universe.actorIndex.get("actor", "John Simm").getSingle();
        
        // SNIPPET_START

        PathFinder<Path> pathFinder = GraphAlgoFactory.shortestPath(Traversal.expanderForTypes(DoctorWhoUniverse.REGENERATED_TO, Direction.OUTGOING), 100);
        Path path = pathFinder.findSinglePath(delgado, simm);

        // SNIPPET_END
        
        int numberOfMasterRegenerations = 7;
        assertEquals(numberOfMasterRegenerations, path.length());
    }

    @Test
    public void shouldEpisodeWhenTennantRegeneratedToSmith() {
        Node tennant = universe.actorIndex.get("actor", "David Tennant").getSingle();
        Node smith = universe.actorIndex.get("actor", "Matt Smith").getSingle();

        // SNIPPET_START

        PathFinder<Path> pathFinder = GraphAlgoFactory.pathsWithLength(Traversal.expanderForTypes(DoctorWhoUniverse.APPEARED_IN, Direction.BOTH), 2);
        Path path = pathFinder.findSinglePath(tennant, smith);

        // SNIPPET_END

        Node endOfTimeEpisode = universe.episodeIndex.get("title", "The End of Time").getSingle();
        assertThat(path, contains(endOfTimeEpisode));
    }
}
