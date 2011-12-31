package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 * In this Koan we enhance the default REST API with managed and unmanaged (JAX-RS) extensions
 * to provide a domain-specific set of Doctor Who resources inside the Neo4j server.
 */
@Ignore
public class Koan12
{

    private static ServerDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new ServerDoctorWhoUniverse(new DoctorWhoUniverseGenerator());
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }


}
