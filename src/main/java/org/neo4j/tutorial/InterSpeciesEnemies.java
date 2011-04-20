package org.neo4j.tutorial;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class InterSpeciesEnemies {

    private Map<String, List<String>> interSpeciesEnemiesMap;

    @SuppressWarnings("unchecked")
    public InterSpeciesEnemies(File data) {
        ObjectMapper m = new ObjectMapper();
        try {
            interSpeciesEnemiesMap = m.readValue(data, Map.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(GraphDatabaseService db) {
        Transaction tx = db.beginTx();
        try {
            for (String enemyName : interSpeciesEnemiesMap.keySet()) {
                if (enemySpeciesKnown(enemyName, db)) {
                    List<String> enemySpecies = interSpeciesEnemiesMap.get(enemyName);
                    for(String enemy : enemySpecies) {
                        makeMutualEnemies(enemyName, enemy, db);
                    }
                } else {
                    throw new RuntimeException(String.format(
                            "The enemy species [%s] is not known in the Doctor Who universe, unable to add ENEMY_OF relationship between species", enemyName));
                }
            }

            tx.success();
        } finally {
            tx.finish();
        }

    }

    private void makeMutualEnemies(String e1, String e2, GraphDatabaseService db) {
        Node enemy1 = db.index().forNodes("species").get("species", e1).getSingle();
        Node enemy2 = db.index().forNodes("species").get("species", e2).getSingle();
        
        enemy1.createRelationshipTo(enemy2, DoctorWhoUniverse.ENEMY_OF);
        enemy2.createRelationshipTo(enemy1, DoctorWhoUniverse.ENEMY_OF);
    }

    private boolean enemySpeciesKnown(String enemyName, GraphDatabaseService db) {
        return db.index().forNodes("species").get("species", enemyName) != null;
    }

}
