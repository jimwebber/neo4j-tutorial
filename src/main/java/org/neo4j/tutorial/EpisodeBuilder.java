package org.neo4j.tutorial;

import static org.neo4j.tutorial.CharacterBuilder.ensureAllyOfRelationshipInDb;
import static org.neo4j.tutorial.CharacterBuilder.ensureCompanionRelationshipInDb;
import static org.neo4j.tutorial.CharacterBuilder.ensureEnemyOfRelationshipInDb;
import static org.neo4j.tutorial.DatabaseHelper.ensureRelationshipInDb;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class EpisodeBuilder {

    private String title;
    private List<String> companionNames = new ArrayList<String>();
    private int episodeNumber = 0;
    private List<String> doctorActors = new ArrayList<String>();
    private List<String> enemySpecies = new ArrayList<String>();
    private List<String> enemies = new ArrayList<String>();
    private String[] allies;

    private EpisodeBuilder(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public static EpisodeBuilder episode(int episodeNumber) {
        return new EpisodeBuilder(episodeNumber);
    }

    public EpisodeBuilder title(String title) {
        this.title = title;
        return this;
    }

    public EpisodeBuilder doctor(String actorName) {
        doctorActors.add(actorName);
        return this;
    }

    public EpisodeBuilder companion(String... namesOfCompanions) {
        for (String str : namesOfCompanions) {
            companionNames.add(str);
        }
        return this;
    }

    public EpisodeBuilder enemySpecies(String... enemySpecies) {
        for (String str : enemySpecies) {
            this.enemySpecies.add(str);
        }
        return this;
    }

    public EpisodeBuilder enemy(String... enemies) {
        for (String str : enemies) {
            this.enemies.add(str);
        }
        return this;
    }

    public void fact(DoctorWhoUniverse universe) {
        checkEpisodeNumberAndTitle();

        Node episode = ensureEpisodeNodeInDb(universe);

        ensureDoctorActorsAreInDb(universe, episode);

        if (this.companionNames != null) {
            for (String companionName : companionNames) {
                Node companionNode = CharacterBuilder.ensureCharacterIsInDb(companionName, universe);
                companionNode.createRelationshipTo(episode, DoctorWhoUniverse.APPEARED_IN);
                ensureCompanionRelationshipInDb(companionNode, universe);
            }
        }

        if (this.enemySpecies != null) {
            for (String eSpecies : enemySpecies) {
                Node speciesNode = SpeciesBuilder.ensureSpeciesInDb(eSpecies, universe);
                speciesNode.createRelationshipTo(episode, DoctorWhoUniverse.APPEARED_IN);
                ensureEnemyOfRelationshipInDb(speciesNode, universe);
            }
        }

        if (this.enemies != null) {
            for (String enemy : enemies) {
                Node enemyNode = CharacterBuilder.ensureCharacterIsInDb(enemy, universe);
                enemyNode.createRelationshipTo(episode, DoctorWhoUniverse.APPEARED_IN);
                ensureEnemyOfRelationshipInDb(enemyNode, universe);
            }
        }
        
        if(this.allies != null) {
            for(String ally : allies) {
                Node allyNode = CharacterBuilder.ensureCharacterIsInDb(ally, universe);
                allyNode.createRelationshipTo(episode, DoctorWhoUniverse.APPEARED_IN);
                ensureAllyOfRelationshipInDb(allyNode, universe);                
            }
        }
    }


    private void ensureDoctorActorsAreInDb(DoctorWhoUniverse universe, Node episode) {
        if (doctorActors != null) {
            for (String actor : doctorActors) {
                Node actorNode = ensureDoctorActorInDb(actor, universe);
                ensureRelationshipInDb(actorNode, DoctorWhoUniverse.APPEARED_IN, episode);
            }
        }
    }

    private Node ensureEpisodeNodeInDb(DoctorWhoUniverse universe) {
        GraphDatabaseService db = universe.getDatabase();
       
        Node episode = universe.episodeIndex.get("title", this.title).getSingle();
        
        if (episode == null) {
            episode = db.createNode();
            episode.setProperty("episode", episodeNumber);
            episode.setProperty("title", title);
        }

        universe.episodeIndex.add(episode, "title", title);
        universe.episodeIndex.add(episode, "episode", episodeNumber);
        
        return episode;
    }

    private void checkEpisodeNumberAndTitle() {
        if (title == null) {
            throw new RuntimeException("Episodes must have a title");
        }

        if (episodeNumber < 1) {
            throw new RuntimeException("Episodes must have a number");
        }
    }

    private Node ensureDoctorActorInDb(String doctorActor, DoctorWhoUniverse universe) {
        Node theDoctor = universe.theDoctor();
        Iterable<Relationship> relationships = theDoctor.getRelationships(DoctorWhoUniverse.PLAYED, Direction.INCOMING);

        for (Relationship r : relationships) {
            Node current = r.getStartNode();
            if (current.getProperty("actor").equals(doctorActor)) {
                return current;
            }
        }

        Node doctorActorNode = universe.getDatabase().createNode();
        doctorActorNode.setProperty("actor", doctorActor);
        doctorActorNode.createRelationshipTo(theDoctor, DoctorWhoUniverse.PLAYED);
        return doctorActorNode;
    }

    public EpisodeBuilder allies(String... allies) {
        this.allies = allies;
        return this;
    }
}
