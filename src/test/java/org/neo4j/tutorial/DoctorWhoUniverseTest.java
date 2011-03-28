package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.neo4j.tutorial.DoctorWhoUniverse.COMPANION_OF;
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
import org.neo4j.graphdb.index.IndexHits;
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
        
        IndexHits<Node> indexHits = doctorWhoUniverse.getCharacterIndex().get("name", "Doctor");
        assertEquals(1, indexHits.size());
        
        Node theDoctor = indexHits.getSingle();
        assertNotNull(theDoctor);
        assertEquals(numberOfDoctors, databaseHelper.countRelationships(theDoctor.getRelationships(PLAYED, Direction.INCOMING)));
    }

    @Test
    public void shouldBeTenRegenerationRelationshipsBetweenTheElevenDoctors() {
        int numberOfDoctorsRegenerations = 10;
        
        IndexHits<Node> indexHits = doctorWhoUniverse.getActorIndex().get("lastname", "Hartnell");
        assertEquals(1, indexHits.size());
        
        Node firstDoctor = indexHits.getSingle();
        assertNotNull(firstDoctor);
        assertEquals(numberOfDoctorsRegenerations, countRelationships(firstDoctor));
    }
    
    @Test
    public void shouldBeSevenRegenerationRelationshipsBetweenTheEightMasters() {
        int numberOfMastersRegenerations = 7;

        IndexHits<Node> indexHits = doctorWhoUniverse.getActorIndex().get("lastname", "Delgado");
        assertEquals(1, indexHits.size());
        
        Node currentMaster = indexHits.getSingle(); 
        assertEquals(numberOfMastersRegenerations, countRelationships(currentMaster));
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
        
        IndexHits<Node> indexHits = doctorWhoUniverse.getCharacterIndex().get("name", "Master");
        assertEquals(1, indexHits.size());
        
        Node theMaster = indexHits.getSingle(); 
        assertNotNull(theMaster);
        assertEquals(numberOfMasters, databaseHelper.countRelationships(theMaster.getRelationships(PLAYED, Direction.INCOMING)));
    }
    
    @Test
    public void shouldContainMasterAndDoctorInTheIndex() {
        assertEquals(1, doctorWhoUniverse.getCharacterIndex().get("name", "Master").size());
        assertEquals(1, doctorWhoUniverse.getCharacterIndex().get("name", "Doctor").size());
    }
    
    @Test
    public void doctorAndMasterAreTimelords() {
        int numberOfTimelords = 2;

        Node timelord = doctorWhoUniverse.getSpeciesIndex().get("species", "Timelord").getSingle();
        
        Iterable<Relationship> relationships = timelord.getRelationships(DoctorWhoUniverse.IS_A, Direction.INCOMING);
        
        assertNotNull(timelord);
        assertEquals(numberOfTimelords, databaseHelper.countRelationships(relationships));
    }
    
    @Test
    public void timelordsShouldComeFromGallifrey() {
        Node gallifrey = doctorWhoUniverse.getPlanetIndex().get("planet", "Gallifrey").getSingle();
        assertNotNull(gallifrey);
        
        
        Iterable<Relationship> relationships = gallifrey.getRelationships(DoctorWhoUniverse.FROM, Direction.INCOMING);
        
        int numberOfSpeciesFromGallifrey = 1;
        assertEquals(numberOfSpeciesFromGallifrey, databaseHelper.countRelationships(relationships));
    }
    
    @Test
    public void shortestPathBetweenDoctorAndMasterShouldBeLengthOneTypeEnemyOf() {
        Node theMaster = doctorWhoUniverse.getCharacterIndex().get("name", "Master").getSingle();
        Node theDoctor = doctorWhoUniverse.getCharacterIndex().get("name", "Doctor").getSingle();
        
        
        
        int maxDepth = 5; // No more than 5, or we find Kevin Bacon!
        PathFinder<Path> shortestPathFinder = GraphAlgoFactory.shortestPath(Traversal.expanderForAllTypes(), maxDepth);
        
        Path shortestPath = shortestPathFinder.findSinglePath(theDoctor, theMaster);
        assertEquals(1, shortestPath.length());
        assertEquals(DoctorWhoUniverse.ENEMY_OF, shortestPath.lastRelationship().getType());
    }
    
    @Test
    public void daleksShouldBeEnemiesOfTheDoctor() {
        Node dalek = doctorWhoUniverse.getSpeciesIndex().get("species", "Dalek").getSingle();
        assertNotNull(dalek);
        Iterable<Relationship> enemiesOf = dalek.getRelationships(DoctorWhoUniverse.ENEMY_OF, Direction.OUTGOING);
        assertTrue(containsTheDoctor(enemiesOf));
    }
    
    @Test
    public void cybermenShouldBeEnemiesOfTheDoctor() {
        Node cyberman = doctorWhoUniverse.getSpeciesIndex().get("species", "Cyberman").getSingle();
        assertNotNull(cyberman);
        Iterable<Relationship> enemiesOf = cyberman.getRelationships(DoctorWhoUniverse.ENEMY_OF, Direction.OUTGOING);
        assertTrue(containsTheDoctor(enemiesOf));
    }

    private boolean containsTheDoctor(Iterable<Relationship> enemiesOf) {
        Node theDoctor = doctorWhoUniverse.getCharacterIndex().get("name", "Doctor").getSingle();
        for(Relationship r : enemiesOf) {
            if(r.getEndNode().equals(theDoctor)) {
                return true;
            }
        }
        return false;
    }
    
    @Test
    public void shouldFindEnemiesOfEnemies() {
        
        Node theMaster = doctorWhoUniverse.getCharacterIndex().get("name", "Master").getSingle();
        Node dalek = doctorWhoUniverse.getSpeciesIndex().get("species", "Dalek").getSingle();
        Node cyberman = doctorWhoUniverse.getSpeciesIndex().get("species", "Cyberman").getSingle();
        Node silurian = doctorWhoUniverse.getSpeciesIndex().get("species", "Silurian").getSingle();
        Node sontaran = doctorWhoUniverse.getSpeciesIndex().get("species", "Sontaran").getSingle();
        
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
        assertEquals(4, databaseHelper.countNodes(nodes));
        assertTrue(isInList(dalek, listOfNodes));
        assertTrue(isInList(cyberman, listOfNodes));
        assertTrue(isInList(silurian, listOfNodes));
        assertTrue(isInList(sontaran, listOfNodes));
    }

    private boolean isInList(Node candidateNode, List<Node> listOfNodes) {
        for(Node n : listOfNodes) {
            if(n.equals(candidateNode)) {
                return true;
            }
        }
        return false;
    }
    
    @Test
    public void shouldHave46Companions() {
        int numberOfCompanions = 46;

        IndexHits<Node> indexHits = doctorWhoUniverse.getCharacterIndex().get("name", "Doctor");
        assertEquals(1, indexHits.size());
        
        Node theDoctor = indexHits.getSingle();
        assertNotNull(theDoctor);
        
        assertEquals(numberOfCompanions , databaseHelper.countRelationships(theDoctor.getRelationships(COMPANION_OF, Direction.INCOMING)));
    }
}
