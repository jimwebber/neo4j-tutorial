package org.neo4j.tutorial.server.rest;

import org.junit.Test;
import org.neo4j.helpers.collection.MapUtil;

import static org.junit.Assert.assertEquals;

public class BatchCommandBuilderTests
{

    @Test
    public void shouldFormatCreateNodeRequestCorrectly()
    {
        String result = new BatchCommandBuilder().createNode(1, MapUtil.stringMap("key", "value"))
                                                 .build();
        assertEquals("[{\"method\":\"POST\",\"to\":\"/node\",\"body\":{\"key\":\"value\"},\"id\":1}]", result);

    }

    @Test
    public void shouldFormatCreateNodeRequestWithoutJobIdCorrectly()
    {
        String result = new BatchCommandBuilder().createNode(MapUtil.stringMap("key", "value"))
                                                 .build();
        assertEquals("[{\"method\":\"POST\",\"to\":\"/node\",\"body\":{\"key\":\"value\"}}]", result);
    }

    @Test
    public void shouldFormatCreateNodeRequestWithEmptyParamsCorrectly()
    {
        String result = new BatchCommandBuilder().createNode(MapUtil.stringMap())
                                                 .build();
        assertEquals("[{\"method\":\"POST\",\"to\":\"/node\",\"body\":{}}]", result);

    }

    @Test
    public void shouldFormatCreateNodeRequestWithNullParamsCorrectly()
    {
        String result = new BatchCommandBuilder().createNode(null)
                                                 .build();
        assertEquals("[{\"method\":\"POST\",\"to\":\"/node\",\"body\":{}}]", result);

    }

    @Test
    public void shouldFormatCreateRelationshipRequestCorrectly()
    {
        String result = new BatchCommandBuilder().createRelationship(1, "/node/1", "node/2", "KNOWS",
                                                                     MapUtil.stringMap("key", "value"))
                                                 .build();
        assertEquals(
                "[{\"method\":\"POST\",\"to\":\"/node/1\",\"body\":{\"to\":\"node/2\",\"type\":\"KNOWS\",\"data\":{\"key\":\"value\"}},\"id\":1}]",
                result);
    }

    @Test
    public void shouldFormatCreateRelationshipRequestWithoutJobIdCorrectly()
    {
        String result = new BatchCommandBuilder().createRelationship("/node/1", "node/2", "KNOWS",
                                                                     MapUtil.stringMap("key", "value"))
                                                 .build();
        assertEquals(
                "[{\"method\":\"POST\",\"to\":\"/node/1\",\"body\":{\"to\":\"node/2\",\"type\":\"KNOWS\",\"data\":{\"key\":\"value\"}}}]",
                result);
    }

    @Test
    public void shouldFormatCreateRelationshipRequestWithoutJobIdOrDataCorrectly()
    {
        String result = new BatchCommandBuilder().createRelationship("/node/1", "node/2", "KNOWS")
                                                 .build();
        assertEquals("[{\"method\":\"POST\",\"to\":\"/node/1\",\"body\":{\"to\":\"node/2\",\"type\":\"KNOWS\"}}]",
                     result);
    }

    @Test
    public void shouldFormatCreateRelationshipRequestWithEmptyParamsCorrectly()
    {
        String result = new BatchCommandBuilder().createRelationship("/node/1", "node/2", "KNOWS", MapUtil.stringMap())
                                                 .build();
        assertEquals("[{\"method\":\"POST\",\"to\":\"/node/1\",\"body\":{\"to\":\"node/2\",\"type\":\"KNOWS\"}}]",
                     result);
    }

    @Test
    public void shouldFormatCreateRelationshipRequestWithNullParamsCorrectly()
    {
        String result = new BatchCommandBuilder().createRelationship("/node/1", "node/2", "KNOWS", null)
                                                 .build();
        assertEquals("[{\"method\":\"POST\",\"to\":\"/node/1\",\"body\":{\"to\":\"node/2\",\"type\":\"KNOWS\"}}]",
                     result);
    }

    @Test
    public void shouldFormatDeleteRequestCorrectly()
    {
        String result = new BatchCommandBuilder().deleteNodeOrRelationship("/node/123")
                                                 .build();
        assertEquals("[{\"method\":\"DELETE\",\"to\":\"/node/123\"}]", result);
    }
}
