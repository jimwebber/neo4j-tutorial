package org.neo4j.tutorial;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.tooling.GlobalGraphOperations;

public class DatabaseHelper
{
    private final GraphDatabaseService database;

    public DatabaseHelper( GraphDatabaseService database )
    {
        this.database = database;
    }

    public static GraphDatabaseService createDatabase()
    {
        return createDatabase( createTempDatabaseDir().getAbsolutePath() );
    }

    public static GraphDatabaseService createDatabase( String dbDir )
    {
        return new GraphDatabaseFactory().newEmbeddedDatabase( dbDir );
    }

    public static File createTempDatabaseDir()
    {
        File directory;
        try
        {
            directory = File.createTempFile( "neo4j-koans", "dir" );
            System.out.println( String.format( "Created a new Neo4j database at [%s]", directory.getAbsolutePath() ) );
//            System.out.println( System.lineSeparator() );
//            System.out.println( System.lineSeparator() );
//
//            System.out.println( "bin/neo4j stop" );
//            System.out.println( "rm -r data/graph.db/" );
//            System.out.println( String.format( "cp -r %s/ data/graph.db/", directory.getAbsolutePath() ) );
//            System.out.println( "bin/neo4j start" );
//
//            System.out.println( System.lineSeparator() );
//            System.out.println( System.lineSeparator() );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
        if ( !directory.delete() )
        {
            throw new RuntimeException( "temp config directory pre-delete failed" );
        }
        if ( !directory.mkdirs() )
        {
            throw new RuntimeException( "temp config directory not created" );
        }
        directory.deleteOnExit();
        return directory;
    }

    public static void ensureRelationshipInDb( Node startNode, RelationshipType relType, Node endNode, Map<String,
            Object> relationshipProperties )
    {
        for ( Relationship r : startNode.getRelationships( relType, Direction.OUTGOING ) )
        {
            if ( r.getEndNode()
                    .equals( endNode ) )
            {
                return;
            }
        }

        Relationship relationship = startNode.createRelationshipTo( endNode, relType );

        for ( String key : relationshipProperties.keySet() )
        {
            relationship.setProperty( key, relationshipProperties.get( key ) );
        }
    }

    public static void ensureRelationshipInDb( Node startNode, RelationshipType relType, Node endNode )
    {
        ensureRelationshipInDb( startNode, relType, endNode, new HashMap<String, Object>() );
    }

    public void dumpGraphToConsole()
    {
        for ( Node n : GlobalGraphOperations.at( database ).getAllNodes() )
        {
            Iterable<String> propertyKeys = n.getPropertyKeys();
            for ( String key : propertyKeys )
            {
                System.out.print( key + " : " );
                System.out.println( n.getProperty( key ) );
            }
        }
    }

    public boolean nodeExistsInDatabase( Node node )
    {
        try ( Transaction tx = database.beginTx() )
        {
            Node nodeById = database.getNodeById( node.getId() );
            tx.success();
            return nodeById != null;
        }
    }

    public int destructivelyCountRelationships( Iterable<Relationship> relationships )
    {
        return destructivelyCount( relationships );
    }

    public static void dumpNode( Node node )
    {
        if ( node == null )
        {
            System.out.println( "Null Node" );
            return;
        }
        System.out.println( String.format( "Node ID [%d]", node.getId() ) );
        for ( String key : node.getPropertyKeys() )
        {
            System.out.print( key + " : " );
            System.out.println( node.getProperty( key ) );
        }
    }

    public List<Relationship> toListOfRelationships( Iterable<Relationship> iterable )
    {
        ArrayList<Relationship> rels = new ArrayList<>();
        for ( Relationship r : iterable )
        {
            rels.add( r );
        }
        return rels;
    }

    public List<Node> toListOfNodes( Iterable<Node> nodes )
    {
        ArrayList<Node> rels = new ArrayList<>();
        for ( Node n : nodes )
        {
            rels.add( n );
        }
        return rels;
    }

    public int destructivelyCount( Iterator iterator )
    {
        return destructivelyCount( IteratorUtil.asIterable( iterator ) );
    }

    public int destructivelyCount( Iterable iterable )
    {
        int count = 0;

        for ( @SuppressWarnings("unused") Object o : iterable )
        {
            count++;
        }

        return count;
    }
}
