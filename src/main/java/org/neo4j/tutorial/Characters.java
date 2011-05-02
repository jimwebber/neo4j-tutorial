package org.neo4j.tutorial;

import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

public abstract class Characters {

    public abstract void insertAndIndex(GraphDatabaseService db, Index<Node> enemiesIndex, Index<Node> speciesIndex, Index<Node> planetIndex);
    
    protected void connectToSpecies(Node characterNode, Map<String, String> map, Index<Node> speciesIndex) {
        if (map == null || !map.containsKey("species")) {
            return; // no species known for this character
        }

        String speciesName = map.get("species");
        Node species = speciesIndex.get("species", speciesName).getSingle();
        if (species != null) {
                characterNode.createRelationshipTo(species, DoctorWhoUniverse.IS_A);
        } else {
            throw new RuntimeException(String.format(
                    "Species [%s] is not known in the Doctor Who universe, unable to add IS_A relationship for character [%s]", speciesName,
                    characterNode.getProperty("name")));
        }
    }
}
