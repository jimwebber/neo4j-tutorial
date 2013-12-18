package org.neo4j.tutorial;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import static java.lang.System.lineSeparator;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;

public class AwesomenessRatingEngine
{
    private GraphDatabaseService db;

    public AwesomenessRatingEngine( GraphDatabaseService db )
    {
        this.db = db;
    }

    public double rateAwesomeness( Node node )
    {
        try ( Transaction transaction = db.beginTx() )
        {
            ExecutionEngine engine = new ExecutionEngine( db, DEV_NULL );

            String cql = String.format( "MATCH(doc:Character {character: 'Doctor'}), (n:%s {%s})",
                    expandLabels( node ), expandNodeProperties( node ) ) +
                    lineSeparator() +
                    "MATCH p=shortestPath( (n)-[*..15]-(doc) )" +
                    lineSeparator() +
                    "RETURN length(p) as hops";


            ExecutionResult result = engine.execute( cql );
            transaction.success();

            int hops = Integer.valueOf( String.valueOf( result.javaColumnAs( "hops" ).next() ) );

            return 100 / ((hops + 1) * 1.0);
        }
    }

    private String expandLabels( Node node )
    {
        StringBuilder sb = new StringBuilder();

        for ( Label label : node.getLabels() )
        {
            try
            {
                sb.append( label.name() );
                sb.append( ":" );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        return removeTrailingDelimiter( ':', sb.toString() );
    }

    private String expandNodeProperties( Node node )
    {
        StringBuilder sb = new StringBuilder();

        for ( String key : node.getPropertyKeys() )
        {
            sb.append( key );
            sb.append( ": " );
            sb.append( "\"" );
            sb.append( node.getProperty( key ) );
            sb.append( "\"" );
        }

        return removeTrailingDelimiter( ',', sb.toString() );
    }

    private String removeTrailingDelimiter( char delimiter, String s )
    {
        if ( s.endsWith( String.valueOf( delimiter ) ) )
        {
            return s.substring( 0, s.length() - 1 );
        }
        else
        {
            return s;
        }
    }
}
