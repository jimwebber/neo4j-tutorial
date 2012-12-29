package org.neo4j.tutorial.matchers;

import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.tutorial.DoctorWhoRelationships;

public class ContainsOnlyHumanCompanions extends TypeSafeMatcher<Set<Node>>
{

    private Node failedNode;

    public void describeTo( Description description )
    {
        description.appendText( String.format(
                "Node [%d] does not have an IS_A relationship to the human species node.", failedNode.getId() ) );
    }

    @Override
    public boolean matchesSafely( Set<Node> nodes )
    {
        for ( Node n : nodes )
        {
            if ( !linksToHumanSpeciesNode( n ) )
            {
                failedNode = n;
                return false;
            }
        }
        return true;
    }

    private boolean linksToHumanSpeciesNode( Node n )
    {
        for ( Relationship relationship : n.getRelationships( DoctorWhoRelationships.IS_A, Direction.OUTGOING ) )
        {
            if ( relationship.getEndNode().hasProperty( "species" ) &&
                    relationship.getEndNode().getProperty( "species" ).equals( "Human" ) )
            {
                return true;
            }
        }
        return false;
    }

    @Factory
    public static ContainsOnlyHumanCompanions containsOnlyHumanCompanions()
    {
        return new ContainsOnlyHumanCompanions();
    }
}
