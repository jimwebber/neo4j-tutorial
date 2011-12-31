package org.neo4j.tutorial.server.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TraversalDescription
{

    public static final String DEPTH_FIRST = "depth_first";
    public static final String NODE = "node";
    public static final String ALL = "all";

    private String uniqueness = NODE;
    private int maxDepth = 1;
    private String returnFilter = ALL;
    private String order = DEPTH_FIRST;
    private List<RelationshipDescription> relationships = new ArrayList<RelationshipDescription>();

    public void setOrder(String order)
    {
        this.order = order;
    }

    public void setUniqueness(String uniqueness)
    {
        this.uniqueness = uniqueness;
    }

    public void setMaxDepth(int maxDepth)
    {
        this.maxDepth = maxDepth;
    }

    public void setReturnFilter(String returnFilter)
    {
        this.returnFilter = returnFilter;
    }

    public void setRelationships(RelationshipDescription... relationships)
    {
        this.relationships = Arrays.asList(relationships);
    }

    public String toJson()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append(" \"order\" : \"" + order + "\"");
        sb.append(", ");
        sb.append(" \"uniqueness\" : \"" + uniqueness + "\"");
        sb.append(", ");
        if (relationships.size() > 0)
        {
            sb.append("\"relationships\" : [");
            for (int i = 0; i < relationships.size(); i++)
            {
                sb.append(relationships.get(i)
                                       .toJsonCollection());
                if (i < relationships.size() - 1)
                { // Miss off the final comma
                    sb.append(", ");
                }
            }
            sb.append("], ");
        }
        sb.append("\"return_filter\" : { ");
        sb.append("\"language\" : \"javascript\", ");
        sb.append("\"body\" : \"");
        sb.append(returnFilter);
        sb.append("\" }, ");
        sb.append("\"max_depth\" : ");
        sb.append(maxDepth);
        sb.append(" }");
        return sb.toString();
    }
}
