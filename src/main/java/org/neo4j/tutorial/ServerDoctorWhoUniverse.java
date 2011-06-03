package org.neo4j.tutorial;

import org.neo4j.graphdb.Node;
import org.neo4j.server.NeoServer;
import org.neo4j.tutorial.server.ServerBuilder;

public class ServerDoctorWhoUniverse extends DoctorWhoUniverse {

	private final NeoServer server;

	public ServerDoctorWhoUniverse() throws Exception {
		super();
		server = ServerBuilder.server()
				.usingDatabaseDir(getDatabaseDirectory())
				.withPassingStartupHealthcheck().withDefaultDatabaseTuning()
				.build();
		server.start();
	}

	@Override
	Node theDoctor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void stop() {
		server.stop();
	}

}
