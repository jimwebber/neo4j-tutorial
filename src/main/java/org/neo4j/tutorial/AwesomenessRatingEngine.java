package org.neo4j.tutorial;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import static java.lang.System.lineSeparator;


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
            String cql = String.format( "MATCH(doc:Character {character: 'Doctor'}), (n:%s {%s})",
                    expandLabels( node ), expandNodeProperties( node ) ) +
                    lineSeparator() +
                    "MATCH p=shortestPath( (n)-[*0..15]-(doc) )" +
                    lineSeparator() +
                    "RETURN length(p) as hops";

            Result result = db.execute( cql );
            transaction.success();

            int hops = Integer.valueOf( String.valueOf( result.columnAs( "hops" ).next() ) );

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
