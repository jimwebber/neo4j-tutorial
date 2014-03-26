package org.neo4j.tutorial;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.neo4j.graphdb.Direction.INCOMING;
import static org.neo4j.graphdb.Direction.OUTGOING;
import static org.neo4j.graphdb.traversal.Evaluation.EXCLUDE_AND_CONTINUE;
import static org.neo4j.graphdb.traversal.Evaluation.EXCLUDE_AND_PRUNE;
import static org.neo4j.graphdb.traversal.Evaluation.INCLUDE_AND_PRUNE;
import static org.neo4j.graphdb.traversal.Uniqueness.NODE_GLOBAL;
import static org.neo4j.helpers.collection.IteratorUtil.asList;
import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;
import static org.neo4j.tutorial.DoctorWhoLabels.ACTOR;
import static org.neo4j.tutorial.DoctorWhoLabels.CHARACTER;
import static org.neo4j.tutorial.DoctorWhoLabels.EPISODE;
import static org.neo4j.tutorial.DoctorWhoLabels.PLANET;
import static org.neo4j.tutorial.DoctorWhoLabels.SPECIES;
import static org.neo4j.tutorial.DoctorWhoRelationships.COMES_FROM;
import static org.neo4j.tutorial.DoctorWhoRelationships.COMPANION_OF;
import static org.neo4j.tutorial.DoctorWhoRelationships.ENEMY_OF;
import static org.neo4j.tutorial.DoctorWhoRelationships.IS_A;
import static org.neo4j.tutorial.DoctorWhoRelationships.NEXT;
import static org.neo4j.tutorial.DoctorWhoRelationships.PLAYED;
import static org.neo4j.tutorial.DoctorWhoRelationships.PREVIOUS;
import static org.neo4j.tutorial.DoctorWhoRelationships.REGENERATED_TO;

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
    private static ExecutionEngine engine;

    @BeforeClass
    public static void startDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator().getDatabase() );
        database = universe.getDatabase();
        databaseHelper = new DatabaseHelper( database );
        engine = new ExecutionEngine( database, DEV_NULL );
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
            Node ep = getNodeFromDatabase( EPISODE, "1" );


            int count = 1;
            while ( ep.hasRelationship( NEXT, OUTGOING ) )
            {
                ep = ep.getSingleRelationship( NEXT, OUTGOING ).getEndNode();
                count++;
            }

            ExecutionResult result = engine.execute( "MATCH (ep:Episode) RETURN count(ep) AS episodeCount" );
            assertEquals( count, Integer.valueOf( result.columnAs( "episodeCount" ).next().toString() ).intValue() );

            while ( ep.hasRelationship( PREVIOUS, OUTGOING ) )
            {
                ep = ep.getSingleRelationship( PREVIOUS, OUTGOING ).getEndNode();
                count--;
            }

            assertEquals( 1, count );
            tx.failure();
        }
    }

    @SuppressWarnings("unused")
    @Test
    public void shouldHaveCorrectNumberOfPlanets()
    {
        try ( Transaction tx = database.beginTx() )
        {
            final ExecutionResult result = engine.execute( "MATCH (p:Planet) RETURN count(p) AS planetCount" );

            int numberOfPlanetsMentionedInTVEpisodes = 447;
            assertEquals( numberOfPlanetsMentionedInTVEpisodes, Integer.valueOf( result.columnAs( "planetCount" )
                    .next().toString() ).intValue() );
            tx.success();
        }
    }

    @Test
    public void theDoctorsRegenerationsShouldBeDated()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node nextDoctor = getNodeFromDatabase( ACTOR, "William Hartnell" );

            boolean allDoctorsHaveRegenerationYears = true;

            do
            {
                Relationship relationship = nextDoctor.getSingleRelationship( REGENERATED_TO, OUTGOING );
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
            Node humanSpeciesNode = getNodeFromDatabase( SPECIES, "Human" );

            int numberOfHumansFriendliesInTheDB = databaseHelper.destructivelyCountRelationships(
                    humanSpeciesNode.getRelationships( IS_A, INCOMING ) );

            int knownNumberOfHumans = 57;
            assertEquals( knownNumberOfHumans, numberOfHumansFriendliesInTheDB );
            tx.failure();
        }
    }

    @Test
    public void shouldBe8TimelordsInTheUniverse()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node timelordSpeciesNode = getNodeFromDatabase( SPECIES, "Timelord" );

            int numberOfTimelordsInTheDb = databaseHelper.destructivelyCountRelationships(
                    timelordSpeciesNode.getRelationships(
                            IS_A, INCOMING ) );

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
            int numberOfSpecies = 55;

            ExecutionResult result = engine.execute( "MATCH (s:Species) RETURN count(s) AS speciesCount" );
            assertEquals( numberOfSpecies, Integer.valueOf( result.columnAs( "speciesCount" ).next().toString() )
                    .intValue() );

            tx.failure();
        }
    }

    @Test
    public void shouldHave12ActorsThatHavePlayedTheDoctor()
    {
        try ( Transaction tx = database.beginTx() )
        {
            int numberOfDoctors = 13; // 13 Because the first doctor was played by 2
            // actors over the course of the franchise

            Node theDoctor = universe.theDoctor();

            assertNotNull( theDoctor );
            assertEquals( numberOfDoctors, databaseHelper.destructivelyCountRelationships(
                    theDoctor.getRelationships( PLAYED, INCOMING ) ) );
            tx.success();
        }
    }

    @Test
    public void shouldBeTenRegenerationRelationshipsBetweenTheElevenDoctors()
    {
        try ( Transaction tx = database.beginTx() )
        {
            int numberOfDoctorsRegenerations = 12;

            Node firstDoctor = database.findNodesByLabelAndProperty( ACTOR, "actor",
                    "William Hartnell" ).iterator().next();
            assertNotNull( firstDoctor );

            assertEquals( numberOfDoctorsRegenerations,
                    countOutgoingRegeneratedToRelationshipsStartingWith( firstDoctor ) );

            tx.failure();
        }
    }

    @Test
    public void shouldBeSevenRegenerationRelationshipsBetweenTheEightMasters()
    {
        try ( Transaction tx = database.beginTx() )
        {
            int numberOfMastersRegenerations = 7;

            final ResourceIterable<Node> nodes = database.findNodesByLabelAndProperty( ACTOR, "actor",
                    "Roger Delgado" );

            assertEquals( 1, databaseHelper.destructivelyCount( nodes ) );

            Node currentMaster = database.findNodesByLabelAndProperty( ACTOR, "actor",
                    "Roger Delgado" ).iterator().next();

            assertEquals( numberOfMastersRegenerations, countOutgoingRegeneratedToRelationshipsStartingWith(
                    currentMaster ) );

            tx.failure();
        }
    }

    private int countOutgoingRegeneratedToRelationshipsStartingWith( Node timelord )
    {
        int regenerationCount = 0;
        while ( true )
        {
            List<Relationship> relationships = asList( timelord.getRelationships(
                    REGENERATED_TO, OUTGOING ) );

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
            Node theMaster = getNodeFromDatabase( CHARACTER, "Master" );

            assertNotNull( theMaster );
            assertEquals( numberOfMasters, databaseHelper.destructivelyCountRelationships(
                    theMaster.getRelationships( PLAYED, INCOMING ) ) );
            tx.failure();
        }
    }

    @Test
    public void timelordsShouldComeFromGallifrey()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node gallifrey = getNodeFromDatabase( PLANET, "Gallifrey" );
            Node timelord = getNodeFromDatabase( SPECIES, "Timelord" );

            assertNotNull( gallifrey );
            assertNotNull( timelord );

            Iterable<Relationship> relationships = timelord.getRelationships( COMES_FROM, OUTGOING );
            List<Relationship> listOfRelationships = asList( relationships );

            assertEquals( 1, listOfRelationships.size() );
            assertTrue( listOfRelationships.get( 0 )
                    .getEndNode()
                    .equals( gallifrey ) );
            tx.failure();
        }
    }

    private Node getNodeFromDatabase( Label label, String value )
    {
        return database.findNodesByLabelAndProperty( label, label.name().toLowerCase(), value ).iterator().next();
    }

    @Test
    public void shortestPathBetweenDoctorAndMasterShouldBeLengthOneTypeEnemyOf()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node theMaster = getNodeFromDatabase( CHARACTER, "Master" );
            Node theDoctor = universe.theDoctor();

            int maxDepth = 5; // No more than 5, or we find Kevin Bacon!
            PathFinder<Path> shortestPathFinder = GraphAlgoFactory.shortestPath( Traversal.expanderForAllTypes(),
                    maxDepth );

            Path shortestPath = shortestPathFinder.findSinglePath( theDoctor, theMaster );
            assertEquals( 1, shortestPath.length() );
            assertTrue( shortestPath.lastRelationship()
                    .isType( ENEMY_OF ) );
            tx.failure();
        }
    }

    @Test
    public void daleksShouldBeEnemiesOfTheDoctor()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node dalek = getNodeFromDatabase( SPECIES, "Dalek" );
            assertNotNull( dalek );

            Iterable<Relationship> enemiesOf = dalek.getRelationships( ENEMY_OF,
                    OUTGOING );
            assertTrue( containsTheDoctor( enemiesOf ) );
            tx.failure();
        }
    }

    @Test
    public void cybermenShouldBeEnemiesOfTheDoctor()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node cyberman = getNodeFromDatabase( SPECIES, "Cyberman" );
            assertNotNull( cyberman );
            Iterable<Relationship> enemiesOf = cyberman.getRelationships( ENEMY_OF, OUTGOING );
            assertTrue( containsTheDoctor( enemiesOf ) );
            tx.failure();
        }
    }

    private boolean containsTheDoctor( Iterable<Relationship> enemiesOf )
    {
        Node theDoctor = universe.theDoctor();

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
            Node theMaster = getNodeFromDatabase( CHARACTER, "Master" );

            Traverser traverser = Traversal.description()
                    .expand( Traversal.expanderForTypes( ENEMY_OF, OUTGOING ) )
                    .depthFirst()
                    .evaluator( new Evaluator()
                    {
                        public Evaluation evaluate( Path path )
                        {
                            // Only include if we're at depth 2, don't want any mere
                            // enemies
                            if ( path.length() == 2 )
                            {
                                return INCLUDE_AND_PRUNE;
                            }
                            else if ( path.length() > 2 )
                            {
                                return EXCLUDE_AND_PRUNE;
                            }
                            else
                            {
                                return EXCLUDE_AND_CONTINUE;
                            }
                        }
                    } )
                    .uniqueness( NODE_GLOBAL )
                    .traverse( theMaster );

            Iterable<Node> nodes = traverser.nodes();
            assertNotNull( nodes );


            List<Node> enemiesOfEnemies = asList( nodes );

            int numberOfIndividualAndSpeciesEnemiesInTheDatabase = 148;
            assertEquals( numberOfIndividualAndSpeciesEnemiesInTheDatabase, enemiesOfEnemies.size() );

            assertTrue( isInList( getNodeFromDatabase( SPECIES, "Dalek" ), enemiesOfEnemies ) );
            assertTrue( isInList( getNodeFromDatabase( SPECIES, "Cyberman" ), enemiesOfEnemies ) );
            assertTrue( isInList( getNodeFromDatabase( SPECIES, "Silurian" ), enemiesOfEnemies ) );
            assertTrue( isInList( getNodeFromDatabase( SPECIES, "Sontaran" ), enemiesOfEnemies ) );
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
            Node theDoctor = universe.theDoctor();

            Iterable<Relationship> relationships = theDoctor.getRelationships( ENEMY_OF, INCOMING );
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

            Node theDoctor = universe.theDoctor();

            assertNotNull( theDoctor );

            assertEquals( numberOfCompanions, databaseHelper.destructivelyCountRelationships( theDoctor
                    .getRelationships(
                            COMPANION_OF, INCOMING ) ) );
            tx.failure();
        }
    }

    @Test
    public void shouldHaveCorrectNumberofIndividualEnemyCharactersInTotal()
    {
        try ( Transaction tx = database.beginTx() )
        {
            int numberOfEnemies = 113;

            Node theDoctor = universe.theDoctor();

            assertNotNull( theDoctor );

            int count = 0;
            Iterable<Relationship> relationships = theDoctor.getRelationships( ENEMY_OF, INCOMING );
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
        Node n1 = getNodeFromDatabase( SPECIES, enemy1 );

        Node n2 = getNodeFromDatabase( SPECIES, enemy2 );

        return isEnemyOf( n1, n2 ) && isEnemyOf( n2, n1 );
    }

    private boolean isEnemyOf( Node n1, Node n2 )
    {
        for ( Relationship r : n1.getRelationships( ENEMY_OF, OUTGOING ) )
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
