package org.neo4j.tutorial.matchers;

import java.util.HashSet;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.neo4j.graphdb.Node;

public class ContainsOnlySpecificTitles extends TypeSafeMatcher<Set<Node>> {

    private final Set<String> titles;

    public ContainsOnlySpecificTitles(String... specificTitles) {
        this.titles = new HashSet<String>();
        for (String name : specificTitles) {
            titles.add(name);
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Checks whether each member in the supplied list of titles is present in the presented arguments.");
    }

    @Override
    public boolean matchesSafely(Set<Node> indexHits) {
        
        for (Node n : indexHits) {
            String property = String.valueOf(n.getProperty("title"));
            
            if (!titles.contains(property)) {
                return false;
            } 
            titles.remove(property);
        }

        return titles.size() == 0;
    }

    @Factory
    public static <T> Matcher<Set<Node>> containsOnly(String... titles) {
        return new ContainsOnlySpecificTitles(titles);
    }
}
