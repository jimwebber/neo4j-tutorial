package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import static org.neo4j.tutorial.ActorBuilder.actor;

public class Actors
{

    private final GraphDatabaseService db;

    public Actors(GraphDatabaseService db)
    {
        this.db = db;
    }

    public void insert()
    {
        Transaction tx = db.beginTx();
        try
        {
            actor("David Tennant").played("Doctor").salary(1000000).fact(db);
            actor("Matt Smith").played("Doctor").salary(200000).fact(db);
            actor("Alex Kingston").played("River Song").fact(db);

            tx.success();
        } finally
        {
            tx.finish();
        }
    }
}
