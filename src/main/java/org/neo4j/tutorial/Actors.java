package org.neo4j.tutorial;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;
import static org.neo4j.tutorial.ActorBuilder.actor;

public class Actors
{

    private final GraphDatabaseService db;
    private final ExecutionEngine engine;

    public Actors( GraphDatabaseService db )
    {
        this.db = db;
        this.engine = new ExecutionEngine( db, DEV_NULL );
    }

    public void insert()
    {
        try ( Transaction tx = db.beginTx() )
        {
            actor( "David Tennant" ).wikipedia( "http://en.wikipedia.org/wiki/David_Tennant" ).played( "Doctor" )
                    .salary( 1000000 ).fact( engine );
            actor( "Matt Smith" ).wikipedia( "http://en.wikipedia.org/wiki/Matt_Smith_(actor)" ).played( "Doctor" )
                    .salary( 200000 ).fact( engine );
            actor( "Alex Kingston" ).wikipedia( "http://en.wikipedia.org/wiki/Alex_Kingston" ).played( "River Song" )
                    .fact( engine );
            actor( "Karen Gillan" ).played( "Amy Pond" ).fact( engine );
            actor( "Arthur Darvill" ).played( "Rory Williams" ).fact( engine );
            actor( "Freema Agyeman" ).played( "Martha Jones", "Adeola Oshodi" ).fact( engine );
            actor( "Jenna-Louise Coleman" ).played( "Clara Oswald" ).fact( engine );
            actor( "Sophie Aldred" ).played( "Ace" ).fact( engine );
            actor( "Timothy Dalton" ).played( "Rassilon" ).fact( engine );
            actor( "Richard Mathews" ).played( "Rassilon" ).fact( engine );

            tx.success();
        }
    }
}
