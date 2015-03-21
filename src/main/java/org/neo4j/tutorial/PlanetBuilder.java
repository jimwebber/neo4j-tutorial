package org.neo4j.tutorial;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.MapUtil;

import static java.lang.String.format;

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
        try ( Transaction tx = db.beginTx() )
        {
            for ( String planetName : planetNames )
            {
                create( MapUtil.map( "planet", planetName ), db );
            }
            tx.success();
        }
    }

    private static void create( Map<String, Object> parameters,GraphDatabaseService db )
    {
        db.execute( queryString, parameters );
    }
}
