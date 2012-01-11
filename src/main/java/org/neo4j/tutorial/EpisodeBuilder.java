package org.neo4j.tutorial;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.tutorial.CharacterBuilder.*;
import static org.neo4j.tutorial.DatabaseHelper.ensureRelationshipInDb;

public class EpisodeBuilder
{

    private String title;
    private List<String> companionNames = new ArrayList<String>();
    private String episodeNumber = null;
    private List<String> doctorActors = new ArrayList<String>();
    private List<String> enemySpecies = new ArrayList<String>();
    private List<String> enemies = new ArrayList<String>();
    private String[] allies;
    private List<String> alliedSpecies = new ArrayList<String>();

    private static Node previousEpisode = null;

    public EpisodeBuilder(String episodeNumber)
    {
        this.episodeNumber = episodeNumber;
    }

    public static void reset()
    {
        previousEpisode = null;
    }

    public static EpisodeBuilder episode(int episodeNumber)
    {
        return new EpisodeBuilder(String.valueOf(episodeNumber));
    }

    public static EpisodeBuilder episode(String episodeNumber)
    {
        return new EpisodeBuilder(episodeNumber);
    }

    public EpisodeBuilder title(String title)
    {
        this.title = title;
        return this;
    }

    public EpisodeBuilder doctor(String actorName)
    {
        doctorActors.add(actorName);
        return this;
    }

    public EpisodeBuilder companion(String... namesOfCompanions)
    {
        for (String str : namesOfCompanions)
        {
            companionNames.add(str);
        }
        return this;
    }

    public EpisodeBuilder enemySpecies(String... enemySpecies)
    {
        for (String str : enemySpecies)
        {
            this.enemySpecies.add(str);
        }
        return this;
    }

    public EpisodeBuilder enemy(String... enemies)
    {
        for (String str : enemies)
        {
            this.enemies.add(str);
        }
        return this;
    }

    public void fact(GraphDatabaseService db)
    {
        checkEpisodeNumberAndTitle();

        Node episode = ensureEpisodeNodeInDb(db);

        ensureDoctorActorsAreInDb(db, episode);

        if (this.companionNames != null)
        {
            for (String companionName : companionNames)
            {
                Node companionNode = CharacterBuilder.ensureCharacterIsInDb(companionName, db);
                companionNode.createRelationshipTo(episode, DoctorWhoRelationships.APPEARED_IN);
                ensureCompanionRelationshipInDb(companionNode, db);
            }
        }

        if (this.enemySpecies != null)
        {
            for (String eSpecies : enemySpecies)
            {
                Node speciesNode = SpeciesBuilder.ensureSpeciesInDb(eSpecies, db);
                speciesNode.createRelationshipTo(episode, DoctorWhoRelationships.APPEARED_IN);
                ensureEnemyOfRelationshipInDb(speciesNode, db);
            }
        }

        if (this.enemies != null)
        {
            for (String enemy : enemies)
            {
                Node enemyNode = CharacterBuilder.ensureCharacterIsInDb(enemy, db);
                enemyNode.createRelationshipTo(episode, DoctorWhoRelationships.APPEARED_IN);
                ensureEnemyOfRelationshipInDb(enemyNode, db);
            }
        }

        if (this.allies != null)
        {
            for (String ally : allies)
            {
                Node allyNode = CharacterBuilder.ensureCharacterIsInDb(ally, db);
                allyNode.createRelationshipTo(episode, DoctorWhoRelationships.APPEARED_IN);
                ensureAllyOfRelationshipInDb(allyNode, db);
            }
        }

        if (this.alliedSpecies != null)
        {
            for (String aSpecies : alliedSpecies)
            {
                Node speciesNode = SpeciesBuilder.ensureSpeciesInDb(aSpecies, db);
                speciesNode.createRelationshipTo(episode, DoctorWhoRelationships.APPEARED_IN);
                ensureAllyOfRelationshipInDb(speciesNode, db);
            }
        }

        linkToPrevious(episode, db);
    }

    private void linkToPrevious(Node episode, GraphDatabaseService db)
    {
        if (previousEpisode != null)
        {
            previousEpisode.createRelationshipTo(episode, DoctorWhoRelationships.NEXT);
            episode.createRelationshipTo(previousEpisode, DoctorWhoRelationships.PREVIOUS);
        }

        previousEpisode = episode;
    }

    private void ensureDoctorActorsAreInDb(GraphDatabaseService db, Node episode)
    {
        if (doctorActors != null)
        {
            for (String actor : doctorActors)
            {
                Node actorNode = ensureDoctorActorInDb(actor, db);
                ensureRelationshipInDb(actorNode, DoctorWhoRelationships.APPEARED_IN, episode);
            }
        }
    }

    private Node ensureEpisodeNodeInDb(GraphDatabaseService db)
    {
        Node episode = db.index()
                         .forNodes("episodes")
                         .get("title", this.title)
                         .getSingle();

        if (episode == null)
        {
            episode = db.createNode();
            episode.setProperty("episode", episodeNumber);
            episode.setProperty("title", title);
        }

        db.index()
          .forNodes("episodes")
          .add(episode, "title", title);
        db.index()
          .forNodes("episodes")
          .add(episode, "episode", episodeNumber);

        return episode;
    }

    private void checkEpisodeNumberAndTitle()
    {
        if (title == null)
        {
            throw new RuntimeException("Episodes must have a title");
        }

        if (episodeNumber == null)
        {
            throw new RuntimeException("Episodes must have a number");
        }
    }

    private Node ensureDoctorActorInDb(String doctorActor, GraphDatabaseService db)
    {
        Node theDoctor = db.index()
                           .forNodes("characters")
                           .get("character", "Doctor")
                           .getSingle();
        Iterable<Relationship> relationships = theDoctor.getRelationships(DoctorWhoRelationships.PLAYED,
                                                                          Direction.INCOMING);

        for (Relationship r : relationships)
        {
            Node current = r.getStartNode();
            if (current.getProperty("actor")
                       .equals(doctorActor))
            {
                return current;
            }
        }

        Node doctorActorNode = db.createNode();
        doctorActorNode.setProperty("actor", doctorActor);
        doctorActorNode.createRelationshipTo(theDoctor, DoctorWhoRelationships.PLAYED);
        db.index()
          .forNodes("actors")
          .add(doctorActorNode, "actor", doctorActor);
        return doctorActorNode;
    }

    public EpisodeBuilder allies(String... allies)
    {
        this.allies = allies;
        return this;
    }

    public EpisodeBuilder alliedSpecies(String... alliedSpecies)
    {
        for (String str : alliedSpecies)
        {
            this.alliedSpecies.add(str);
        }
        return this;
    }
}
