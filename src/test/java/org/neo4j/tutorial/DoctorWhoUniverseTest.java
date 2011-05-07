package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.neo4j.tutorial.DoctorWhoUniverse.COMPANION_OF;
import static org.neo4j.tutorial.DoctorWhoUniverse.ENEMY_OF;
import static org.neo4j.tutorial.DoctorWhoUniverse.PLAYED;

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
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;

/**
 * Be careful when adding tests here - each test in this class uses the same
 * database instance and so can pollute. This was done for performance reasons
 * since loading the database for each test takes a long time, even on fast
 * hardware.
 * 
 */
public class DoctorWhoUniverseTest {

    private static DoctorWhoUniverse universe;
    private static GraphDatabaseService database;
    private static DatabaseHelper databaseHelper;

    @BeforeClass
    public static void startDatabase() throws Exception {
        universe = new DoctorWhoUniverse();
        database = universe.getDatabase();
        databaseHelper = new DatabaseHelper(database);
    }

    @AfterClass
    public static void stopDatabase() {
        database.shutdown();
    }

    @SuppressWarnings("unused")
    @Test
    public void shouldHaveCorrectNumberOfPlanetsInIndex() {
        IndexHits<Node> indexHits = universe.planetIndex.query("planet", "*");
        int planetCount = 0;
        for (Node n : indexHits) {
            planetCount++;
        }

        int numberOfPlanetsMentionedInTVEpisodes = 447;
        assertEquals(numberOfPlanetsMentionedInTVEpisodes, planetCount);
    }

    @Test
    public void shouldHaveCorrectNumberOfHumans() {
        Node humanSpeciesNode = universe.speciesIndex.get("species", "Human").getSingle();
        int numberOfHumansFriendliesInTheDB = databaseHelper.countRelationships(humanSpeciesNode.getRelationships(DoctorWhoUniverse.IS_A, Direction.INCOMING));

        int knownNumberOfHumans = 46;
        assertEquals(knownNumberOfHumans, numberOfHumansFriendliesInTheDB);
    }

    @Test
    public void shouldBe6Timelords() {
        Node timelordSpeciesNode = universe.speciesIndex.get("species", "Timelord").getSingle();

        int numberOfTimelordsInTheDb = databaseHelper.countRelationships(timelordSpeciesNode.getRelationships(DoctorWhoUniverse.IS_A, Direction.INCOMING));

        int knownNumberOfTimelords = 6;
        assertEquals(knownNumberOfTimelords, numberOfTimelordsInTheDb);
    }

    @SuppressWarnings("unused")
    @Test
    public void shouldHaveCorrectNumberOfSpecies() {
        IndexHits<Node> indexHits = universe.speciesIndex.query("species", "*");
        int speciesCount = 0;
        for (Node n : indexHits) {
            speciesCount++;
        }

        int numberOfSpecies = 53;
        assertEquals(numberOfSpecies, speciesCount);
    }

    @Test
    public void shouldHave12ActorsThatHavePlayedTheDoctor() {
        int numberOfDoctors = 12; // 12 Because the first doctor was played by 2
                                  // actors over the course of the franchise

        Node theDoctor = universe.theDoctor();
        assertNotNull(theDoctor);
        assertEquals(numberOfDoctors, databaseHelper.countRelationships(theDoctor.getRelationships(PLAYED, Direction.INCOMING)));
    }

    @Test
    public void shouldBeTenRegenerationRelationshipsBetweenTheElevenDoctors() {
        int numberOfDoctorsRegenerations = 10;

        IndexHits<Node> indexHits = getActorIndex().get("actor", "William Hartnell");
        assertEquals(1, indexHits.size());

        Node firstDoctor = indexHits.getSingle();
        assertNotNull(firstDoctor);
        assertEquals(numberOfDoctorsRegenerations, countRelationships(firstDoctor));
    }

    @Test
    public void shouldBeSevenRegenerationRelationshipsBetweenTheEightMasters() {
        int numberOfMastersRegenerations = 7;

        IndexHits<Node> indexHits = getActorIndex().get("actor", "Roger Delgado");
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
        Node theMaster = universe.characterIndex.get("name", "Master").getSingle();

        assertNotNull(theMaster);
        assertEquals(numberOfMasters, databaseHelper.countRelationships(theMaster.getRelationships(PLAYED, Direction.INCOMING)));
    }

    @Test
    public void timelordsShouldComeFromGallifrey() {
        Node gallifrey = getPlanetIndex().get("planet", "Gallifrey").getSingle();
        Node timelord = getSpeciesIndex().get("species", "Timelord").getSingle();
        assertNotNull(gallifrey);
        assertNotNull(timelord);

        Iterable<Relationship> relationships = timelord.getRelationships(DoctorWhoUniverse.COMES_FROM, Direction.OUTGOING);
        List<Relationship> listOfRelationships = databaseHelper.toListOfRelationships(relationships);

        assertEquals(1, listOfRelationships.size());
        assertTrue(listOfRelationships.get(0).getEndNode().equals(gallifrey));
    }

    @Test
    public void shortestPathBetweenDoctorAndMasterShouldBeLengthOneTypeEnemyOf() {
        Node theMaster = universe.characterIndex.get("name", "Master").getSingle();
        Node theDoctor = universe.characterIndex.get("name", "Doctor").getSingle();

        int maxDepth = 5; // No more than 5, or we find Kevin Bacon!
        PathFinder<Path> shortestPathFinder = GraphAlgoFactory.shortestPath(Traversal.expanderForAllTypes(), maxDepth);

        Path shortestPath = shortestPathFinder.findSinglePath(theDoctor, theMaster);
        assertEquals(1, shortestPath.length());
        assertEquals(DoctorWhoUniverse.ENEMY_OF, shortestPath.lastRelationship().getType());
    }

    @Test
    public void daleksShouldBeEnemiesOfTheDoctor() {
        Node dalek = getSpeciesIndex().get("species", "Dalek").getSingle();
        assertNotNull(dalek);
        Iterable<Relationship> enemiesOf = dalek.getRelationships(DoctorWhoUniverse.ENEMY_OF, Direction.OUTGOING);
        assertTrue(containsTheDoctor(enemiesOf));
    }

    @Test
    public void cybermenShouldBeEnemiesOfTheDoctor() {
        Node cyberman = getSpeciesIndex().get("species", "Cyberman").getSingle();
        assertNotNull(cyberman);
        Iterable<Relationship> enemiesOf = cyberman.getRelationships(DoctorWhoUniverse.ENEMY_OF, Direction.OUTGOING);
        assertTrue(containsTheDoctor(enemiesOf));
    }

    private boolean containsTheDoctor(Iterable<Relationship> enemiesOf) {
        Node theDoctor = universe.theDoctor();
        for (Relationship r : enemiesOf) {
            if (r.getEndNode().equals(theDoctor)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void shouldFindEnemiesOfTheMastersEnemies() {

        Node theMaster = universe.characterIndex.get("name", "Master").getSingle();
        Node dalek = getSpeciesIndex().get("species", "Dalek").getSingle();
        Node cyberman = getSpeciesIndex().get("species", "Cyberman").getSingle();
        Node silurian = getSpeciesIndex().get("species", "Silurian").getSingle();
        Node sontaran = getSpeciesIndex().get("species", "Sontaran").getSingle();

        Traverser traverser = Traversal.description().expand(Traversal.expanderForTypes(DoctorWhoUniverse.ENEMY_OF, Direction.OUTGOING)).depthFirst()
                .evaluator(new Evaluator() {

                    @Override
                    public Evaluation evaluate(Path path) {
                        // Only include if we're at depth 2, don't want any mere
                        // enemies
                        if (path.length() == 2) {
                            return Evaluation.INCLUDE_AND_PRUNE;
                        } else if (path.length() > 2) {
                            return Evaluation.EXCLUDE_AND_PRUNE;
                        } else {
                            return Evaluation.EXCLUDE_AND_CONTINUE;
                        }
                    }
                }).uniqueness(Uniqueness.NODE_GLOBAL).traverse(theMaster);

        Iterable<Node> nodes = traverser.nodes();
        assertNotNull(nodes);

        List<Node> enemiesOfEnemies = databaseHelper.toListOfNodes(nodes);

        int numberOfIndividualAndSpeciesEnemiesInTheDatabase = 138;
        assertEquals(numberOfIndividualAndSpeciesEnemiesInTheDatabase, enemiesOfEnemies.size());
        assertTrue(isInList(dalek, enemiesOfEnemies));
        assertTrue(isInList(cyberman, enemiesOfEnemies));
        assertTrue(isInList(silurian, enemiesOfEnemies));
        assertTrue(isInList(sontaran, enemiesOfEnemies));
    }

    private boolean isInList(Node candidateNode, List<Node> listOfNodes) {
        for (Node n : listOfNodes) {
            if (n.equals(candidateNode)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void shouldBeCorrectNumberOfEnemySpecies() {
        int numberOfEnemySpecies = 41;
        Node theDoctor = universe.theDoctor();

        Iterable<Relationship> relationships = theDoctor.getRelationships(ENEMY_OF, Direction.INCOMING);
        int enemySpeciesFound = 0;
        for (Relationship rel : relationships) {
            if (rel.getStartNode().hasProperty("species")) {
                enemySpeciesFound++;
            }
        }

        assertEquals(numberOfEnemySpecies, enemySpeciesFound);
    }

    @Test
    public void shouldHaveCorrectNumberOfCompanionsInTotal() {
        int numberOfCompanions = 45;

        Node theDoctor = universe.theDoctor();
        assertNotNull(theDoctor);

        assertEquals(numberOfCompanions, databaseHelper.countRelationships(theDoctor.getRelationships(COMPANION_OF, Direction.INCOMING)));
    }

    @Test
    public void shouldHaveCorrectNumberofIndividualEnemyCharactersInTotal() {
        int numberOfEnemies = 98;

        Node theDoctor = universe.theDoctor();
        assertNotNull(theDoctor);

        int count = 0;
        Iterable<Relationship> relationships = theDoctor.getRelationships(ENEMY_OF, Direction.INCOMING);
        for (Relationship rel : relationships) {
            if (rel.getStartNode().hasProperty("name")) {
                count++;
            }
        }

        assertEquals(numberOfEnemies, count);
    }

    private Index<Node> getActorIndex() {
        return database.index().forNodes("actors");
    }

    private Index<Node> getPlanetIndex() {
        return database.index().forNodes("planets");
    }

    private Index<Node> getSpeciesIndex() {
        return database.index().forNodes("species");
    }

    @Test
    public void severalSpeciesShouldBeEnemies() {
        assertTrue(areMututalEnemySpecies("Dalek", "Cyberman"));
        assertTrue(areMututalEnemySpecies("Dalek", "Human"));
        assertTrue(areMututalEnemySpecies("Human", "Auton"));
        assertTrue(areMututalEnemySpecies("Timelord", "Dalek"));
    }

    private boolean areMututalEnemySpecies(String enemy1, String enemy2) {
        Index<Node> speciesIndex = database.index().forNodes("species");

        Node n1 = speciesIndex.get("species", enemy1).getSingle();
        Node n2 = speciesIndex.get("species", enemy2).getSingle();

        return isEnemyOf(n1, n2) && isEnemyOf(n2, n1);
    }

    private boolean isEnemyOf(Node n1, Node n2) {
        for (Relationship r : n1.getRelationships(ENEMY_OF, Direction.OUTGOING)) {
            if (r.getEndNode().equals(n2)) {
                return true;
            }
        }
        return false;
    }
}
