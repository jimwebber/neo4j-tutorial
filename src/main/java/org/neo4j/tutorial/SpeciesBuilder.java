package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class SpeciesBuilder {
    public static Node ensureSpeciesInDb(String theSpecies, DoctorWhoUniverse universe) {
        ensureArgumentsAreSane(theSpecies, universe);
        
        GraphDatabaseService db = universe.getDatabase();
        
        Node speciesNode = universe.speciesIndex.get("species", theSpecies).getSingle();

        if (speciesNode == null) {
            speciesNode = db.createNode();
            speciesNode.setProperty("species", theSpecies);
            universe.speciesIndex.add(speciesNode, "species", theSpecies);
        }

        return speciesNode;
    }

    private static void ensureArgumentsAreSane(String theSpecies, DoctorWhoUniverse universe) {
        if(theSpecies == null) {
            throw new RuntimeException("Must provide a value for the species to the species builder");
        }
        
        if(universe == null) {
            throw new RuntimeException("Must provide a value for the universe to the species builder");
        }
    }
}
