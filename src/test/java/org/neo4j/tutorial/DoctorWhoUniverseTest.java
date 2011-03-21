package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.neo4j.tutorial.DoctorWhoUniverse.PLAYED;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;

public class DoctorWhoUniverseTest {
    
    private DoctorWhoUniverse doctorWhoUniverse;
    private GraphDatabaseService doctorWhoDatabase;
    private DatabaseHelper databaseHelper;

    @Before
    public void startDatabase() throws Exception {
        doctorWhoUniverse = new DoctorWhoUniverse();
        doctorWhoDatabase = doctorWhoUniverse.getDatabase();
        databaseHelper = new DatabaseHelper(doctorWhoDatabase);
    }

    @After
    public void stopDatabase() {
        doctorWhoDatabase.shutdown();
    }

    @Test
    public void shouldHave11Doctors() {
        int numberOfDoctors = 11;
        
        Node theDoctor = doctorWhoUniverse.getIndex().getSingleNode("name", "Doctor");
        assertNotNull(theDoctor);
        assertEquals(numberOfDoctors, databaseHelper.countRelationships(theDoctor.getRelationships(PLAYED, Direction.INCOMING)));
    }

    @Test
    public void shouldBeTenRegenerationRelationshipsBetweenTheElevenDoctors() {
        int numberOfDoctorsRegenerations = 10;
        Node firstDoctor = doctorWhoUniverse.getIndex().getSingleNode("lastname", "Hartnell");
        assertNotNull(firstDoctor);
        assertEquals(numberOfDoctorsRegenerations, countRelationships(firstDoctor));
    }
    
    @Test
    public void shouldBeSevenRegenerationRelationshipsBetweenTheEightMasters() {
        Node currentDoctor = doctorWhoUniverse.getIndex().getSingleNode("lastname", "Delgado");
        int numberOfMastersRegenerations = 7;
        assertEquals(numberOfMastersRegenerations, countRelationships(currentDoctor));
    }

    private int countRelationships(Node timelord) {
        int regenerationCount = 0;
        while (true) {
            List<Relationship> relationships = databaseHelper.toListOfRelationships(timelord.getRelationships(DoctorWhoUniverse.REGENERATED_TO,
                    Direction.OUTGOING));

            if (relationships.size() == 1) {
                Relationship regeneratedTo = relationships.get(0);
                timelord = regeneratedTo.getEndNode();
                regenerationCount++;
            } else {
                break;
            }
        }
        return regenerationCount;
    }

    @Test
    public void shouldHave8Masters() {
        int numberOfMasters = 8;
        
        Node theMaster = doctorWhoUniverse.getIndex().getSingleNode("name", "Master");
        assertNotNull(theMaster);
        assertEquals(numberOfMasters, databaseHelper.countRelationships(theMaster.getRelationships(PLAYED, Direction.INCOMING)));
    }
    
    @Test
    public void shouldContainMasterAndDoctorInTheIndex() {
        assertNotNull(doctorWhoUniverse.getIndex().getSingleNode("name", "Master"));
        assertNotNull(doctorWhoUniverse.getIndex().getSingleNode("name", "Doctor"));
    }
    
    @Test
    public void doctorAndMasterAreTimelords() {
        int numberOfTimelords = 2;

        Node timelord = doctorWhoUniverse.getIndex().getSingleNode("species", "Timelord");
        
        Iterable<Relationship> relationships = timelord.getRelationships(DoctorWhoUniverse.IS_A, Direction.INCOMING);
        
        assertNotNull(timelord);
        assertEquals(numberOfTimelords, databaseHelper.countRelationships(relationships));
    }
    
    @Test
    public void timelordsShouldComeFromGallifrey() {
        Node gallifrey = doctorWhoUniverse.getIndex().getSingleNode("planet", "Gallifrey");
        assertNotNull(gallifrey);
        
        
        Iterable<Relationship> relationships = gallifrey.getRelationships(DoctorWhoUniverse.FROM, Direction.INCOMING);
        
        int numberOfSpeciesFromGallifrey = 1;
        assertEquals(numberOfSpeciesFromGallifrey, databaseHelper.countRelationships(relationships));
    }
    
    @Test
    public void shortestPathBetweenDoctorAndMasterShouldBeLengthOneTypeEnemyOf() {
        Node theMaster = doctorWhoUniverse.getIndex().getSingleNode("name", "Master");
        Node theDoctor = doctorWhoUniverse.getIndex().getSingleNode("name", "Doctor");
        
        
        
        int maxDepth = 5; // No more than 5, or we find Kevin Bacon!
        PathFinder<Path> shortestPathFinder = GraphAlgoFactory.shortestPath(Traversal.expanderForAllTypes(), maxDepth);
        
        Path shortestPath = shortestPathFinder.findSinglePath(theDoctor, theMaster);
        assertEquals(1, shortestPath.length());
        assertEquals(DoctorWhoUniverse.ENEMY_OF, shortestPath.lastRelationship().getType());
    }
    
    @Test
    public void daleksShouldBeEnemiesOfTheDoctor() {
        Node dalek = doctorWhoUniverse.getIndex().getSingleNode("species", "Dalek");
        assertNotNull(dalek);
        Iterable<Relationship> enemiesOf = dalek.getRelationships(DoctorWhoUniverse.ENEMY_OF, Direction.OUTGOING);
        assertTrue(containsTheDoctor(enemiesOf));
    }
    
    @Test
    public void cybermenShouldBeEnemiesOfTheDoctor() {
        Node cyberman = doctorWhoUniverse.getIndex().getSingleNode("species", "Cyberman");
        assertNotNull(cyberman);
        Iterable<Relationship> enemiesOf = cyberman.getRelationships(DoctorWhoUniverse.ENEMY_OF, Direction.OUTGOING);
        assertTrue(containsTheDoctor(enemiesOf));
    }

    private boolean containsTheDoctor(Iterable<Relationship> enemiesOf) {
        Node theDoctor = doctorWhoUniverse.getIndex().getSingleNode("name", "Doctor");
        for(Relationship r : enemiesOf) {
            if(r.getEndNode().equals(theDoctor)) {
                return true;
            }
        }
        return false;
    }
    
    @Test
    public void shouldFindEnemiesOfEnemies() {
        
        Node theMaster = doctorWhoUniverse.getIndex().getSingleNode("name", "Master");
        Node dalek = doctorWhoUniverse.getIndex().getSingleNode("species", "Dalek");
        Node cyberman = doctorWhoUniverse.getIndex().getSingleNode("species", "Cyberman");
        
        Traverser traverser = Traversal.description().expand(Traversal.expanderForTypes(DoctorWhoUniverse.ENEMY_OF, Direction.OUTGOING)).depthFirst().evaluator(new Evaluator() {
            
            @Override
            public Evaluation evaluate(Path path) {
                // Only include if we're at depth 2, don't want any mere enemies
                if(path.length() == 2) {
                    return Evaluation.INCLUDE_AND_PRUNE;
                } else if(path.length() > 2){
                    return Evaluation.EXCLUDE_AND_PRUNE;
                } else {
                    return Evaluation.EXCLUDE_AND_CONTINUE;
                }
            }
        }).uniqueness(Uniqueness.NODE_GLOBAL)
        .traverse(theMaster);
        

        Iterable<Node> nodes = traverser.nodes();
        assertNotNull(nodes);

        List<Node> listOfNodes = databaseHelper.toListOfNodes(nodes);
        assertEquals(2, databaseHelper.countNodes(nodes));
        assertTrue(isInList(dalek, listOfNodes));
        assertTrue(isInList(cyberman, listOfNodes));
    }

    private boolean isInList(Node candidateNode, List<Node> listOfNodes) {
        for(Node n : listOfNodes) {
            if(n.equals(candidateNode)) {
                return true;
            }
        }
        return false;
    }
}
