package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.neo4j.server.NeoServerWithEmbeddedWebServer;
import org.neo4j.tutorial.server.ServerBuilder;

/**
 * In this Koan we enhance the default REST API with managed extensions
 * to provide a domain-specific set of Doctor Who resources inside the Neo4j server.
 */
public class Koan13
{

    private static ServerDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        DoctorWhoUniverseGenerator doctorWhoUniverseGenerator = new DoctorWhoUniverseGenerator();

        NeoServerWithEmbeddedWebServer server = ServerBuilder
                .server()
                .usingDatabaseDir(doctorWhoUniverseGenerator.getDatabaseDirectory())
                .withThirdPartyJaxRsPackage("org.neo4j.tutorial.koan13", "/koan13")
                .build();

        universe = new ServerDoctorWhoUniverse(server, doctorWhoUniverseGenerator);
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

}
