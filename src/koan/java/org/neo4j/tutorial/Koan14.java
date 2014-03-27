package org.neo4j.tutorial;

import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.collection.convert.Wrappers;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;

import static org.junit.Assert.assertThat;

import static org.neo4j.helpers.collection.IteratorUtil.asIterable;
import static org.neo4j.helpers.collection.IteratorUtil.asSet;
import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;

/**
 * In this koan we learn about breaking apart queries using WITH
 */
public class Koan14
{
    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception
    {
        universe = new EmbeddedDoctorWhoUniverse( new DoctorWhoUniverseGenerator().getDatabase() );
    }

    @AfterClass
    public static void closeTheDatabase()
    {
        universe.stop();
    }

    @Test
    public void shouldSortCompanionsAlphabeticallyIntoACollection() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = "MATCH (c:Character)-[:COMPANION_OF]->(:Character {character: 'Doctor'})\n";

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql += "WITH c\n" +
                "ORDER BY c.character\n" +
                "RETURN collect(c.character) AS characters\n";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        assertThat( result, containsOrderedList( "Ace", "Adam Mitchell", "Adelaide Brooke", "Adric", "Amy Pond",
                "Astrid Peth", "Barbara Wright", "Ben Jackson", "Clara Oswald", "Craig Owens", "Dodo Chaplet",
                "Donna Noble", "Grace Holloway", "Hamish Wilson", "Harry Sullivan", "Ian Chesterton",
                "Jack Harkness", "Jackson Lake", "Jamie McCrimmon", "Jo Grant", "K9", "Kamelion", "Katarina",
                "Lady Christina de Souza", "Leela", "Liz Shaw", "Martha Jones", "Melanie Bush", "Mickey Smith",
                "Nyssa", "Peri Brown", "Polly", "River Song", "Romana", "Rory Williams", "Rose Tyler",
                "Rosita Farisi", "Sara Kingdom", "Sarah Jane Smith", "Steven Taylor", "Susan Foreman",
                "Tegan Jovanka", "Vicki", "Victoria Waterfield", "Vislor Turlough", "Wilfred Mott", "Zoe Heriot" ) );
    }

    @Test
    public void shouldFindThePopularCompanionsWhoApprearedMoreThanTwentyTimes() throws Exception
    {
        ExecutionEngine engine = new ExecutionEngine( universe.getDatabase(), DEV_NULL );
        String cql = "MATCH (ep:Episode)<-[:APPEARED_IN]-(companion:Character)" +
                "-[:COMPANION_OF]->(:Character {character: 'Doctor'})\n";

        // YOUR CODE GOES HERE
        // SNIPPET_START

        cql += "WITH companion, count(ep) AS numberOfEpisodes\n" +
                "WHERE numberOfEpisodes > 20\n" +
                "RETURN companion.character AS companions";

        // SNIPPET_END

        ExecutionResult result = engine.execute( cql );

        assertThat( result, containsOnlyCompanions( "Rory Williams", "Amy Pond", "Sarah Jane Smith", "Rose Tyler",
                "Jamie McCrimmon" ) );
    }

    private TypeSafeMatcher<ExecutionResult> containsOrderedList( final String... companions )
    {
        return new TypeSafeMatcher<ExecutionResult>()
        {
            @Override
            protected boolean matchesSafely( ExecutionResult result )
            {
                Wrappers.SeqWrapper characters = (Wrappers.SeqWrapper) result.javaColumnAs( "characters" ).next();

                for ( int i = 0; i < companions.length; i++ )
                {
                    if ( !companions[i].equals( characters.get( i ) ) )
                    {
                        return false;
                    }
                }

                return true;
            }

            @Override
            public void describeTo( Description description )
            {
                description.appendText( "" );
            }
        };
    }

    private TypeSafeMatcher<ExecutionResult> containsOnlyCompanions( final String... companions )
    {
        return new TypeSafeMatcher<ExecutionResult>()
        {
            private final Set<String> theCompanions = asSet( companions );

            @Override
            protected boolean matchesSafely( ExecutionResult result )
            {

                for ( Object o : asIterable( result.javaColumnAs( "companions" ) ) )
                {
                    if ( !theCompanions.remove( o ) )
                    {
                        return false;
                    }
                }

                return theCompanions.size() == 0;
            }

            @Override
            public void describeTo( Description description )
            {

            }
        };
    }
}
