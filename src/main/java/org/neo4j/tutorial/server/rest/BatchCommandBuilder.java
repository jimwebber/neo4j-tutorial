package org.neo4j.tutorial.server.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BatchCommandBuilder
{
    private static final String CreateNodeTemplate = "{\"method\":\"POST\",\"to\":\"/node\",\"body\":{%s}%s}";
    private static final String CreateRelationshipTemplate = "{\"method\":\"POST\",\"to\":\"%s\",\"body\":{\"to\":\"%s\",\"type\":\"%s\"%s}%s}";
    private static final String DeleteTemplate = "{\"method\":\"DELETE\",\"to\":\"%s\"}";

    private final List<String> commands = new ArrayList<String>();

    public BatchCommandBuilder createNode(int jobId, Map<String, String> bodyParams)
    {
        return formatCreateNode(bodyParams, jobId);
    }

    public BatchCommandBuilder createNode(Map<String, String> bodyParams)
    {
        return formatCreateNode(bodyParams, null);
    }

    public BatchCommandBuilder createRelationship(int jobId, String startNodeRelationshipUri, String endNodeUri, String relType, Map<String, String> dataParams)
    {
        return formatCreateRelationship(startNodeRelationshipUri, endNodeUri, relType, dataParams, jobId);
    }

    public BatchCommandBuilder createRelationship(String startNodeRelationshipUri, String endNodeUri, String relType)
    {
        return formatCreateRelationship(startNodeRelationshipUri, endNodeUri, relType, null, null);
    }

    public BatchCommandBuilder createRelationship(String startNodeRelationshipUri, String endNodeUri, String relType, Map<String, String> dataParams)
    {
        return formatCreateRelationship(startNodeRelationshipUri, endNodeUri, relType, dataParams, null);
    }

    public BatchCommandBuilder deleteNodeOrRelationship(String uri)
    {
        commands.add(String.format(DeleteTemplate, uri));
        return this;
    }

    private BatchCommandBuilder formatCreateNode(Map<String, String> bodyParams, Integer jobId)
    {
        commands.add(String.format(CreateNodeTemplate, formatParams(bodyParams), formatJobId(jobId)));
        return this;
    }

    private BatchCommandBuilder formatCreateRelationship(String startNodeRelationshipUri, String endNodeUri, String relType, Map<String, String> dataParams, Integer jobId)
    {
        commands.add(String.format(CreateRelationshipTemplate, startNodeRelationshipUri, endNodeUri, relType,
                                   createData(dataParams), formatJobId(jobId)));
        return this;
    }

    private String formatJobId(Integer jobId)
    {
        if (jobId == null)
        {
            return "";
        }
        return String.format(",\"id\":%s", jobId);
    }

    private String createData(Map<String, String> dataParams)
    {
        if (dataParams == null || dataParams.isEmpty())
        {
            return "";
        }
        return ",\"data\":{" + formatParams(dataParams) + "}";
    }

    private String formatParams(Map<String, String> params)
    {
        if (params == null)
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext())
        {
            String key = keys.next();
            sb.append(String.format("\"%s\":\"%s\"", key, params.get(key)));
            sb.append(keys.hasNext() ? "," : "");
        }
        return sb.toString();
    }

    public String build()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Iterator<String> cmds = commands.iterator();
        while (cmds.hasNext())
        {
            sb.append(cmds.next());
            sb.append(cmds.hasNext() ? "," : "");
        }
        sb.append("]");
        return sb.toString();
    }
}