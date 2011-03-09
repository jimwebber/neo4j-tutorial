package org.neo4j.tutorial;

import org.junit.After;
import org.junit.Before;
import org.neo4j.graphdb.GraphDatabaseService;

public abstract class Koan {
    protected GraphDatabaseService db;

    @Before
    public void createADatabase() {
        db = DatabaseHelper.createDatabase();
    }
    
    @After
    public void closeTheDatabase() {
        db.shutdown();
    }
}
