package org.neo4j.tutorial;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.server.NeoServer;
import org.neo4j.tutorial.server.ServerBuilder;


/**
 * In this Koan we use the REST API to explore the Doctor Who universe.
 */
public class Koan09 {
	
	private static DoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception {
        universe = new DoctorWhoUniverse();
    }

    @AfterClass
    public static void closeTheDatabase() {
        universe.stop();    
    }
    
	@Test
	public void doX() throws IOException{
		NeoServer server = ServerBuilder.server().usingDatabaseDir(universe.getDatabaseDirectory()).build();
		server.start();
		server.stop();
	}
}
