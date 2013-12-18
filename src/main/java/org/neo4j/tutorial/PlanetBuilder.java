package org.neo4j.tutorial;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.MapUtil;

import static java.lang.String.format;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;
import static org.neo4j.tutorial.DoctorWhoLabels.PLANET;

public class PlanetBuilder
{
    private static final String queryString = format( "MERGE (n:%s {planet: {planet}}) RETURN n", PLANET.name() );
    private final Set<String> planetNames = new HashSet<>();

    public static PlanetBuilder planets()
    {
        return new PlanetBuilder();
    }

    private PlanetBuilder()
    {
    }

    public PlanetBuilder add( String planet )
    {
        planetNames.add( planet );
        return this;
    }

    public void commit( GraphDatabaseService db )
    {
        final ExecutionEngine engine = new ExecutionEngine( db, DEV_NULL );
        try ( Transaction tx = db.beginTx() )
        {
            for ( String planetName : planetNames )
            {
                create( MapUtil.map( "planet", planetName ), engine );
            }
            tx.success();
        }
    }

    private static void create( Map<String, Object> parameters, ExecutionEngine engine )
    {
        engine.execute( queryString, parameters );
    }
}
