package org.neo4j.tutorial.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.tutorial.DoctorWhoRelationships;

import java.util.Set;

public class ContainsOnlyHumanCompanions extends TypeSafeMatcher<Set<Node>>
{

    private Node failedNode;

    public void describeTo(Description description)
    {
        description.appendText(String.format(
                "Node [%d] does not have an IS_A relationship to the human species node.", failedNode.getId()));
    }

    @Override
    public boolean matchesSafely(Set<Node> nodes)
    {
        for (Node n : nodes)
        {
            if (!(n.hasRelationship(DoctorWhoRelationships.IS_A, Direction.OUTGOING) && n.getSingleRelationship(
                    DoctorWhoRelationships.IS_A, Direction.OUTGOING)
                                                                                         .getEndNode()
                                                                                         .getProperty("species")
                                                                                         .equals("Human")))
            {
                failedNode = n;
                return false;
            }
        }
        return true;
    }

    @Factory
    public static ContainsOnlyHumanCompanions containsOnlyHumanCompanions()
    {
        return new ContainsOnlyHumanCompanions();
    }
}
