package org.neo4j.tutorial;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * This first Koan will introduce you to the tool support available for Neo4j.
 * It will also introduce you to the Doctor Who universe.
 */
public class Koan1
{
    @Test
    public void justEmitsThePathToTheDatabase() throws Exception
    {
        EmbeddedDoctorWhoUniverse universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator()
                .getDatabase() );
        assertNotNull( universe.getDatabase() );
        universe.stop();
    }
}
