package org.neo4j.tutorial;

import java.io.File;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

public class Species {

    private Map<String, Map<String, String>> speciesMap;

    @SuppressWarnings("unchecked")
    public Species(File data) {
        ObjectMapper m = new ObjectMapper();
        try {
            speciesMap = m.readValue(data, Map.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void insertAndIndex(GraphDatabaseService db, Index<Node> speciesIndex, final Index<Node> planetIndex) {
        Transaction tx = db.beginTx();
        try {
            for (String speciesName : speciesMap.keySet()) {
                Node speciesNode = db.createNode();
                speciesNode.setProperty("species", speciesName);
                speciesIndex.add(speciesNode, "species", speciesName);

                connectToHomeworld(speciesNode, speciesMap.get(speciesName), planetIndex);
            }
            tx.success();
        } finally {
            tx.finish();
        }
    }
    
    public static void connectToHomeworld(Node speciesNode, Map<String, String> map, Index<Node> planetIndex) {
        if(map == null || !map.containsKey("planet")) {
            return; // no planet known for this species
        }
        
        String planetName = map.get("planet");
        IndexHits<Node> indexHits = planetIndex.get("planet", planetName);
        if(indexHits != null) {
            Node planet = indexHits.getSingle();
            if(planet != null) {
                speciesNode.createRelationshipTo(planet, DoctorWhoUniverse.COMES_FROM);
            }
        } else {
            throw new RuntimeException(String.format("Planet [%s] is not known in the Doctor Who universe, unable to add FROM relationship for species [%s]", planetName, speciesNode.getProperty("species")));
        }
    }
}
