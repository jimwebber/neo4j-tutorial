package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

import static org.neo4j.tutorial.DatabaseHelper.ensureRelationshipInDb;

public class ActorBuilder
{
    private String actorName;
    private String characterName;
    private int cash = -1;

    public static ActorBuilder actor(String actorName)
    {
        return new ActorBuilder(actorName);
    }

    public ActorBuilder(String actorName)
    {
        this.actorName = actorName;
    }

    public ActorBuilder played(String characterName)
    {
        this.characterName = characterName;
        return this;
    }

    public ActorBuilder salary(int cash)
    {
        this.cash = cash;
        return this;
    }

    public void fact(GraphDatabaseService db)
    {
        Node actor = ensureActorIsInDb(db);
        ensureCharacterIsInDb(actor, db);
    }

    private Node ensureActorIsInDb(GraphDatabaseService db)
    {
        Index<Node> index = db.index().forNodes("actors");
        Node actor = index.get("actor", actorName).getSingle();

        if (actor == null)
        {
            actor = db.createNode();
            actor.setProperty("actor", actorName);
            index.add(actor, "actor", actorName);
        }

        if (cash > 0)
        {
            actor.setProperty("salary", cash);
        }

        return actor;
    }

    private void ensureCharacterIsInDb(Node actor, GraphDatabaseService db)
    {
        if (characterName != null)
        {
            new CharacterBuilder(characterName).fact(db);

            Node character = db.index().forNodes("characters").get("character", characterName).getSingle();

            if (actor != null && character != null)
            {
                ensureRelationshipInDb(actor, DoctorWhoRelationships.PLAYED, character);
            }
        }
    }
}
