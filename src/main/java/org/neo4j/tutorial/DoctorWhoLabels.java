package org.neo4j.tutorial;

import org.neo4j.graphdb.Label;

public class DoctorWhoLabels
{
    public static final Label ACTOR = createLabel( "Actor" );
    public static final Label SPECIES = createLabel( "Species" );
    public static final Label CHARACTER = createLabel( "Character" );
    public static final Label PLANET = createLabel( "Planet" );
    public static final Label EPISODE = createLabel( "Episode" );
    public static final Label PROP = createLabel( "Prop" );
    public static final Label PROPS = createLabel( "Props" );

    private static Label createLabel( final String label )
    {
        return new Label()
        {
            @Override
            public String name()
            {
                return label;
            }
        };
    }
}
