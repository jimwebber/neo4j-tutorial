package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This first programming Koan will get you started with the basics of managing
 * nodes and relationships with the core API.
 * <p/>
 * We'll also have think about transaction semantics (read uncommitted!)
 * and neo4j's caching infrastructure
 * <p/>
 * It will also introduce you to the earliest Doctor Who storylines!
 */
public class Koan02
{

    private static GraphDatabaseService database;
    private static DatabaseHelper databaseHelper;


    @BeforeClass
    public static void createADatabase()
    {
        database = DatabaseHelper.createDatabase();
        databaseHelper = new DatabaseHelper( database );
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        database.shutdown();
        database = null;
    }

    @Test
    public void shouldCreateANodeInTheDatabase()
    {
        Node node = null;

        // HINT: mutation operations need to be wrapped in transactions
        // db.beginTx()
        // tx.success()/tx.fail()
        // tx.finish()

        // YOUR CODE GOES HERE
        // SNIPPET_START


        try ( Transaction tx = database.beginTx() )
        {
            node = database.createNode();
            tx.success();
        }

        // SNIPPET_END

        assertTrue( databaseHelper.nodeExistsInDatabase( node ) );
    }

    @Test
    public void shouldCreateSomePropertiesOnANode()
    {
        Node theDoctor = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        try ( Transaction tx = database.beginTx() )
        {
            theDoctor = database.createNode();
            theDoctor.setProperty( "firstname", "William" );
            theDoctor.setProperty( "lastname", "Hartnell" );
            tx.success();
        }

        // SNIPPET_END

        try ( Transaction tx = database.beginTx() )
        {
            assertTrue( databaseHelper.nodeExistsInDatabase( theDoctor ) );

            Node storedNode = database.getNodeById( theDoctor.getId() );
            assertEquals( "William", storedNode.getProperty( "firstname" ) );
            assertEquals( "Hartnell", storedNode.getProperty( "lastname" ) );

            tx.success();
        }
    }

    @Test
    public void shouldRelateTwoNodes()
    {
        Node theDoctor = null;
        Node susan = null;
        Relationship companionRelationship = null;

        // HINT: you can create a new RelationshipsType with
        // DynamicRelationshipType.withName(name) or use the precanned Types for this tutorial
        // e.g. DoctorWhoRelationships.COMPANION_OF

        // YOUR CODE GOES HERE
        // SNIPPET_START

        try ( Transaction tx = database.beginTx() )
        {
            theDoctor = database.createNode();
            theDoctor.setProperty( "character", "Doctor" );

            susan = database.createNode();
            susan.setProperty( "firstname", "Susan" );
            susan.setProperty( "lastname", "Campbell" );

            companionRelationship = susan.createRelationshipTo( theDoctor,
                    DoctorWhoRelationships.COMPANION_OF );

            tx.success();
        }

        // SNIPPET_END

        try ( Transaction tx = database.beginTx() )
        {
            Relationship storedCompanionRelationship = database.getRelationshipById( companionRelationship.getId() );
            assertNotNull( storedCompanionRelationship );
            assertNotNull( storedCompanionRelationship.getType().equals( DoctorWhoRelationships.COMPANION_OF ) );
            assertEquals( susan, storedCompanionRelationship.getStartNode() );
            assertEquals( theDoctor, storedCompanionRelationship.getEndNode() );
            tx.success();
        }
    }

    @Test
    public void shouldRemoveStarTrekInformation()
    {
        /* Captain Kirk has no business being in our database, so set phasers to kill */
        Node captainKirk = createPollutedDatabaseContainingStarTrekReferences();

        // YOUR CODE GOES HERE
        // SNIPPET_START

        try ( Transaction tx = database.beginTx() )
        {

            // This is the tricky part, you have to remove the active
            // relationships before you can remove a node
            Iterable<Relationship> relationships = captainKirk.getRelationships();
            for ( Relationship r : relationships )
            {
                r.delete();
            }

            captainKirk.delete();

            tx.success();
        }

        // SNIPPET_END

        try
        {
            captainKirk.hasProperty( "character" );
            fail();
        }
        catch ( Exception e )
        {
            // If the exception is thrown, we've removed Captain Kirk from the
            // database
            assertNotNull( e );
        }
    }

    @Test
    public void shouldRemoveIncorrectEnemyOfRelationshipBetweenSusanAndTheDoctor()
    {
        Node susan = createInaccurateDatabaseWhereSusanIsEnemyOfTheDoctor();

        // YOUR CODE GOES HERE
        // SNIPPET_START

        try ( Transaction tx = database.beginTx() )
        {
            Iterable<Relationship> relationships = susan.getRelationships( DoctorWhoRelationships.ENEMY_OF,
                    Direction.OUTGOING );
            for ( Relationship r : relationships )
            {
                Node n = r.getEndNode();
                if ( n.hasProperty( "character" ) && n.getProperty( "character" )
                        .equals( "Doctor" ) )
                {
                    r.delete();
                }
            }

            tx.success();
        }

        // SNIPPET_END

        try ( Transaction tx = database.beginTx() )
        {
            assertEquals( 1, databaseHelper.destructivelyCountRelationships( susan.getRelationships() ) );
            tx.success();
        }
    }

    private Node createInaccurateDatabaseWhereSusanIsEnemyOfTheDoctor()
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node theDoctor = database.createNode();
            theDoctor.setProperty( "character", "Doctor" );

            Node susan = database.createNode();
            susan.setProperty( "firstname", "Susan" );
            susan.setProperty( "lastname", "Campbell" );

            susan.createRelationshipTo( theDoctor, DoctorWhoRelationships.COMPANION_OF );
            susan.createRelationshipTo( theDoctor, DoctorWhoRelationships.ENEMY_OF );

            tx.success();
            return susan;
        }
    }

    private Node createPollutedDatabaseContainingStarTrekReferences()
    {
        Node captainKirk = null;
        try ( Transaction tx = database.beginTx() )
        {
            Node theDoctor = database.createNode();
            theDoctor.setProperty( "character", "The Doctor" );

            captainKirk = database.createNode();
            captainKirk.setProperty( "firstname", "James" );
            captainKirk.setProperty( "initial", "T" );
            captainKirk.setProperty( "lastname", "Kirk" );

            captainKirk.createRelationshipTo( theDoctor, DoctorWhoRelationships.COMPANION_OF );
            captainKirk.createRelationshipTo( theDoctor, DoctorWhoRelationships.CHILD_OF );

            tx.success();
            return captainKirk;
        }
    }
}
