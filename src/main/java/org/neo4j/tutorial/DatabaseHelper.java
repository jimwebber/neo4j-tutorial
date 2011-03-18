package org.neo4j.tutorial;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.index.IndexHits;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class DatabaseHelper<T> {

    public static GraphDatabaseService createDatabase() {

        return new EmbeddedGraphDatabase(createTempDatabaseDir().getAbsolutePath());
    }

    private static File createTempDatabaseDir() {

        File d;
        try {
            d = File.createTempFile("neo4j-test", "dir");
            System.out.println(String.format("Created a new Neo4j database at [%s]", d.getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!d.delete()) {
            throw new RuntimeException("temp config directory pre-delete failed");
        }
        if (!d.mkdirs()) {
            throw new RuntimeException("temp config directory not created");
        }
        d.deleteOnExit();
        return d;
    }
    
    
    public static void dumpGraphToConsole(GraphDatabaseService db) {
        for(Node n : db.getAllNodes()) {
            Iterable<String> propertyKeys = n.getPropertyKeys();
            for(String key : propertyKeys) {
                System.out.print(key + " : " );
                System.out.println(n.getProperty(key));
            }
        }
    }

    public static int countNodesWithAllGivenProperties(Iterable<Node> allNodes, String... propertyNames) {
        Iterator<Node> iterator = allNodes.iterator();
        int count = 0;
        while(iterator.hasNext()) {
            Node next = iterator.next();
            
            boolean hasAllPropertyNames = true;
            for(String propertyName : propertyNames) {
                hasAllPropertyNames = hasAllPropertyNames && next.hasProperty(propertyName);
                if(!hasAllPropertyNames) break; // Modest optimisation
            }
            if(hasAllPropertyNames) {
                count ++;
            }
        }
        return count;
    }
    
    public static int countNodes(Iterable<Node> allNodes) {
        return destructivelyCount(allNodes);
    }

    public static boolean nodeExistsInDatabase(GraphDatabaseService db, Node node) {
        return db.getNodeById(node.getId()) != null;
    }

    public static int countRelationships(Iterable<Relationship> relationships) {
        return destructivelyCount(relationships);
    }

    public static void dumpNode(Node node) {
        for(String key : node.getPropertyKeys()) {
            System.out.println(String.format("Node ID [%d]", node.getId()));
            System.out.print(key + " : ");
            System.out.println(node.getProperty(key));
        }
    }

    public static List<Relationship> toList(Iterable<Relationship> relationshipsIterable) {
        ArrayList<Relationship> relationships = new ArrayList<Relationship>();
        
        for(Relationship r : relationshipsIterable) {
            relationships.add(r);
        }
        
        return relationships;
    }

    public static int count(IndexHits<Node> indexHits) {
        return destructivelyCount(indexHits);
    }
    
    private static int destructivelyCount(Iterable<?> iterable) {
        int count = 0;
        
        for(@SuppressWarnings("unused") Object o : iterable) {
            count ++;
        }
        
        return count;
    }
}
