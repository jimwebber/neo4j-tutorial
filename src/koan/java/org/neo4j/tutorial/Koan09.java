package org.neo4j.tutorial;

import static org.junit.Assert.*;

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

	private static NeoServer server;

	@BeforeClass
	public static void createDatabase() throws Exception {
		DoctorWhoUniverse universe = new DoctorWhoUniverse();
		universe.stop();
		
		server = ServerBuilder.server()
				.usingDatabaseDir(universe.getDatabaseDirectory())
				.withPassingStartupHealthcheck().withDefaultDatabaseTuning()
				.build();
		server.start();
	}

	@AfterClass
	public static void closeTheDatabase() {
		server.stop();
	}

	@Test
	public void doX() throws IOException {
		assertEquals(1, 1);
	}
}
