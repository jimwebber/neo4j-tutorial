package org.neo4j.tutorial;

import java.util.concurrent.TimeUnit;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.schema.IndexDefinition;

import static java.util.concurrent.TimeUnit.SECONDS;

import static org.junit.Assert.assertThat;

import static org.neo4j.graphdb.DynamicLabel.label;
import static org.neo4j.helpers.collection.IteratorUtil.asSet;
import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;
import static org.neo4j.tutorial.DatabaseHelper.destructivelyCount;

/**
 * In this koan, we create indexes to boost query performance.
 */
public class Koan12
{
    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator().getDatabase() );
        dropAnyExistingSpeciesIndexOrConstraints();
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Test
    public void shouldCreateAnIndexOfSpecies()
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "CREATE INDEX ON :Species(species) ";

        // SNIPPET_END

        engine.execute( cql );

        assertThat( universe.getDatabase(), hasIndex( "Species" ) );
    }

    @Test
    public void shouldDropSpeciesIndex() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "DROP INDEX ON :Species(species) ";

        // SNIPPET_END

        engine.execute( cql );

        assertThat( universe.getDatabase(), hasNoSuchIndex( "Species" ) );
    }

    private TypeSafeMatcher<GraphDatabaseService> hasNoSuchIndex( String indexName )
    {
        return new TypeSafeMatcher<GraphDatabaseService>()
        {
            @Override
            protected boolean matchesSafely( GraphDatabaseService graphDatabaseService )
            {
                try ( Transaction tx = universe.getDatabase().beginTx() )
                {
                    return destructivelyCount( universe.getDatabase().schema()
                            .getIndexes( label( "Species" ) ).iterator() ) == 0;
                }
            }

            @Override
            public void describeTo( Description description )
            {
                description.appendText( "" );
            }
        };
    }

    private void waitForIndexToBeBuilt( Label label, long timeout, TimeUnit timeUnit )
    {
        try ( Transaction tx = universe.getDatabase().beginTx() )
        {
            universe.getDatabase().schema().awaitIndexOnline( universe.getDatabase()
                    .schema()
                    .getIndexes( label ).iterator().next(),
                    timeout, timeUnit );
            tx.success();
        }
    }

    private TypeSafeMatcher<GraphDatabaseService> hasIndex( final String indexName )
    {
        return new TypeSafeMatcher<GraphDatabaseService>()
        {
            @Override
            protected boolean matchesSafely( GraphDatabaseService db )
            {
                try ( Transaction tx = universe.getDatabase().beginTx() )
                {
                    waitForIndexToBeBuilt( label( "Species" ), 30, SECONDS );

                    IndexDefinition species = universe.getDatabase()
                            .schema()
                            .getIndexes( label( "Species" ) )
                            .iterator()
                            .next();

                    tx.success();

                    return species.getLabel().equals( label( "Species" ) )
                            && asSet( species.getPropertyKeys() ).contains( "species" );

                }
                catch ( Exception e )
                {
                    return false;
                }
            }

            @Override
            public void describeTo( Description description )
            {
                description.appendText( "index on species to be ready" );
            }
        };
    }


    private static void dropAnyExistingSpeciesIndexOrConstraints()
    {
        try ( Transaction tx = universe.getDatabase().beginTx() )
        {
            // Species index is implied by the universe's uniqueness constraint, so we'll drop that as part of setup
            universe.getDatabase().schema().getConstraints( label( "Species" ) ).iterator().next().drop();
            tx.success();
        }
    }
}