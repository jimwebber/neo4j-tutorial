package org.neo4j.tutorial.spring;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.tutorial.DoctorWhoUniverse;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DoctorWhoUniverseConfiguration {
	
	private final DoctorWhoUniverse universe = new DoctorWhoUniverse();
	
	public GraphDatabaseService getGraphDatabaseService(){
		return universe.getDatabase();
	}
}
