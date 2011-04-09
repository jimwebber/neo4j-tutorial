package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.neo4j.tutorial.DoctorWhoUniverse.COMPANION_OF;
import static org.neo4j.tutorial.DoctorWhoUniverse.ENEMY_OF;
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
    
    @SuppressWarnings("unused")
    @Test
    public void shouldHave446Planets() {
        IndexHits<Node> indexHits = doctorWhoUniverse.planetIndex.query("planet", "*");
        int planetCount = 0;
        for(Node n : indexHits) {
            planetCount++;
        }
        
        int numberOfPlanetsMentionedInTVEpisodes = 446;
        assertEquals(numberOfPlanetsMentionedInTVEpisodes, planetCount);
    }
    
    @Test
    public void shouldHave46Friendlies() {
        IndexHits<Node> indexHits = doctorWhoUniverse.friendliesIndex.query("name", "*");
        int friendlyCount = 0;
        
        for(Node n : indexHits) {
            friendlyCount++;
        }
        
        int numberOfFriendlies = 46;
        assertEquals(numberOfFriendlies, friendlyCount);
    }
    
    @Test
    public void shouldHave40HumanFriendlies() {
        Node humanSpeciesNode = doctorWhoUniverse.speciesIndex.get("species", "Human").getSingle();
        int numberOfHumansFriendliesInTheDB = databaseHelper.countRelationships(humanSpeciesNode.getRelationships(DoctorWhoUniverse.IS_A, Direction.INCOMING));
        
        int knownNumberOfHumanFriendlies = 40;
        assertEquals(knownNumberOfHumanFriendlies, numberOfHumansFriendliesInTheDB);
    }
    
    @Test
    public void shouldHave30Enemies() {
        IndexHits<Node> indexHits = doctorWhoUniverse.enemiesIndex.query("name", "*");
        int enemyCount = 0;
        
        for(Node n : indexHits) {
            enemyCount++;
        }
        
        int numberOfEnemies = 30;
        assertEquals(numberOfEnemies, enemyCount);
    }
    
    @Test
    public void shouldBe6Timelords() {
        Node timelordSpeciesNode = doctorWhoUniverse.speciesIndex.get("species", "Timelord").getSingle();
       
        int numberOfTimelordsInTheDb = databaseHelper.countRelationships(timelordSpeciesNode.getRelationships(DoctorWhoUniverse.IS_A, Direction.INCOMING));
        
        int knownNumberOfTimelords = 6;
        assertEquals(knownNumberOfTimelords, numberOfTimelordsInTheDb);
    }
    
    @Test
    public void shouldHave16Species() {
        IndexHits<Node> indexHits = doctorWhoUniverse.speciesIndex.query("species", "*");
        int speciesCount = 0;
        for(Node n : indexHits) {
            speciesCount++;
        }
        
        int numberOfSpecies = 16;
        assertEquals(numberOfSpecies, speciesCount); 
    }

    @Test
    public void shouldHave11ActorsThatHavePlayedTheDoctor() {
        int numberOfDoctors = 11;
        
        Node theDoctor = doctorWhoUniverse.theDoctor();
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
        Node theMaster = doctorWhoUniverse.theMaster();
        
        assertNotNull(theMaster);
        assertEquals(numberOfMasters, databaseHelper.countRelationships(theMaster.getRelationships(PLAYED, Direction.INCOMING)));
    }
    
    @Test
    public void timelordsShouldComeFromGallifrey() {
        Node gallifrey = doctorWhoUniverse.getPlanetIndex().get("planet", "Gallifrey").getSingle();
        Node timelord = doctorWhoUniverse.getSpeciesIndex().get("species", "Timelord").getSingle();
        assertNotNull(gallifrey);
        assertNotNull(timelord);
        
        Iterable<Relationship> relationships = timelord.getRelationships(DoctorWhoUniverse.FROM, Direction.OUTGOING);
        List<Relationship> listOfRelationships = databaseHelper.toListOfRelationships(relationships);
        
        assertEquals(1, listOfRelationships.size());
        assertTrue(listOfRelationships.get(0).getEndNode().equals(gallifrey));
    }
    
    @Test
    public void shortestPathBetweenDoctorAndMasterShouldBeLengthOneTypeEnemyOf() {
        Node theMaster = doctorWhoUniverse.theMaster();
        Node theDoctor = doctorWhoUniverse.theDoctor();

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
        Node theDoctor = doctorWhoUniverse.theDoctor();
        for(Relationship r : enemiesOf) {
            if(r.getEndNode().equals(theDoctor)) {
                return true;
            }
        }
        return false;
    }
    
    @Test
    public void shouldFindEnemiesOfEnemies() {
        
        Node theMaster = doctorWhoUniverse.theMaster();
        Node dalek = doctorWhoUniverse.getSpeciesIndex().get("species", "Dalek").getSingle();
        Node cyberman = doctorWhoUniverse.getSpeciesIndex().get("species", "Cyberman").getSingle();
        Node silurian = doctorWhoUniverse.getSpeciesIndex().get("species", "Silurian").getSingle();
        Node sontaran = doctorWhoUniverse.getSpeciesIndex().get("species", "Sontaran").getSingle();
        
        Traverser traverser = Traversal.description().expand(Traversal.expanderForTypes(DoctorWhoUniverse.ENEMY_OF, Direction.BOTH)).depthFirst().evaluator(new Evaluator() {
            
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
        
        int numberOfIndividualAndSpeciesEnemiesInTheDatabase = 40;
        assertEquals(numberOfIndividualAndSpeciesEnemiesInTheDatabase, databaseHelper.countNodes(nodes));
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
    public void shouldBe11EnemySpecies() {
        int numberOfEnemySpecies = 11;
        Node theDoctor = doctorWhoUniverse.theDoctor();
        
        Iterable<Relationship> relationships = theDoctor.getRelationships(ENEMY_OF, Direction.INCOMING);
        int enemySpeciesFound = 0;
        for(Relationship rel : relationships) {
            if(rel.getStartNode().hasProperty("species")) {
                enemySpeciesFound++;
            }
        }
        
        assertEquals(numberOfEnemySpecies, enemySpeciesFound);
    }
    
    @Test
    public void shouldHave42CompanionsInTotal() {
        int numberOfCompanions = 42;

        Node theDoctor = doctorWhoUniverse.theDoctor();
        assertNotNull(theDoctor);
        
        assertEquals(numberOfCompanions , databaseHelper.countRelationships(theDoctor.getRelationships(COMPANION_OF, Direction.INCOMING)));
    }
    
    @Test
    public void shouldHave30IndividualEnemyCharactersInTotal() {
        int numberOfEnemies = 30;

        Node theDoctor = doctorWhoUniverse.theDoctor();
        assertNotNull(theDoctor);
        
        int count = 0;
        Iterable<Relationship> relationships = theDoctor.getRelationships(ENEMY_OF);
        for(Relationship rel : relationships) {
            if(rel.getStartNode().hasProperty("name")) {
                count ++;
            }
        }
        
        assertEquals(numberOfEnemies, count);
        
    }
}
