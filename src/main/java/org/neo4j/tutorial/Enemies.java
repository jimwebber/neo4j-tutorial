package org.neo4j.tutorial;

import java.io.File;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

public class Enemies extends Characters {

    private Map<String, Map<String, String>> enemiesMap;
    private final Node theDoctor;

    @SuppressWarnings("unchecked")
    public Enemies(Node theDoctor, File data) {
        this.theDoctor = theDoctor;
        ObjectMapper m = new ObjectMapper();
        try {
            enemiesMap = m.readValue(data, Map.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void insertAndIndex(GraphDatabaseService db, Index<Node> enemiesIndex, Index<Node> speciesIndex, Index<Node> planetIndex) {
        Transaction tx = db.beginTx();
        try {
            for (String friendlyName : enemiesMap.keySet()) {
                Node enemyNode = db.createNode();
                enemyNode.setProperty("name", friendlyName);
                enemiesIndex.add(enemyNode, "name", friendlyName);

                connectToSpecies(enemyNode, enemiesMap.get(friendlyName), speciesIndex);
                Species.connectToHomeworld(enemyNode, enemiesMap.get(friendlyName), planetIndex);
                makeEnemies(enemyNode);
            }

            tx.success();
        } finally {
            tx.finish();
        }
    }

    private void makeEnemies(Node enemyNode) {
        enemyNode.createRelationshipTo(theDoctor, DoctorWhoUniverse.ENEMY_OF);
        theDoctor.createRelationshipTo(enemyNode, DoctorWhoUniverse.ENEMY_OF);
    }
}
