package org.neo4j.tutorial;

public class ShortIdGenerator
{
    private static int counter = 0;

    public static String shortId( String prefix )
    {
        return prefix +  ++counter;
    }
}
