package org.neo4j.tutorial;

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
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.graphdb.traversal.Uniqueness;
import org.neo4j.kernel.Traversal;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Be careful when adding tests here - each test in this class uses the same
 * database instance and so can pollute. This was done for performance reasons
 * since loading the database for each test takes a long time, even on fast
 * hardware.
 */
public class DoctorWhoUniverseGeneratorTest
{
    private static EmbeddedDoctorWhoUniverse universe;
    private static GraphDatabaseService database;
    private static DatabaseHelper databaseHelper;

    @BeforeClass
    public static void startDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator() );
        database = universe.getDatabase();
        databaseHelper = new DatabaseHelper( database );
    }

    @AfterClass
    public static void stopDatabase()
    {
        database.shutdown();
    }

    @Test
    public void shouldHaveCorrectNextAndPreviousLinks()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node ep = universe.getDatabase().index().forNodes( "episodes" ).get( "episode", 1 ).getSingle();

            int count = 1;
            while ( ep.hasRelationship( DoctorWhoRelationships.NEXT, Direction.OUTGOING ) )
            {
                ep = ep.getSingleRelationship( DoctorWhoRelationships.NEXT, Direction.OUTGOING ).getEndNode();
                count++;
            }

            assertEquals(
                    databaseHelper.count( universe.getDatabase().index().forNodes( "episodes" ).query( "episode",
                            "*" ) ),
                    count );

            while ( ep.hasRelationship( DoctorWhoRelationships.PREVIOUS, Direction.OUTGOING ) )
            {
                ep = ep.getSingleRelationship( DoctorWhoRelationships.PREVIOUS, Direction.OUTGOING ).getEndNode();
                count--;
            }

            assertEquals( 1, count );
            tx.failure();
        }
    }

    @SuppressWarnings("unused")
    @Test
    public void shouldHaveCorrectNumberOfPlanetsInIndex()
    {
        try ( Transaction tx = database.beginTx() )
        {
            IndexHits<Node> indexHits = universe.getDatabase()
                    .index()
                    .forNodes( "planets" )
                    .query( "planet", "*" );
            int planetCount = 0;
            for ( Node n : indexHits )
            {
                planetCount++;
            }

            int numberOfPlanetsMentionedInTVEpisodes = 447;
            assertEquals( numberOfPlanetsMentionedInTVEpisodes, planetCount );
            tx.failure();
        }
    }

    @Test
    public void theDoctorsRegenerationsShouldBeDated()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node nextDoctor = getActorIndex().get( "actor", "William Hartnell" )
                    .getSingle();

            boolean allDoctorsHaveRegenerationYears = true;

            do
            {
                Relationship relationship = nextDoctor.getSingleRelationship( DoctorWhoRelationships.REGENERATED_TO,
                        Direction.OUTGOING );
                if ( relationship == null )
                {
                    break;
                }

                allDoctorsHaveRegenerationYears = relationship.hasProperty( "year" ) &&
                        allDoctorsHaveRegenerationYears;

                nextDoctor = relationship.getEndNode();

            }
            while ( nextDoctor != null );

            assertTrue( allDoctorsHaveRegenerationYears );

            tx.failure();
        }
    }

    @Test
    public void shouldHaveCorrectNumberOfHumans()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node humanSpeciesNode = universe.getDatabase()
                    .index()
                    .forNodes( "species" )
                    .get( "species", "Human" )
                    .getSingle();
            int numberOfHumansFriendliesInTheDB = databaseHelper.destructivelyCountRelationships(
                    humanSpeciesNode.getRelationships(
                            DoctorWhoRelationships.IS_A, Direction.INCOMING ) );

            int knownNumberOfHumans = 55;
            assertEquals( knownNumberOfHumans, numberOfHumansFriendliesInTheDB );
            tx.failure();
        }
    }

    @Test
    public void shouldBe8TimelordsInTheUniverse()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node timelordSpeciesNode = universe.getDatabase()
                    .index()
                    .forNodes( "species" )
                    .get( "species", "Timelord" )
                    .getSingle();

            int numberOfTimelordsInTheDb = databaseHelper.destructivelyCountRelationships(
                    timelordSpeciesNode.getRelationships(
                            DoctorWhoRelationships.IS_A, Direction.INCOMING ) );

            int knownNumberOfTimelords = 9;
            assertEquals( knownNumberOfTimelords, numberOfTimelordsInTheDb );
            tx.failure();
        }
    }

    @Test
    public void shouldHaveCorrectNumberOfSpecies()
    {
        try ( Transaction tx = database.beginTx() )
        {
            IndexHits<Node> indexHits = universe.getDatabase()
                    .index()
                    .forNodes( "species" )
                    .query( "species", "*" );
            int speciesCount = 0;
            for ( Node n : indexHits )
            {
                speciesCount++;
            }

            int numberOfSpecies = 56;
            assertEquals( numberOfSpecies, speciesCount );
            tx.failure();
        }
    }

    @Test
    public void shouldHave12ActorsThatHavePlayedTheDoctor()
    {
        try ( Transaction tx = database.beginTx() )
        {
            int numberOfDoctors = 12; // 12 Because the first doctor was played by 2
            // actors over the course of the franchise

            Node theDoctor = universe.getDatabase()
                    .index()
                    .forNodes( "characters" )
                    .get( "character", "Doctor" )
                    .getSingle();
            assertNotNull( theDoctor );
            assertEquals( numberOfDoctors, databaseHelper.destructivelyCountRelationships( theDoctor.getRelationships(
                    DoctorWhoRelationships.PLAYED, Direction.INCOMING ) ) );
            tx.failure();
        }
    }

    @Test
    public void shouldBeTenRegenerationRelationshipsBetweenTheElevenDoctors()
    {
        try ( Transaction tx = database.beginTx() )
        {
            int numberOfDoctorsRegenerations = 10;

            IndexHits<Node> indexHits = getActorIndex().get( "actor", "William Hartnell" );
            assertEquals( 1, indexHits.size() );

            Node firstDoctor = indexHits.getSingle();
            assertNotNull( firstDoctor );
            assertEquals( numberOfDoctorsRegenerations, countRelationships( firstDoctor ) );
            tx.failure();
        }
    }

    @Test
    public void shouldBeSevenRegenerationRelationshipsBetweenTheEightMasters()
    {
        try ( Transaction tx = database.beginTx() )
        {
            int numberOfMastersRegenerations = 7;

            IndexHits<Node> indexHits = getActorIndex().get( "actor", "Roger Delgado" );
            assertEquals( 1, indexHits.size() );

            Node currentMaster = indexHits.getSingle();
            assertEquals( numberOfMastersRegenerations, countRelationships( currentMaster ) );
            tx.failure();
        }
    }

    private int countRelationships( Node timelord )
    {
        int regenerationCount = 0;
        while ( true )
        {
            List<Relationship> relationships = databaseHelper.toListOfRelationships( timelord.getRelationships(
                    DoctorWhoRelationships.REGENERATED_TO, Direction.OUTGOING ) );

            if ( relationships.size() == 1 )
            {
                Relationship regeneratedTo = relationships.get( 0 );
                timelord = regeneratedTo.getEndNode();
                regenerationCount++;
            }
            else
            {
                break;
            }
        }
        return regenerationCount;
    }

    @Test
    public void shouldHave8Masters()
    {
        try ( Transaction tx = database.beginTx() )
        {
            int numberOfMasters = 8;
            Node theMaster = universe.getDatabase()
                    .index()
                    .forNodes( "characters" )
                    .get( "character", "Master" )
                    .getSingle();

            assertNotNull( theMaster );
            assertEquals( numberOfMasters, databaseHelper.destructivelyCountRelationships( theMaster.getRelationships(
                    DoctorWhoRelationships.PLAYED, Direction.INCOMING ) ) );
            tx.failure();
        }
    }

    @Test
    public void timelordsShouldComeFromGallifrey()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node gallifrey = getPlanetIndex().get( "planet", "Gallifrey" )
                    .getSingle();
            Node timelord = getSpeciesIndex().get( "species", "Timelord" )
                    .getSingle();
            assertNotNull( gallifrey );
            assertNotNull( timelord );

            Iterable<Relationship> relationships = timelord.getRelationships( DoctorWhoRelationships.COMES_FROM,
                    Direction.OUTGOING );
            List<Relationship> listOfRelationships = databaseHelper.toListOfRelationships( relationships );

            assertEquals( 1, listOfRelationships.size() );
            assertTrue( listOfRelationships.get( 0 )
                    .getEndNode()
                    .equals( gallifrey ) );
            tx.failure();
        }
    }

    @Test
    public void shortestPathBetweenDoctorAndMasterShouldBeLengthOneTypeEnemyOf()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node theMaster = universe.getDatabase()
                    .index()
                    .forNodes( "characters" )
                    .get( "character", "Master" )
                    .getSingle();
            Node theDoctor = universe.getDatabase()
                    .index()
                    .forNodes( "characters" )
                    .get( "character", "Doctor" )
                    .getSingle();

            int maxDepth = 5; // No more than 5, or we find Kevin Bacon!
            PathFinder<Path> shortestPathFinder = GraphAlgoFactory.shortestPath( Traversal.expanderForAllTypes(),
                    maxDepth );

            Path shortestPath = shortestPathFinder.findSinglePath( theDoctor, theMaster );
            assertEquals( 1, shortestPath.length() );
            assertTrue( shortestPath.lastRelationship()
                    .isType( DoctorWhoRelationships.ENEMY_OF ) );
            tx.failure();
        }
    }

    @Test
    public void daleksShouldBeEnemiesOfTheDoctor()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node dalek = getSpeciesIndex().get( "species", "Dalek" )
                    .getSingle();
            assertNotNull( dalek );
            Iterable<Relationship> enemiesOf = dalek.getRelationships( DoctorWhoRelationships.ENEMY_OF,
                    Direction.OUTGOING );
            assertTrue( containsTheDoctor( enemiesOf ) );
            tx.failure();
        }
    }

    @Test
    public void cybermenShouldBeEnemiesOfTheDoctor()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node cyberman = getSpeciesIndex().get( "species", "Cyberman" )
                    .getSingle();
            assertNotNull( cyberman );
            Iterable<Relationship> enemiesOf = cyberman.getRelationships( DoctorWhoRelationships.ENEMY_OF,
                    Direction.OUTGOING );
            assertTrue( containsTheDoctor( enemiesOf ) );
            tx.failure();
        }
    }

    private boolean containsTheDoctor( Iterable<Relationship> enemiesOf )
    {
        Node theDoctor = universe.getDatabase()
                .index()
                .forNodes( "characters" )
                .get( "character", "Doctor" )
                .getSingle();
        for ( Relationship r : enemiesOf )
        {
            if ( r.getEndNode()
                    .equals( theDoctor ) )
            {
                return true;
            }
        }
        return false;
    }

    @Test
    public void shouldFindEnemiesOfTheMastersEnemies()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node theMaster = universe.getDatabase()
                    .index()
                    .forNodes( "characters" )
                    .get( "character", "Master" )
                    .getSingle();
            Node dalek = getSpeciesIndex().get( "species", "Dalek" )
                    .getSingle();
            Node cyberman = getSpeciesIndex().get( "species", "Cyberman" )
                    .getSingle();
            Node silurian = getSpeciesIndex().get( "species", "Silurian" )
                    .getSingle();
            Node sontaran = getSpeciesIndex().get( "species", "Sontaran" )
                    .getSingle();

            Traverser traverser = Traversal.description()
                    .expand( Traversal.expanderForTypes( DoctorWhoRelationships.ENEMY_OF,
                            Direction.OUTGOING ) )
                    .depthFirst()
                    .evaluator( new Evaluator()
                    {
                        public Evaluation evaluate( Path path )
                        {
                            // Only include if we're at depth 2, don't want any mere
                            // enemies
                            if ( path.length() == 2 )
                            {
                                return Evaluation.INCLUDE_AND_PRUNE;
                            }
                            else if ( path.length() > 2 )
                            {
                                return Evaluation.EXCLUDE_AND_PRUNE;
                            }
                            else
                            {
                                return Evaluation.EXCLUDE_AND_CONTINUE;
                            }
                        }
                    } )
                    .uniqueness( Uniqueness.NODE_GLOBAL )
                    .traverse( theMaster );

            Iterable<Node> nodes = traverser.nodes();
            assertNotNull( nodes );

            List<Node> enemiesOfEnemies = databaseHelper.toListOfNodes( nodes );

            int numberOfIndividualAndSpeciesEnemiesInTheDatabase = 152;
            assertEquals( numberOfIndividualAndSpeciesEnemiesInTheDatabase, enemiesOfEnemies.size() );
            assertTrue( isInList( dalek, enemiesOfEnemies ) );
            assertTrue( isInList( cyberman, enemiesOfEnemies ) );
            assertTrue( isInList( silurian, enemiesOfEnemies ) );
            assertTrue( isInList( sontaran, enemiesOfEnemies ) );
            tx.failure();
        }
    }

    private boolean isInList( Node candidateNode, List<Node> listOfNodes )
    {
        for ( Node n : listOfNodes )
        {
            if ( n.equals( candidateNode ) )
            {
                return true;
            }
        }
        return false;
    }

    @Test
    public void shouldBeCorrectNumberOfEnemySpecies()
    {
        try ( Transaction tx = database.beginTx() )
        {
            int numberOfEnemySpecies = 43;
            Node theDoctor = universe.getDatabase()
                    .index()
                    .forNodes( "characters" )
                    .get( "character", "Doctor" )
                    .getSingle();

            Iterable<Relationship> relationships = theDoctor.getRelationships( DoctorWhoRelationships.ENEMY_OF,
                    Direction.INCOMING );
            int enemySpeciesFound = 0;
            for ( Relationship rel : relationships )
            {
                if ( rel.getStartNode()
                        .hasProperty( "species" ) )
                {
                    enemySpeciesFound++;
                }
            }

            assertEquals( numberOfEnemySpecies, enemySpeciesFound );
            tx.failure();
        }
    }

    @Test
    public void shouldHaveCorrectNumberOfCompanionsInTotal()
    {
        try ( Transaction tx = database.beginTx() )
        {

            int numberOfCompanions = 47;

            Node theDoctor = universe.getDatabase()
                    .index()
                    .forNodes( "characters" )
                    .get( "character", "Doctor" )
                    .getSingle();
            assertNotNull( theDoctor );

            assertEquals( numberOfCompanions, databaseHelper.destructivelyCountRelationships( theDoctor
                    .getRelationships(
                            DoctorWhoRelationships.COMPANION_OF, Direction.INCOMING ) ) );
            tx.failure();
        }
    }

    @Test
    public void shouldHaveCorrectNumberofIndividualEnemyCharactersInTotal()
    {
        try ( Transaction tx = database.beginTx() )
        {
            int numberOfEnemies = 110;

            Node theDoctor = universe.getDatabase()
                    .index()
                    .forNodes( "characters" )
                    .get( "character", "Doctor" )
                    .getSingle();
            assertNotNull( theDoctor );

            int count = 0;
            Iterable<Relationship> relationships = theDoctor.getRelationships( DoctorWhoRelationships.ENEMY_OF,
                    Direction.INCOMING );
            for ( Relationship rel : relationships )
            {
                if ( rel.getStartNode()
                        .hasProperty( "character" ) )
                {
                    count++;
                }
            }

            assertEquals( numberOfEnemies, count );
            tx.failure();
        }
    }

    private Index<Node> getActorIndex()
    {
        return database.index()
                .forNodes( "actors" );
    }

    private Index<Node> getPlanetIndex()
    {
        return database.index()
                .forNodes( "planets" );
    }

    private Index<Node> getSpeciesIndex()
    {
        return database.index()
                .forNodes( "species" );
    }

    @Test
    public void severalSpeciesShouldBeEnemies()
    {
        try ( Transaction tx = database.beginTx() )
        {
            assertTrue( areMututalEnemySpecies( "Dalek", "Cyberman" ) );
            assertTrue( areMututalEnemySpecies( "Dalek", "Human" ) );
            assertTrue( areMututalEnemySpecies( "Human", "Auton" ) );
            assertTrue( areMututalEnemySpecies( "Timelord", "Dalek" ) );
            tx.failure();
        }
    }

    private boolean areMututalEnemySpecies( String enemy1, String enemy2 )
    {
        Index<Node> speciesIndex = database.index()
                .forNodes( "species" );

        Node n1 = speciesIndex.get( "species", enemy1 )
                .getSingle();
        Node n2 = speciesIndex.get( "species", enemy2 )
                .getSingle();

        return isEnemyOf( n1, n2 ) && isEnemyOf( n2, n1 );
    }

    private boolean isEnemyOf( Node n1, Node n2 )
    {
        for ( Relationship r : n1.getRelationships( DoctorWhoRelationships.ENEMY_OF, Direction.OUTGOING ) )
        {
            if ( r.getEndNode()
                    .equals( n2 ) )
            {
                return true;
            }
        }
        return false;
    }
}
