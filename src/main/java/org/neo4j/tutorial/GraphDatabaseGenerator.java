package org.neo4j.tutorial;

public interface GraphDatabaseGenerator
{
    /**
     * return the store directory of generated (or cached) graph db
     */
    String generate();
}
