package org.neo4j.tutorial.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.AutoIndexer;

import java.util.Set;

public class CharacterAutoIndexContainsSpecificCharacters extends TypeSafeMatcher<AutoIndexer<Node>>
{

    private final Set<String> characterNames;
    private String failedCharacterName;

    private CharacterAutoIndexContainsSpecificCharacters(Set<String> characterNames)
    {
        this.characterNames = characterNames;
    }

    public void describeTo(Description description)
    {
        description.appendText(String.format(
                "The presented arguments did not contain all the supplied character names. Missing [%s].",
                failedCharacterName));
    }

    @Override
    public boolean matchesSafely(AutoIndexer<Node> characters)
    {
        for (String name : characterNames)
        {
            if (characters.getAutoIndex()
                          .get("character-name", name)
                          .getSingle() == null)
            {
                failedCharacterName = name;
                return false;
            }
        }

        return true;
    }

    @Factory
    public static Matcher<AutoIndexer<Node>> containsSpecificCharacters(Set<String> allCharacterNames)
    {
        return new CharacterAutoIndexContainsSpecificCharacters(allCharacterNames);
    }
}
