package org.neo4j.tutorial;

import org.junit.After;
import org.junit.Before;
import org.neo4j.graphdb.GraphDatabaseService;

public abstract class Koan {
    protected GraphDatabaseService db;
    protected DatabaseHelper databaseHelper;

    @Before
    public void createADatabase() {
        db = DatabaseHelper.createDatabase();
        databaseHelper = new DatabaseHelper(db);
    }
    
    @After
    public void closeTheDatabase() {
        db.shutdown();
    }
}
