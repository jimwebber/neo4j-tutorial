package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class EmbeddedDoctorWhoUniverse extends DoctorWhoUniverse {

	private final GraphDatabaseService db = DatabaseHelper.createDatabase(getDatabaseDirectory());

	@Override
	Node theDoctor() {
		return db.index().forNodes("characters").get("name", "Doctor").getSingle();
	}
	
	@Override
	public GraphDatabaseService getDatabase() {
        return db;
    }
	
	@Override
	public void stop() {
        if(db!= null) db.shutdown();
    }
}
