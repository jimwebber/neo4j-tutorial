package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class PlanetBuilder
{

    private final String planetName;

    public static PlanetBuilder planet(String planetName)
    {
        return new PlanetBuilder(planetName);
    }

    private PlanetBuilder(String planetName)
    {
        this.planetName = planetName;
    }

    public void fact(GraphDatabaseService db)
    {
        ensurePlanetInDb(planetName, db);
    }

    public static Node ensurePlanetInDb(String planet, GraphDatabaseService db)
    {

        Node planetNode = db.index()
                            .forNodes("planets")
                            .get("planet", planet)
                            .getSingle();

        if (planetNode == null)
        {
            planetNode = db.createNode();
            planetNode.setProperty("planet", planet);
            db.index()
              .forNodes("planets")
              .add(planetNode, "planet", planet);
        }

        return planetNode;
    }
}
