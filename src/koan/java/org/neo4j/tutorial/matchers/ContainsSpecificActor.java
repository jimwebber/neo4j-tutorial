package org.neo4j.tutorial.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.neo4j.graphdb.Node;

public class ContainsSpecificActor extends TypeSafeMatcher<Iterable<Node>> {

    private final String actorName;

    private ContainsSpecificActor(String actorName) {
        this.actorName = actorName;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Checks whether each index in the presented arguments contains the supplied companion names.");
    }

    @Override
    public boolean matchesSafely(Iterable<Node> actors) {
        boolean foundActor = false;
        int actorCount = 0;
        for(Node actor : actors) {
            actorCount ++;
            if(actor.getProperty("actor").equals(actorName)) {
                foundActor = true;
            }
        }
        
        return foundActor && actorCount == 1;
    }
    
    @Factory
    public static <T> Matcher<Iterable<Node>> containsOnly(String actorName) {
      return new ContainsSpecificActor(actorName);
    }
}