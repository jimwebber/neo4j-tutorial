package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class EmbeddedDoctorWhoUniverse {
	
	private final EmbeddedGraphDatabase db;

	public EmbeddedDoctorWhoUniverse(DoctorWhoUniverseGenerator universe) {
	    db = new EmbeddedGraphDatabase(universe.getDatabaseDirectory());
	}
	
	@Override
	public Node theDoctor() {
		return db.index().forNodes("characters").get("name", "Doctor").getSingle();
	}
	
	@Override
	public void stop() {
        if(db!= null) db.shutdown();
    }
	
	public GraphDatabaseService getDatabase() {
        return db;
    }
}
