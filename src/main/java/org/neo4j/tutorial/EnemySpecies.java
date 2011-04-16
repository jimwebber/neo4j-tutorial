package org.neo4j.tutorial;

import java.io.File;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

public class EnemySpecies {

    private List<String> enemySpeciesList;
    private final Node theDoctor;

    @SuppressWarnings("unchecked")
    public EnemySpecies(Node theDoctor, File data) {
        this.theDoctor = theDoctor;
        ObjectMapper m = new ObjectMapper();
        try {
            enemySpeciesList = m.readValue(data, List.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void insertEnemySpeciesRelationships(GraphDatabaseService db, final Index<Node> speciesIndex) {
        Transaction tx = db.beginTx();
        try {
            for(String enemySpecies : enemySpeciesList) {
                Node speciesNode = speciesIndex.get("species", enemySpecies).getSingle();
                if(speciesNode != null) {
                    speciesNode.createRelationshipTo(theDoctor, DoctorWhoUniverse.ENEMY_OF);
                    theDoctor.createRelationshipTo(speciesNode, DoctorWhoUniverse.ENEMY_OF);
                } else {
                    throw new RuntimeException(String.format("Species [%s] is not known in the Doctor Who universe, unable to add EMENY_OF relationship to the Doctor with node id [%d]", enemySpecies, theDoctor.getId()));
                }
            }
            tx.success();
        } finally {
            tx.finish();
        }
    }

}
