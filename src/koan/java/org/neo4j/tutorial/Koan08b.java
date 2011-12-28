package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.Node;

import java.util.Iterator;

/**
 * In this Koan we focus on aggregate functions from the Cypher graph pattern matching language
 * to process some statistics about the Doctor Who universe.
 */
public class Koan08b
{
    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator() );
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Test
    public void shouldCountTheNumberOfActorsKnownToHavePlayedTheDoctor()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase() );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "start doctor = node:characters(character = 'Doctor') return doctor";
//                + "match (doctor)<-[:PLAYED]-(actor) "
//                + "return actor, count(*) ";

        // SNIPPET_END

//        ExecutionResult result = engine.execute( cql );
//        Iterator<Node> actors = result.javaColumnAs( "actors" );
//
//        assertEquals(11, count(actors));
    }

    private int count(Iterator<Node> nodes) {
        int result = 0;
        
        while(nodes.hasNext())
        {
            nodes.next();
            result++;
        }
        
        return result;
    }
}
