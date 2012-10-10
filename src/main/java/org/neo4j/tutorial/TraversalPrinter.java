package org.neo4j.tutorial;

import static java.util.Arrays.asList;

import java.util.Iterator;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;

public class TraversalPrinter implements Evaluator
{
    private final Iterable<String> propertyNames;
    private final Evaluator innerEvaluator;

    public TraversalPrinter( Evaluator innerEvaluator, String... propertyNames )
    {
        this.innerEvaluator = innerEvaluator;
        this.propertyNames = asList( propertyNames );
    }

    private void printPath( Path path )
    {
        for ( PropertyContainer propertyContainer : path )
        {
            if ( Node.class.isAssignableFrom( propertyContainer.getClass() ) )
            {
                printNode( (Node) propertyContainer );
            }
            else
            {
                printRelationship( (Relationship) propertyContainer, path.endNode() );
            }
        }
    }

    private Evaluation printResult( Evaluation evaluation )
    {
        System.out.print( String.format( " %s\n", evaluation.name() ) );
        return evaluation;
    }


    private void printRelationship( Relationship relationship, Node currentNode )
    {
        String prefix = "";
        String suffix = "";

        if ( relationship.getStartNode().equals( currentNode ) )
        {
            prefix = "<";
        }
        else
        {
            suffix = ">";
        }

        System.out.print( String.format( "%s-[:%s]-%s", prefix, relationship.getType().name(), suffix ) );
    }

    private void printNode( Node node )
    {
        System.out.print( "(" );
        boolean propertyFound = false;
        Iterator<String> iterator = propertyNames.iterator();
        while ( iterator.hasNext() && !propertyFound )
        {
            String propertyName = iterator.next();
            if ( node.hasProperty( propertyName ) )
            {
                System.out.print( String.format( "%s:%s", propertyName, node.getProperty( propertyName ) ) );
                propertyFound = true;
            }
        }
        if ( !propertyFound )
        {
            System.out.print( String.format( "id:%s", node.getId() ) );
        }

        System.out.print( ")" );
    }

    public Evaluation evaluate( Path path )
    {
        printPath( path );
        return printResult( innerEvaluator.evaluate( path ) );
    }
}
