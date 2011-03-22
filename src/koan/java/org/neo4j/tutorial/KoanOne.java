package org.neo4j.tutorial;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;


/**
 * This first Koan will introduce you to the tool support available for Neo4j.
 * It will also introduce you to the Doctor Who universe.
 */
public class KoanOne {
    @Test
    public void justEmitsThePathToTheDatabase() throws JsonParseException, JsonMappingException, RuntimeException, IOException {
        DoctorWhoUniverse universe = new DoctorWhoUniverse();
        assertNotNull(universe.getDatabase());
    }
}
