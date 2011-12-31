package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.*;

import static org.junit.Assert.*;

/**
 * This first programming Koan will get you started with the basics of managing
 * nodes and relationships with the core API. It will also introduce you to the
 * earliest Doctor Who storylines!
 */
public class Koan02
{

    private static GraphDatabaseService db;
    private static DatabaseHelper databaseHelper;

    @BeforeClass
    public static void createADatabase()
    {
        db = DatabaseHelper.createDatabase();
        databaseHelper = new DatabaseHelper(db);
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        db.shutdown();
    }

    @Test
    public void shouldCreateANodeInTheDatabase()
    {
        Node node = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        Transaction tx = db.beginTx();
        try
        {
            node = db.createNode();
            tx.success();
        } finally
        {
            tx.finish();
        }

        // SNIPPET_END

        assertTrue(databaseHelper.nodeExistsInDatabase(node));
    }

    @Test
    public void shouldCreateSomePropertiesOnANode()
    {
        Node theDoctor = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        Transaction tx = db.beginTx();
        try
        {
            theDoctor = db.createNode();
            theDoctor.setProperty("firstname", "William");
            theDoctor.setProperty("lastname", "Hartnell");
            tx.success();
        } finally
        {
            tx.finish();
        }

        // SNIPPET_END

        assertTrue(databaseHelper.nodeExistsInDatabase(theDoctor));

        Node storedNode = db.getNodeById(theDoctor.getId());
        assertEquals("William", storedNode.getProperty("firstname"));
        assertEquals("Hartnell", storedNode.getProperty("lastname"));
    }

    @Test
    public void shouldRelateTwoNodes()
    {
        Node theDoctor = null;
        Node susan = null;
        Relationship companionRelationship = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        Transaction tx = db.beginTx();
        try
        {
            theDoctor = db.createNode();
            theDoctor.setProperty("character", "Doctor");

            susan = db.createNode();
            susan.setProperty("firstname", "Susan");
            susan.setProperty("lastname", "Campbell");

            companionRelationship = susan.createRelationshipTo(theDoctor,
                                                               DoctorWhoRelationships.COMPANION_OF);

            tx.success();
        } finally
        {
            tx.finish();
        }

        // SNIPPET_END

        Relationship storedCompanionRelationship = db.getRelationshipById(companionRelationship.getId());
        assertNotNull(storedCompanionRelationship);
        assertEquals(susan, storedCompanionRelationship.getStartNode());
        assertEquals(theDoctor, storedCompanionRelationship.getEndNode());
    }

    @Test
    public void shouldRemoveStarTrekInformation()
    {
        /* Captain Kirk has no business being in our database, so set phasers to kill */
        Node captainKirk = createPollutedDatabaseContainingStarTrekReferences();

        // YOUR CODE GOES HERE
        // SNIPPET_START

        Transaction tx = db.beginTx();
        try
        {

            // This is the tricky part, you have to remove the active
            // relationships before you can remove a node
            Iterable<Relationship> relationships = captainKirk.getRelationships();
            for (Relationship r : relationships)
            {
                r.delete();
            }

            captainKirk.delete();

            tx.success();
        } finally
        {
            tx.finish();
        }

        // SNIPPET_END

        try
        {
            captainKirk.hasProperty("character");
            fail();
        } catch (NotFoundException nfe)
        {
            // If the exception is thrown, we've removed Captain Kirk from the
            // database
            assertNotNull(nfe);
        }
    }

    @Test
    public void shouldRemoveIncorrectEnemyOfRelationshipBetweenSusanAndTheDoctor()
    {
        Node susan = createInaccurateDatabaseWhereSusanIsEnemyOfTheDoctor();

        // YOUR CODE GOES HERE
        // SNIPPET_START

        Transaction tx = db.beginTx();
        try
        {

            Iterable<Relationship> relationships = susan.getRelationships(DoctorWhoRelationships.ENEMY_OF,
                                                                          Direction.OUTGOING);
            for (Relationship r : relationships)
            {
                Node n = r.getEndNode();
                if (n.hasProperty("character") && n.getProperty("character")
                                                   .equals("The Doctor"))
                {
                    r.delete();
                }
            }

            tx.success();
        } finally
        {
            tx.finish();
        }

        // SNIPPET_END
        assertEquals(1, databaseHelper.destructivelyCountRelationships(susan.getRelationships()));
    }

    private Node createInaccurateDatabaseWhereSusanIsEnemyOfTheDoctor()
    {
        Transaction tx = db.beginTx();
        Node susan = null;
        try
        {
            Node theDoctor = db.createNode();
            theDoctor.setProperty("character", "The Doctor");

            susan = db.createNode();
            susan.setProperty("firstname", "Susan");
            susan.setProperty("lastname", "Campbell");

            susan.createRelationshipTo(theDoctor, DoctorWhoRelationships.COMPANION_OF);
            susan.createRelationshipTo(theDoctor, DoctorWhoRelationships.ENEMY_OF);

            tx.success();
            return susan;
        } finally
        {
            tx.finish();
        }

    }

    private Node createPollutedDatabaseContainingStarTrekReferences()
    {
        Transaction tx = db.beginTx();
        Node captainKirk = null;
        try
        {
            Node theDoctor = db.createNode();
            theDoctor.setProperty("character", "The Doctor");

            captainKirk = db.createNode();
            captainKirk.setProperty("firstname", "James");
            captainKirk.setProperty("initial", "T");
            captainKirk.setProperty("lastname", "Kirk");

            captainKirk.createRelationshipTo(theDoctor, DynamicRelationshipType.withName("COMPANION_OF"));

            tx.success();
            return captainKirk;
        } finally
        {
            tx.finish();
        }
    }
}
