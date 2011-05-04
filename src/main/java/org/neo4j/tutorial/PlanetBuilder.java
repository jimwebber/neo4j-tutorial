package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class PlanetBuilder {

    private final String planetName;

    public static PlanetBuilder planet(String planetName) {
        return new PlanetBuilder(planetName);
    }

    private PlanetBuilder(String planetName) {
        this.planetName = planetName;
    }
    
    public void fact(DoctorWhoUniverse universe) {
        ensurePlanetInDb(planetName, universe);
    }
    
    public static Node ensurePlanetInDb(String planet, DoctorWhoUniverse universe) {

        GraphDatabaseService db = universe.getDatabase();

        Node planetNode = universe.planetIndex.get("planet", planet).getSingle();

        if (planetNode == null) {
            planetNode = db.createNode();
            planetNode.setProperty("planet", planet);
            universe.planetIndex.add(planetNode, "planet", planet);
        }

        return planetNode;

    }

}
