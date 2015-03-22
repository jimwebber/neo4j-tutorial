package org.neo4j.tutorial;

import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * This first Koan will introduce you to the tool support available for Neo4j.
 * It will also introduce you to the Doctor Who universe.
 */
public class Koan1
{

    @ClassRule
    public static DoctorWhoUniverseResource neo4j = new DoctorWhoUniverseResource();

    @Test
    public void justEmitsThePathToTheDatabase() throws Exception
    {
        assertNotNull( neo4j.getGraphDatabaseService() );
    }
}
