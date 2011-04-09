package org.neo4j.tutorial;

import java.io.File;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

public class Friendlies extends Characters {

    private Map<String, Map<String, String>> friendliesMap;

    @SuppressWarnings("unchecked")
    public Friendlies(File data) {
        ObjectMapper m = new ObjectMapper();
        try {
            friendliesMap = m.readValue(data, Map.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void insertAndIndex(GraphDatabaseService db, Index<Node> friendlyIndex, Index<Node> speciesIndex, Index<Node> planetIndex) {
        Transaction tx = db.beginTx();
        try {
            for (String friendlyName : friendliesMap.keySet()) {
                Node friendlyNode = db.createNode();
                friendlyNode.setProperty("name", friendlyName);
                friendlyIndex.add(friendlyNode, "name", friendlyName);

                connectToSpecies(friendlyNode, friendliesMap.get(friendlyName), speciesIndex);
                Species.connectToHomeworld(friendlyNode, friendliesMap.get(friendlyName), planetIndex);
            }

            tx.success();
        } finally {
            tx.finish();
        }
    }
}
