package org.neo4j.tutorial;

import static org.junit.Assert.*;

import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class LessonOne extends Koan {
    
    @Test
    public void shouldCreateANodeInTheDatabase() {        
        Node node = null;

        Transaction tx = db.beginTx();
        try {
            node = db.createNode();
            tx.success();
        } finally {
            tx.finish();
        }
        
        assertNotNull(node);
        assertEquals(2, DatabaseHelper.countNodes(db.getAllNodes()));
    }
    
    @Test
    public void shouldCreateSomePropertiesOnANode() {
        Node node = null;

        Transaction tx = db.beginTx();
        try {
            node = db.createNode();
            node.setProperty("firstname", "William");
            node.setProperty("lastname", "Hartnell");
            tx.success();
        } finally {
            tx.finish();
        }
        
        assertNotNull(node);
        assertEquals(2, DatabaseHelper.countNodes(db.getAllNodes()));
    }
}
