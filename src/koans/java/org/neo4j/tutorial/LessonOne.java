package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

/**
 * This first Koan will get you started with the basics of managing nodes and relationships with the core API.
 * It will also introduce you to the earliest Doctor Who storylines! 
 */
public class LessonOne extends Koan {

    @Test
    public void shouldCreateANodeInTheDatabase() {
        Node node = null;

        // SNIPPET_START

        Transaction tx = db.beginTx();
        try {
            node = db.createNode();
            tx.success();
        } finally {
            tx.finish();
        }

        // SNIPPET_END

        assertTrue(DatabaseHelper.nodeExistsInDatabase(db, node));
    }

    @Test
    public void shouldCreateSomePropertiesOnANode() {
        Node theDoctor = null;

        // SNIPPET_START

        Transaction tx = db.beginTx();
        try {
            theDoctor = db.createNode();
            theDoctor.setProperty("firstname", "William");
            theDoctor.setProperty("lastname", "Hartnell");
            tx.success();
        } finally {
            tx.finish();
        }

        // SNIPPET_END

        assertTrue(DatabaseHelper.nodeExistsInDatabase(db, theDoctor));

        Node storedNode = db.getNodeById(theDoctor.getId());
        assertEquals("William", storedNode.getProperty("firstname"));
        assertEquals("Hartnell", storedNode.getProperty("lastname"));
    }

    @Test
    public void shouldRelateTwoNodes() {
        Node theDoctor = null;
        Node susan = null;
        Relationship companionRelationship = null;

        // SNIPPET_START

        Transaction tx = db.beginTx();
        try {
            theDoctor = db.createNode();
            theDoctor.setProperty("name", "The Doctor");

            susan = db.createNode();
            susan.setProperty("firstname", "Susan");
            susan.setProperty("lastname", "Campbell");

            companionRelationship = susan.createRelationshipTo(theDoctor, DynamicRelationshipType.withName("COMPANION_OF"));

            tx.success();
        } finally {
            tx.finish();
        }

        // SNIPPET_END
        
        Relationship storedCompanionRelationship = db.getRelationshipById(companionRelationship.getId());
        assertNotNull(storedCompanionRelationship);
        assertEquals(susan, storedCompanionRelationship.getStartNode());
        assertEquals(theDoctor, storedCompanionRelationship.getEndNode());
    }
    
    @Test
    public void shouldRemoveStarTrekInformation() {
        /* Captain Kirk has no business being in our database, so set phasers to kill */
        Node captainKirk = createPollutedDatabaseContainingStarTrekReferences();
        
        // SNIPPET_START

        Transaction tx = db.beginTx();
        try {
            
            // This is the tricky part, you have to remove the active relationships before you can remove a node
            Iterable<Relationship> relationships = captainKirk.getRelationships();
            for(Relationship r : relationships) {
                r.delete();
            }
            
            captainKirk.delete();

            tx.success();
        } finally {
            tx.finish();
        }

        // SNIPPET_END
        
        try {
            db.getNodeById(captainKirk.getId());
            fail();
        } catch(NotFoundException nfe) {
            // If the exception is thrown, we've removed Captain Kirk from the database
            assertNotNull(nfe);
        }
    }
    
    
    @Test
    public void shouldRemoveIncorrectEnemyOfRelationshipBetweenSusanAndTheDoctor() {
        Node susan = createInaccurateDatabaseWhereSusanIsEnemyOfTheDoctor();
        
        Transaction tx = db.beginTx();
        try {
            
            Iterable<Relationship> relationships = susan.getRelationships(DynamicRelationshipType.withName("ENEMY_OF"));
            for(Relationship r : relationships) {
                Node n = r.getEndNode();
                if(n.hasProperty("name") && n.getProperty("name").equals("The Doctor")) {
                   r.delete(); 
                }
            }

            tx.success();
        } finally {
            tx.finish();
        }

        // SNIPPET_END
        
        assertEquals(1, DatabaseHelper.countRelationships(susan.getRelationships()));
    }

    private Node createInaccurateDatabaseWhereSusanIsEnemyOfTheDoctor() {
        Transaction tx = db.beginTx();
        Node susan = null;
        try {
            Node theDoctor = db.createNode();
            theDoctor.setProperty("name", "The Doctor");

            susan = db.createNode();
            susan.setProperty("firstname", "Susan");
            susan.setProperty("lastname", "Campbell");

            susan.createRelationshipTo(theDoctor, DynamicRelationshipType.withName("COMPANION_OF"));
            susan.createRelationshipTo(theDoctor, DynamicRelationshipType.withName("ENEMY_OF"));

            tx.success();
            return susan;
        } finally {
            tx.finish();
        }
        
    }

    private Node createPollutedDatabaseContainingStarTrekReferences() {
        Transaction tx = db.beginTx();
        Node captainKirk = null;
        try {
            Node theDoctor = db.createNode();
            theDoctor.setProperty("name", "The Doctor");

            captainKirk = db.createNode();
            captainKirk.setProperty("firstname", "James");
            captainKirk.setProperty("initial", "T");
            captainKirk.setProperty("lastname", "Kirk");

            captainKirk.createRelationshipTo(theDoctor, DynamicRelationshipType.withName("COMPANION_OF"));

            tx.success();
            return captainKirk;
        } finally {
            tx.finish();
        }
    }
    
}
