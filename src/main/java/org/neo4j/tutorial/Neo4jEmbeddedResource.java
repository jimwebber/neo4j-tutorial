package org.neo4j.tutorial;

import org.junit.rules.ExternalResource;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.test.TestGraphDatabaseFactory;

public class Neo4jEmbeddedResource extends ExternalResource
{

    private final GraphDatabaseGenerator generator;

    private GraphDatabaseService graphDatabaseService;

    public Neo4jEmbeddedResource() {
        this.generator = null;
    }

    public Neo4jEmbeddedResource(GraphDatabaseGenerator generator) {
        this.generator = generator;
    }

    public GraphDatabaseService getGraphDatabaseService() {
        return graphDatabaseService;
    }

    @Override
    protected void before() throws Throwable {

        if (generator == null) {
            graphDatabaseService = new TestGraphDatabaseFactory().newImpermanentDatabase();
        } else {
            graphDatabaseService = new GraphDatabaseFactory().newEmbeddedDatabase(generator.generate());
        }
    }

    @Override
    protected void after() {
        graphDatabaseService.shutdown();
    }
}
