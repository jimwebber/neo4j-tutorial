package org.neo4j.tutorial;

import static org.junit.Assert.*;

import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class LessonOne extends Koan {
    
    @Test
    public void shouldCreateANodeInTheDatabase() {        
        Node node = null;

        //SNIPPET_START
        
        Transaction tx = db.beginTx();
        try {
            node = db.createNode();
            tx.success();
        } finally {
            tx.finish();
        }
        
        //SNIPPET_END
        
        assertTrue(DatabaseHelper.nodeExistsInDatabase(db, node));
    }
    
    @Test
    public void shouldCreateSomePropertiesOnANode() {
        Node node = null;

        //SNIPPET_START
        
        Transaction tx = db.beginTx();
        try {
            node = db.createNode();
            node.setProperty("firstname", "William");
            node.setProperty("lastname", "Hartnell");
            tx.success();
        } finally {
            tx.finish();
        }
        
        //SNIPPET_END
        
        assertTrue(DatabaseHelper.nodeExistsInDatabase(db, node));
        
        Node storedNode = db.getNodeById(node.getId());
        assertEquals("William", storedNode.getProperty("firstname"));
        assertEquals("Hartnell", storedNode.getProperty("lastname"));
    }
}
