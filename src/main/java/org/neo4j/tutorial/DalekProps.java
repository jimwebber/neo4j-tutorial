package org.neo4j.tutorial;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import static org.neo4j.kernel.impl.util.StringLogger.DEV_NULL;
import static org.neo4j.tutorial.DalekPropBuilder.Shoulder.shoulder;
import static org.neo4j.tutorial.DalekPropBuilder.Skirt.skirt;
import static org.neo4j.tutorial.DalekPropBuilder.dalekProps;

public class DalekProps
{
    private final GraphDatabaseService db;
    private final ExecutionEngine engine;

    // This is a bit of a hack, we remember parts we've seen in this set, and if we haven't seen them,
    // then we add the original prop relationship
    private Set<DalekPropBuilder.Part> knownParts = new HashSet<>();

    public DalekProps( GraphDatabaseService db )
    {
        this.db = db;
        this.engine = new ExecutionEngine( db, DEV_NULL );
    }

    public void insert()
    {
        try ( Transaction tx = db.beginTx() )
        {
            dalekProps( "The Daleks" )
                    .addProp( shoulder( "Dalek 1" ), skirt( "Dalek 1" ), "Dalek 1" )
                    .addProp( shoulder( "Dalek 2" ), skirt( "Dalek 2" ), "Dalek 2" )
                    .addProp( shoulder( "Dalek 3" ), skirt( "Dalek 3" ), "Dalek 3" )
                    .addProp( shoulder( "Dalek 4" ), skirt( "Dalek 4" ), "Dalek 4" )
                    .operators( "Robert Jewell", "Kevin Manser", "Gerald Taylor", "Michael Summerton", "Peter Murphy" )
                    .voices( "Peter Hawkins", "David Graham" )
                    .fact( engine, knownParts );
            dalekProps( "The Dalek Invasion of Earth" )
                    .addProp( shoulder( "Dalek 1" ), skirt( "Dalek 1" ), "Dalek 1" )
                    .addProp( shoulder( "Dalek 2" ), skirt( "Dalek 2" ), "Dalek 2" )
                    .addProp( shoulder( "Dalek 3" ), skirt( "Dalek 3" ), "Dalek 3" )
                    .addProp( shoulder( "Dalek 4" ), skirt( "Dalek 4" ), "Dalek 4" )
                    .addProp( shoulder( "Dalek 5" ), skirt( "Dalek 5" ), "Dalek 5" )
                    .addProp( shoulder( "Dalek 6" ), skirt( "Dalek 6" ), "Dalek 6" )
                    .operators( "Robert Jewell", "Kevin Manser", "Gerald Taylor", "Nick Evans", "Peter Murphy",
                            "Ken Tyllsen" )
                    .voices( "Peter Hawkins", "David Graham" )
                    .fact( engine, knownParts );
            dalekProps( "The Space Museum" )
                    .addProp( shoulder( "Dalek 1" ), skirt( "Dalek 1" ), "Dalek 1" )
                    .operators( "Murphy Grumbar" )
                    .voices( "Peter Hawkins" )
                    .fact( engine, knownParts );
            dalekProps( "The Chase" )
                    .addProp( shoulder( "Dalek 1" ), skirt( "Dalek 1" ), "Dalek 1" )
                    .addProp( shoulder( "Dalek 2" ), skirt( "Dalek 2" ), "Dalek 2" )
                    .addProp( shoulder( "Dalek 5" ), skirt( "Dalek 5" ), "Dalek 5" )
                    .addProp( shoulder( "Dalek 6" ), skirt( "Dalek 6" ), "Dalek 6" )
                    .addProp( shoulder( "Dalek 7" ), skirt( "Dalek 7" ), "Dalek 7" )
                    .operators( "John Scott Martin", "Robert Jewell", "Kevin Manser", "Gerald Taylor" )
                    .voices( "Peter Hawkins", "David Graham" )
                    .fact( engine, knownParts );
            dalekProps( "The Daleks' Master Plan" )
                    .addProp( shoulder( "Dalek 1" ), skirt( "Dalek 1" ), "Dalek 1" )
                    .addProp( shoulder( "Dalek 2" ), skirt( "Dalek 2" ), "Dalek 2" )
                    .addProp( shoulder( "Dalek 5" ), skirt( "Dalek 5" ), "Dalek 5" )
                    .addProp( shoulder( "Dalek 6" ), skirt( "Dalek 6" ), "Dalek 6" )
                    .operators( "John Scott Martin", "Robert Jewell", "Kevin Manser", "Gerald Taylor" )
                    .voices( "Peter Hawkins", "David Graham" )
                    .fact( engine, knownParts );
            dalekProps( "The Power of the Daleks" )
                    .addProp( shoulder( "Dalek 1" ), skirt( "Dalek 1" ), "Dalek 1" )
                    .addProp( shoulder( "Dalek 2" ), skirt( "Dalek 2" ), "Dalek 2" )
                    .addProp( shoulder( "Dalek 6" ), skirt( "Dalek 5" ), "Dalek Six-5" )
                    .addProp( shoulder( "Dalek 7" ), skirt( "Dalek 7" ), "Dalek 7" )
                    .operators( "John Scott Martin", "Robert Jewell", "Kevin Manser", "Gerald Taylor" )
                    .voices( "Peter Hawkins" )
                    .fact( engine, knownParts );
            dalekProps( "The Evil of the Daleks" )
                    .addProp( shoulder( "Dalek 2" ), skirt( "Dalek 1" ), "Dalek Two-1" )
                    .addProp( shoulder( "Dalek 5" ), skirt( "Dalek 6" ), "Dalek Five-6" )
                    .addProp( shoulder( "Dalek 6" ), skirt( "Dalek 5" ), "Dalek Six-5" )
                    .addProp( shoulder( "Dalek 7" ), skirt( "Dalek 7" ), "Dalek 7" )
                    .addProp( shoulder( "Dalek 8" ), skirt( "Dalek 8" ), "Dalek 8" )
                    .addSkirt( skirt( "Dalek 2" ) )
                    .operators( "John Scott Martin", "Robert Jewell", "Kevin Manser", "Gerald Taylor" )
                    .voices( "Peter Hawkins" )
                    .fact( engine, knownParts );
            dalekProps( "The War Games" )
                    .addProp( shoulder( "Dalek 7" ), skirt( "Dalek 8" ), "Dalek Seven-8" )
                    .fact( engine, knownParts );
            dalekProps( "Day of the Daleks" )
                    .addProp( shoulder( "Dalek 7" ), skirt( "Dalek 2" ), "Dalek Seven-2" )
                    .addProp( shoulder( "Dalek 1" ), skirt( "Dalek 5" ), "Dalek One-5" )
                    .addProp( shoulder( "Dalek 6" ), skirt( "Dalek 7" ), "Dalek Six-7" )
                    .addSkirt( skirt( "Dalek 1" ) )
                    .operators( "John Scott Martin", "Ricky Newby", "Murphy Grumbar" )
                    .voices( "Oliver Gilbert", "Peter Messaline" )
                    .fact( engine, knownParts );
            dalekProps( "Frontier in Space" )
                    .addProp( shoulder( "Dalek 7" ), skirt( "Dalek 2" ), "Dalek Seven-2" )
                    .addProp( shoulder( "Dalek 1" ), skirt( "Dalek 5" ), "Dalek One-5" )
                    .addProp( shoulder( "Dalek 6" ), skirt( "Dalek 7" ), "Dalek Six-7" )
                    .operators( "John Scott Martin", "Cy Town", "Murphy Grumbar" )
                    .voices( "Michael Wisher" )
                    .fact( engine, knownParts );
            dalekProps( "Planet of the Daleks" )
                    .addProp( shoulder( "Dalek 1" ), skirt( "Dalek 5" ), "Dalek One-5" )
                    .addProp( shoulder( "Dalek 7" ), skirt( "Dalek 2" ), "Dalek Seven-2" )
                    .addProp( shoulder( "Dalek 6" ), skirt( "Dalek 7" ), "Dalek Six-7" )
                    .addProp( shoulder( "Gold Movie" ), skirt( "Gold Movie" ), "Gold Movie Dalek" )
                    .addProp( shoulder( "Goon I" ), skirt( "Goon I" ), "Goon I" )
                    .addProp( shoulder( "Goon II" ), skirt( "Goon II" ), "Goon II" )
                    .addProp( shoulder( "Goon III" ), skirt( "Goon III" ), "Goon III" )
                    .addProp( shoulder( "Goon IV" ), skirt( "Goon IV" ), "Goon IV" )
                    .addProp( shoulder( "Goon V" ), skirt( "Goon V" ), "Goon V" )
                    .addProp( shoulder( "Goon VI" ), skirt( "Goon VI" ), "Goon VI" )
                    .addProp( shoulder( "Goon VII" ), skirt( "Goon VII" ), "Goon VII" )
                    .operators( "John Scott Martin", "Cy Town", "Murphy Grumbar", "Tony Starr" )
                    .voices( "Michael Wisher", "Roy Skelton" )
                    .fact( engine, knownParts );
            dalekProps( "Death to the Daleks" )
                    .addProp( shoulder( "Dalek 1" ), skirt( "Dalek 7" ), "Dalek One-7" )
                    .addProp( shoulder( "Dalek 7" ), skirt( "Dalek 2" ), "Dalek Seven-2" )
                    .addProp( shoulder( "Dalek 6" ), skirt( "Dalek 5" ), "Dalek Six-5" )
                    .addProp( shoulder( "Goon I" ), skirt( "Goon I" ), "Goon I" )
                    .addProp( shoulder( "Goon VII" ), skirt( "Goon VII" ), "Goon VII" )
                    .addProp( shoulder( "Goon III" ), skirt( "Goon III" ), "Goon III" )
                    .operators( "John Scott Martin", "Cy Town", "Murphy Grumbar" )
                    .voices( "Michael Wisher" )
                    .fact( engine, knownParts );
            dalekProps( "Genesis of the Daleks" )
                    .addProp( shoulder( "Dalek 1" ), skirt( "Dalek 7" ), "Dalek One-7" )
                    .addProp( shoulder( "Dalek 7" ), skirt( "Dalek 2" ), "Dalek Seven-2" )
                    .addProp( shoulder( "Dalek 6" ), skirt( "Dalek 5" ), "Dalek Six-5" )
                    .addProp( shoulder( "Goon I" ), skirt( "Goon I" ), "Goon I" )
                    .addProp( shoulder( "Goon II" ), skirt( "Goon II" ), "Goon II" )
                    .addProp( shoulder( "Goon IV" ), skirt( "Goon IV" ), "Goon IV" )
                    .addProp( shoulder( "Goon V" ), skirt( "Goon VI" ), "Dalek V-VI" )
                    .operators( "John Scott Martin", "Cy Town", "Keith Ashley" )
                    .voices( "Michael Wisher" )
                    .fact( engine, knownParts );
            dalekProps( "Destiny of the Daleks" )
                    .addProp( shoulder( "Dalek 6" ), skirt( "Dalek 5" ), "Dalek Six-5" )
                    .addProp( shoulder( "Dalek 7" ), skirt( "Goon II" ), "Dalek Seven-II" )
                    .addProp( shoulder( "Goon IV" ), skirt( "Exhibition" ), "Dalek IV-Ex" )
                    .addProp( shoulder( "Goon V" ), skirt( "Goon VI" ), "Dalek V-VI" )
                    .operators( "Mike Mungarvan", "Cy Town", "Toby Byrne", "Tony Starr" )
                    .voices( "David Gooderson", "Roy Skelton" )
                    .fact( engine, knownParts );
            dalekProps( "The Five Doctors" )
                    .addProp( shoulder( "Dalek 1" ), skirt( "Dalek 7" ), "Dalek One-7" )
                    .operators( "John Scott Martin" )
                    .voices( "Roy Skelton" )
                    .fact( engine, knownParts );
            dalekProps( "Resurrection of the Daleks" )
                    .addProp( shoulder( "Dalek 1" ), skirt( "Dalek 7" ), "Dalek One-7" )
                    .addProp( shoulder( "Dalek 6" ), skirt( "Exhibition" ), "Dalek Six-Ex" )
                    .addProp( shoulder( "Dalek 7" ), skirt( "Goon V" ), "Dalek Seven-V" )
                    .addProp( shoulder( "Goon V" ), skirt( "Dalek 5" ), "Dalek V-5" )
                    .operators( "John Scott Martin", "Cy Town", "Tony Starr", "Toby Byrne" )
                    .voices( "Royce Mills", "Brian Miller" )
                    .fact( engine, knownParts );
            dalekProps( "Revelation of the Daleks" )
                    .addProp( shoulder( "Dalek 1" ), skirt( "Dalek 7" ), "Dalek One-7" )
                    .addProp( shoulder( "Dalek 6" ), skirt( "Exhibition" ), "Dalek Six-Ex" )
                    .addProp( shoulder( "Dalek 7" ), skirt( "Goon V" ), "Dalek Seven-V" )
                    .addProp( shoulder( "Goon V" ), skirt( "Dalek 5" ), "Dalek V-5" )
                    .addProp( shoulder( "Necros 1" ), skirt( "Necros 1" ), "Necros 1" )
                    .addProp( shoulder( "Necros 2" ), skirt( "Necros 2" ), "Necros 2" )
                    .addProp( shoulder( "Necros 3" ), skirt( "Necros 3" ), "Necros 3" )
                    .operators( "John Scott Martin", "Cy Town", "Tony Starr", "Toby Byrne" )
                    .voices( "Royce Mills", "Roy Skelton" )
                    .fact( engine, knownParts );
            dalekProps( "Remembrance of the Daleks" )
                    .addProp( shoulder( "Dalek 1" ), skirt( "Dalek 7" ), "Dalek One-7" )
                    .addProp( shoulder( "Dalek 7" ), skirt( "Goon V" ), "Dalek Seven-V" )
                    .addProp( shoulder( "Remembrance 1" ), skirt( "Remembrance 1" ), "Remembrance 1" )
                    .addProp( shoulder( "Remembrance 2" ), skirt( "Remembrance 2" ), "Remembrance 2" )
                    .addProp( shoulder( "Remembrance 3" ), skirt( "Remembrance 3" ), "Remembrance 3" )
                    .addProp( shoulder( "Supreme Dalek" ), skirt( "Supreme Dalek" ), "Supreme Dalek" )
                    .addProp( shoulder( "Imperial 1" ), skirt( "Imperial 1" ), "Imperial 1" )
                    .addProp( shoulder( "Imperial 2" ), skirt( "Imperial 2" ), "Imperial 2" )
                    .addProp( shoulder( "Imperial 3" ), skirt( "Imperial 3" ), "Imperial 3" )
                    .addProp( shoulder( "Imperial 4" ), skirt( "Imperial 4" ), "Imperial 4" )
                    .operators( "John Scott Martin", "Cy Town", "Tony Starr", "Hugh Spight", "David Harrison",
                            "Norman Bacon", "Nigel Wild" )
                    .voices( "Royce Mills", "Roy Skelton", "Brian Miller" )
                    .fact( engine, knownParts );

            DalekPropBuilder.cleanUp( engine );

            tx.success();
        }
    }
}
