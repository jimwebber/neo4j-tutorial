package org.neo4j.tutorial;

import java.util.concurrent.TimeUnit;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
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
    @ClassRule
    static public DoctorWhoUniverseResource neo4jResource = new DoctorWhoUniverseResource();

    @BeforeClass
    public static void dropAnyExistingSpeciesIndexOrConstraints() {
        try (Transaction tx = neo4jResource.getGraphDatabaseService().beginTx()) {
            // Species index is implied by the universe's uniqueness constraint, so we'll drop that as part of setup
            neo4jResource.getGraphDatabaseService().schema().getConstraints(label("Species")).iterator().next().drop();
            tx.success();
        }
    }

    @Test
    public void shouldCreateAnIndexOfSpecies()
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "CREATE INDEX ON :Species(species) ";

        // SNIPPET_END

        db.execute( cql );

        assertThat( db, hasIndex("Species") );
    }

    @Test
    public void shouldDropSpeciesIndex() throws Exception
    {
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        String cql = null;

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql = "DROP INDEX ON :Species(species) ";

        // SNIPPET_END

        db.execute( cql );

        assertThat( db, hasNoSuchIndex("Species") );
    }

    private TypeSafeMatcher<GraphDatabaseService> hasNoSuchIndex( String indexName )
    {
        return new TypeSafeMatcher<GraphDatabaseService>()
        {
            @Override
            protected boolean matchesSafely( GraphDatabaseService db )
            {
                try ( Transaction tx = db.beginTx() )
                {
                    return destructivelyCount( db.schema()
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
        GraphDatabaseService db = neo4jResource.getGraphDatabaseService();
        try ( Transaction tx = db.beginTx() )
        {
            db.schema().awaitIndexOnline( db
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
                try ( Transaction tx = db.beginTx() )
                {
                    waitForIndexToBeBuilt( label( "Species" ), 30, SECONDS );

                    IndexDefinition species = db
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


}