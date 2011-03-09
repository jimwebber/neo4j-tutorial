package org.neo4j.tutorial;

import static org.junit.Assert.*;

import org.junit.Test;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

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
            theDoctor.setProperty("firstname", "William");
            theDoctor.setProperty("lastname", "Hartnell");

            susan = db.createNode();
            theDoctor.setProperty("firstname", "Susan");
            theDoctor.setProperty("lastname", "Campbell");

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
}
