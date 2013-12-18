package org.neo4j.tutorial;

import org.neo4j.graphdb.Label;

import static org.neo4j.graphdb.DynamicLabel.label;

public class DoctorWhoLabels
{
    public static final Label ACTOR = label( "Actor" );
    public static final Label CHARACTER = label( "Character" );
    public static final Label EPISODE = label( "Episode" );
    public static final Label THING = label( "Thing" );
    public static final Label PLANET = label( "Planet" );
    public static final Label PROP = label( "Prop" );
    public static final Label SPECIES = label( "Species" );
    public static final Label PART = label( "Part" );
}
