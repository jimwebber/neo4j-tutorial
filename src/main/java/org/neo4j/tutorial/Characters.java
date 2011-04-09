package org.neo4j.tutorial;

import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

public abstract class Characters {

    public abstract void insertAndIndex(GraphDatabaseService db, Index<Node> enemiesIndex, Index<Node> speciesIndex, Index<Node> planetIndex);
    
    protected void connectToSpecies(Node friendlyNode, Map<String, String> map, Index<Node> speciesIndex) {
        if (map == null || !map.containsKey("species")) {
            return; // no planet known for this species
        }

        String speciesName = map.get("species");
        IndexHits<Node> indexHits = speciesIndex.get("species", speciesName);
        if (indexHits != null) {
            Node species = indexHits.getSingle();
            if (species != null) {
                friendlyNode.createRelationshipTo(species, DoctorWhoUniverse.IS_A);
            }
        } else {
            throw new RuntimeException(String.format(
                    "Species [%s] is not known in the Doctor Who universe, unable to add IS_A relationship for character [%s]", speciesName,
                    friendlyNode.getProperty("species")));
        }
    }
}
