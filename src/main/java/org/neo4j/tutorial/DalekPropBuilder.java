package org.neo4j.tutorial;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;

import static org.neo4j.tutorial.ShortIdGenerator.shortId;

public class DalekPropBuilder
{
    private static final String PROP_KEY = "prop";
    private final String episode;
    private Set<Prop> props = new HashSet<>();
    private Set<Skirt> skirts = new HashSet<>();
    private Set<Shoulder> shoulders = new HashSet<>();
    private Set<String> operators = new HashSet<>();
    private Set<String> voices = new HashSet<>();

    public static DalekPropBuilder dalekProps( String episode )
    {
        return new DalekPropBuilder( episode );
    }

    private DalekPropBuilder( String episode )
    {
        this.episode = episode;
    }

    public DalekPropBuilder addProp( Shoulder shoulder, Skirt skirt, String name )
    {
        props.add( new Prop( shoulder, skirt, name ) );
        return this;
    }

    public DalekPropBuilder addSkirt( Skirt skirt )
    {
        this.skirts.add( skirt );
        return this;
    }

    /**
     * This is a funky pattern. We need uniqueness when we create dalek parts,
     * but in reality there is nothing uniquely identifying about them. So we add
     * a synthetic property for uniqueness which simplifies creation massively.
     * <p/>
     * Ditto for the props aggregator nodes - they're just places where props,
     * voice actors, and operators come together.
     * <p/>
     * In this method we quickly whizz through the database and remove those synthetic
     * uniqueness properties (transient_id)
     */
    public static void cleanUp( GraphDatabaseService db )
    {
        db.execute( "MATCH (part:Part), (props:Props) REMOVE part.transient_id, props.transient_id" );
    }

    public DalekPropBuilder operators( String... operators )
    {
        Collections.addAll( this.operators, operators );
        return this;
    }

    public DalekPropBuilder voices( String... voices )
    {
        Collections.addAll( this.voices, voices );
        return this;
    }

    private static class Prop
    {
        private Shoulder shoulder;
        private Skirt skirt;
        private String name;

        public Prop( Shoulder shoulder, Skirt skirt, String name )
        {
            this.shoulder = shoulder;
            this.skirt = skirt;
            this.name = name;
        }

        public Shoulder getShoulder()
        {
            return shoulder;
        }

        public Skirt getSkirt()
        {
            return skirt;
        }

        public String getName()
        {
            return name;
        }
    }

    public static abstract class Part
    {
        String part;

        @Override
        public boolean equals( Object o )
        {
            if ( o instanceof Part )
            {
                return part.equals( o.toString() );
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return part.hashCode();
        }
    }

    public static class Skirt extends Part
    {
        public static Skirt skirt( String skirt )
        {
            return new Skirt( skirt );
        }

        private Skirt( String skirt )
        {

            this.part = skirt;
        }

        public String toString()
        {
            return part;
        }
    }

    public static class Shoulder extends Part
    {
        public static Shoulder shoulder( String shoulder )
        {
            return new Shoulder( shoulder );
        }

        private Shoulder( String shoulder )
        {

            this.part = shoulder;
        }

        public String toString()
        {
            return part;
        }
    }

    public void fact( GraphDatabaseService db, Set<Part> knownParts )
    {
        StringBuilder sb = new StringBuilder();

        sb.append( lineSeparator() );
        sb.append( format( "MERGE (ep:Episode {title: \"%s\"})", episode ) );
        sb.append( lineSeparator() );
        sb.append( format( "MERGE (dalek:Species {species: \"%s\"})", "Dalek" ) );
        sb.append( lineSeparator() );
        sb.append( "MERGE (dalek)-[:APPEARED_IN]->(ep)" );

        String propsId = shortId( "props" );
        sb.append( lineSeparator() );
        sb.append( format( "MERGE (%s:Props {transient_id: \"%s\"})", propsId, propsId ) );

        for ( Prop prop : props )
        {
            String propId = shortId( "prop" );
            String shoulderId = shortId( "shoulder" );
            String skirtId = shortId( "skirt" );


            sb.append( lineSeparator() );
            sb.append( format( "MERGE (%s)-[:USED_IN]->(ep)", propsId ) );

            sb.append( lineSeparator() );
            sb.append( format( "MERGE (%s:Prop {prop: \"%s\"})", propId, prop.getName() ) );

            sb.append( lineSeparator() );
            sb.append( format( "MERGE (%s)-[:MEMBER_OF]->(%s)", propId, propsId ) );

            Shoulder shoulderPart = prop.getShoulder();
            sb.append( lineSeparator() );
            sb.append( format( "MERGE (%s:Part {transient_id:\"%s\", part: \"%s\"})", shoulderId,
                    shoulderPart.toString(), "shoulder" ) );
            sb.append( lineSeparator() );
            sb.append( format( "MERGE (%s)-[:COMPOSED_OF]->(%s)", propId, shoulderId ) );

            if ( !knownParts.contains( shoulderPart ) )
            {
                sb.append( lineSeparator() );
                sb.append( format( "MERGE (%s)-[:ORIGINAL_PROP]->(%s)", shoulderId, propId ) );
                knownParts.add( shoulderPart );
            }


            Skirt skirtPart = prop.getSkirt();
            sb.append( lineSeparator() );
            sb.append( format( "MERGE (%s:Part {transient_id:\"%s\", part: \"%s\"})", skirtId, skirtPart.toString(),
                    "skirt" ) );
            sb.append( lineSeparator() );
            sb.append( format( "MERGE (%s)-[:COMPOSED_OF]->(%s)", propId, skirtId ) );

            if ( !knownParts.contains( skirtPart ) )
            {
                sb.append( lineSeparator() );
                sb.append( format( "MERGE (%s)-[:ORIGINAL_PROP]->(%s)", skirtId, propId ) );
                knownParts.add( skirtPart );
            }
        }

        for ( String operator : operators )
        {
            String operatorId = shortId( "operator" );

            sb.append( lineSeparator() );
            sb.append( format( "MERGE (%s:Operator {operator: \"%s\"})", operatorId, operator ) );
            sb.append( lineSeparator() );
            sb.append( format( "MERGE (%s)-[:OPERATED]->(%s)", operatorId, propsId ) );
        }

        for ( String voice : voices )
        {
            String voiceId = shortId( "operator" );

            sb.append( lineSeparator() );
            sb.append( format( "MERGE (%s:Voice {voice: \"%s\"})", voiceId, voice ) );
            sb.append( lineSeparator() );
            sb.append( format( "MERGE (%s)-[:VOICED]->(%s)", voiceId, propsId ) );
        }

        db.execute( sb.toString() );
    }
}
