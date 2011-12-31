package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import java.util.*;

import static org.neo4j.tutorial.DatabaseHelper.ensureRelationshipInDb;

public class CharacterBuilder
{
    private final String characterName;
    private HashSet<String> species;
    private boolean companion = false;
    private String[] loverNames;
    private String planet;
    private String[] things;
    private boolean enemy;
    private boolean ally;
    private ArrayList<String> actors = new ArrayList<String>();
    private HashMap<String, Integer> startDates = new HashMap<String, Integer>();
    private String wikipediaUri;

    public static CharacterBuilder character( String characterName )
    {
        return new CharacterBuilder( characterName );
    }

    public CharacterBuilder( String characterName )
    {
        this.characterName = characterName;
    }

    public CharacterBuilder isA( String speciesString )
    {
        if ( species == null )
        {
            species = new HashSet<String>();
        }
        species.add( speciesString );
        return this;
    }

    public CharacterBuilder isCompanion()
    {
        companion = true;
        return this;
    }

    public void fact( GraphDatabaseService db )
    {
        Node characterNode = ensureCharacterIsInDb( characterName, db );
        Node theDoctor = db.index()
                .forNodes( "characters" )
                .get( "character", "Doctor" )
                .getSingle();

        if ( species != null )
        {
            for ( String speciesString : species )
            {
                ensureRelationshipInDb( characterNode, DoctorWhoRelationships.IS_A,
                        SpeciesBuilder.ensureSpeciesInDb( speciesString, db ) );
            }
        }

        if ( companion )
        {
            ensureCompanionRelationshipInDb( characterNode, db );
        }

        if ( enemy )
        {
            ensureEnemyOfRelationshipInDb( characterNode, db );
        }

        if ( ally )
        {
            ensureRelationshipInDb( characterNode, DoctorWhoRelationships.ALLY_OF, theDoctor );
        }

        if ( loverNames != null )
        {
            ensureLoversInDb( characterNode, loverNames, db );
        }

        if ( planet != null )
        {
            ensurePlanetInDb( characterNode, planet, db );
        }

        if ( things != null )
        {
            ensureThingsInDb( characterNode, things, db );
        }

        if ( actors != null )
        {
            ensureActorsInDb( characterNode, actors, db );
        }
        
        if(wikipediaUri != null) {
            characterNode.setProperty("wikipedia", wikipediaUri);
        }
    }

    public static void ensureAllyOfRelationshipInDb( Node allyNode, GraphDatabaseService db )
    {
        Node theDoctor = db.index()
                .forNodes( "characters" )
                .get( "character", "Doctor" )
                .getSingle();
        ensureRelationshipInDb( allyNode, DoctorWhoRelationships.ALLY_OF, theDoctor );
        ensureRelationshipInDb( theDoctor, DoctorWhoRelationships.ALLY_OF, allyNode );
    }

    public static void ensureEnemyOfRelationshipInDb( Node enemyNode, GraphDatabaseService db )
    {
        Node theDoctor = db.index()
                .forNodes( "characters" )
                .get( "character", "Doctor" )
                .getSingle();
        ensureRelationshipInDb( enemyNode, DoctorWhoRelationships.ENEMY_OF, theDoctor );
        ensureRelationshipInDb( theDoctor, DoctorWhoRelationships.ENEMY_OF, enemyNode );
    }

    public static void ensureCompanionRelationshipInDb( Node companionNode, GraphDatabaseService db )
    {
        Node theDoctor = db.index()
                .forNodes( "characters" )
                .get( "character", "Doctor" )
                .getSingle();
        ensureRelationshipInDb( companionNode, DoctorWhoRelationships.COMPANION_OF, theDoctor );
    }

    public void ensureActorsInDb( Node characterNode, List<String> actors, GraphDatabaseService db )
    {
        Node previousActorNode = null;
        for ( String actor : actors )
        {
            Node theActorNode = db.index()
                    .forNodes( "actors" )
                    .get( "actor", actor )
                    .getSingle();
            if ( theActorNode == null )
            {
                theActorNode = db.createNode();
                theActorNode.setProperty( "actor", actor );
                db.index()
                        .forNodes( "actors" )
                        .add( theActorNode, "actor", actor );
            }

            ensureRelationshipInDb( theActorNode, DoctorWhoRelationships.PLAYED, characterNode );
            db.index()
                    .forNodes( "actors" )
                    .add( theActorNode, "actor", actor );

            if ( previousActorNode != null )
            {
                ensureRelationshipInDb( previousActorNode, DoctorWhoRelationships.REGENERATED_TO, theActorNode,
                        map( "year", startDates.get( actor ) ) );
            }

            previousActorNode = theActorNode;
        }
    }

    private Map<String, Object> map( String key, Integer value )
    {

        HashMap<String, Object> result = new HashMap<String, Object>();

        if ( value != null )
        {
            result.put( key, value );
        }

        return result;
    }

    private static void ensureThingsInDb( Node characterNode, String[] things, GraphDatabaseService db )
    {
        for ( String thing : things )
        {
            ensureRelationshipInDb( characterNode, DoctorWhoRelationships.OWNS, ensureThingInDb( thing, db ) );
        }
    }

    private static Node ensureThingInDb( String thing, GraphDatabaseService database )
    {
        Node theThingNode = database.index()
                .forNodes( "things" )
                .get( "thing", thing )
                .getSingle();
        if ( theThingNode == null )
        {
            theThingNode = database.createNode();
            theThingNode.setProperty( "thing", thing );
            ensureThingIsIndexed( theThingNode, database );
        }

        return theThingNode;
    }

    private static void ensureThingIsIndexed( Node thingNode, GraphDatabaseService database )
    {
        database.index()
                .forNodes( "things" )
                .add( thingNode, "thing", thingNode.getProperty( "thing" ) );
    }

    private static Node ensurePlanetInDb( Node characterNode, String planet, GraphDatabaseService database )
    {
        Node thePlanetNode = database.index()
                .forNodes( "planets" )
                .get( "planet", planet )
                .getSingle();
        if ( thePlanetNode == null )
        {
            thePlanetNode = database.createNode();
            thePlanetNode.setProperty( "planet", planet );
            ensurePlanetIsIndexed( thePlanetNode, database );
        }

        ensureRelationshipInDb( characterNode, DoctorWhoRelationships.COMES_FROM, thePlanetNode );

        return thePlanetNode;
    }

    private static void ensurePlanetIsIndexed( Node thePlanetNode, GraphDatabaseService database )
    {
        database.index()
                .forNodes( "planets" )
                .add( thePlanetNode, "planet", thePlanetNode.getProperty( "planet" ) );
    }

    public static Node ensureCharacterIsInDb( String name, GraphDatabaseService db )
    {
        Node theCharacterNode = db.index()
                .forNodes( "characters" )
                .get( "character", name )
                .getSingle();
        if ( theCharacterNode == null )
        {
            theCharacterNode = db.createNode();
            theCharacterNode.setProperty( "character", name );
            ensureCharacterIsIndexed( theCharacterNode, db );
        }
        return theCharacterNode;
    }

    private static void ensureCharacterIsIndexed( Node characterNode, GraphDatabaseService database )
    {
        if ( database.index()
                .forNodes( "characters" )
                .get( "character", characterNode.getProperty( "character" ) )
                .getSingle() == null )
        {
            database.index()
                    .forNodes( "characters" )
                    .add( characterNode, "character", characterNode.getProperty( "character" ) );
        }
    }

    private static void ensureLoversInDb( Node characterNode, String[] loverNames, GraphDatabaseService db )
    {
        for ( String lover : loverNames )
        {
            ensureRelationshipInDb( characterNode, DoctorWhoRelationships.LOVES, ensureCharacterIsInDb( lover, db ) );
        }
    }

    public CharacterBuilder loves( String... loverNames )
    {
        this.loverNames = loverNames;
        return this;
    }

    public CharacterBuilder isFrom( String planet )
    {
        this.planet = planet;
        return this;
    }

    public CharacterBuilder owns( String... things )
    {
        this.things = things;
        return this;
    }

    public CharacterBuilder isEnemy()
    {
        this.enemy = true;
        return this;
    }

    public CharacterBuilder isAlly()
    {
        ally = true;
        return this;
    }

    public CharacterBuilder regeneration( String... actors )
    {
        for ( String actor : actors )
        {
            this.actors.add( actor );
        }
        return this;
    }

    public CharacterBuilder regeneration( String actor, int year )
    {
        this.actors.add( actor );
        this.startDates.put( actor, new Integer( year ) );
        return this;
    }

    public CharacterBuilder wikipedia(String wikipediaEntry) {
        this.wikipediaUri = wikipediaEntry;
        return this;
    }
}
