package org.neo4j.tutorial;

import java.io.File;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

public class Planets {

    private List<String> planetsList;

    @SuppressWarnings("unchecked")
    public Planets(File data) {
        ObjectMapper m = new ObjectMapper();
        try {
            planetsList = m.readValue(data, List.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void insertAndIndex(GraphDatabaseService db, Index<Node> planetIndex) {
        Transaction tx = db.beginTx();
        try {
            for (String planetName : planetsList) {
                Node planetNode = db.createNode();
                planetNode.setProperty("planet", planetName);
                planetIndex.add(planetNode, "planet", planetName);
            }
            tx.success();
        } finally {
            tx.finish();
        }
    }
}
