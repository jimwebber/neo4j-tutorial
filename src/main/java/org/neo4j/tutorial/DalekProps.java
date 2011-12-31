package org.neo4j.tutorial;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import static org.neo4j.tutorial.DalekPropBuilder.dalekProps;

public class DalekProps
{

    private final GraphDatabaseService db;

    public DalekProps(GraphDatabaseService db)
    {
        this.db = db;
    }

    public void insert()
    {
        Transaction tx = db.beginTx();
        try
        {
            dalekProps("The Daleks").addProp("Dalek 1", "Dalek 1", "Dalek 1")
                    .addProp("Dalek 2", "Dalek 2", "Dalek 2")
                    .addProp("Dalek 3", "Dalek 3", "Dalek 3")
                    .addProp("Dalek 4", "Dalek 4", "Dalek 4")
                    .fact(db);
            dalekProps("The Dalek Invasion of Earth").addProp("Dalek 1", "Dalek 1", "Dalek 1")
                    .addProp("Dalek 2", "Dalek 2", "Dalek 2")
                    .addProp("Dalek 3", "Dalek 3", "Dalek 3")
                    .addProp("Dalek 4", "Dalek 4", "Dalek 4")
                    .addProp("Dalek 5", "Dalek 5", "Dalek 5")
                    .addProp("Dalek 6", "Dalek 6", "Dalek 6")
                    .fact(db);
            dalekProps("The Space Museum").addProp("Dalek 1", "Dalek 1", "Dalek 1")
                    .fact(db);
            dalekProps("The Chase").addProp("Dalek 1", "Dalek 1", "Dalek 1")
                    .addProp("Dalek 2", "Dalek 2", "Dalek 2")
                    .addProp("Dalek 5", "Dalek 5", "Dalek 5")
                    .addProp("Dalek 6", "Dalek 6", "Dalek 6")
                    .addProp("Dalek 7", "Dalek 7", "Dalek 7")
                    .fact(db);
            dalekProps("The Daleks' Master Plan").addProp("Dalek 1", "Dalek 1", "Dalek 1")
                    .addProp("Dalek 2", "Dalek 2", "Dalek 2")
                    .addProp("Dalek 5", "Dalek 5", "Dalek 5")
                    .addProp("Dalek 6", "Dalek 6", "Dalek 6")
                    .fact(db);
            dalekProps("The Power of the Daleks").addProp("Dalek 1", "Dalek 1", "Dalek 1")
                    .addProp("Dalek 2", "Dalek 2", "Dalek 2")
                    .addProp("Dalek 6", "Dalek 5", "Dalek Six-5")
                    .addProp("Dalek 7", "Dalek 7", "Dalek 7")
                    .fact(db);
            dalekProps("The Evil of the Daleks").addProp("Dalek 2", "Dalek 1", "Dalek Two-1")
                    .addProp("Dalek 5", "Dalek 6", "Dalek Five-6")
                    .addProp("Dalek 6", "Dalek 5", "Dalek Six-5")
                    .addProp("Dalek 7", "Dalek 7", "Dalek 7")
                    .addProp("Dalek 8", "Dalek 8", "Dalek 8")
                    .addProp(null, "Dalek 2", null)
                    .fact(db);
            dalekProps("The War Games").addProp("Dalek 7", "Dalek 8", "Dalek Seven-8")
                    .fact(db);
            dalekProps("Day of the Daleks").addProp("Dalek 7", "Dalek 2", "Dalek Seven-2")
                    .addProp("Dalek 1", "Dalek 5", "Dalek One-5")
                    .addProp("Dalek 6", "Dalek 7", "Dalek Six-7")
                    .addProp(null, "Dalek 1", null)
                    .fact(db);
            dalekProps("Frontier in Space").addProp("Dalek 7", "Dalek 2", "Dalek Seven-2")
                    .addProp("Dalek 1", "Dalek 5", "Dalek One-5")
                    .addProp("Dalek 6", "Dalek 7", "Dalek Six-7")
                    .fact(db);
            dalekProps("Planet of the Daleks").addProp("Dalek 1", "Dalek 5", "Dalek One-5")
                    .addProp("Dalek 7", "Dalek 2", "Dalek Seven-2")
                    .addProp("Dalek 6", "Dalek 7", "Dalek Six-7")
                    .addProp("Gold Movie", "Gold Movie", "Gold Movie Dalek")
                    .addProp("Goon I", "Goon I", "Goon I")
                    .addProp("Goon II", "Goon II", "Goon II")
                    .addProp("Goon III", "Goon III", "Goon III")
                    .addProp("Goon IV", "Goon IV", "Goon IV")
                    .addProp("Goon V", "Goon V", "Goon V")
                    .addProp("Goon VI", "Goon VI", "Goon VI")
                    .addProp("Goon VII", "Goon VII", "Goon VII")
                    .fact(db);
            dalekProps("Death to the Daleks").addProp("Dalek 1", "Dalek 7", "Dalek One-7")
                    .addProp("Dalek 7", "Dalek 2", "Dalek Seven-2")
                    .addProp("Dalek 6", "Dalek 5", "Dalek Six-5")
                    .addProp("Goon I", "Goon I", "Goon I")
                    .addProp("Goon VII", "Goon VII", "Goon VII")
                    .addProp("Goon III", "Goon III", "Goon III")
                    .fact(db);
            dalekProps("Genesis of the Daleks").addProp("Dalek 1", "Dalek 7", "Dalek One-7")
                    .addProp("Dalek 7", "Dalek 2", "Dalek Seven-2")
                    .addProp("Dalek 6", "Dalek 5", "Dalek Six-5")
                    .addProp("Goon I", "Goon I", "Goon I")
                    .addProp("Goon II", "Goon II", "Goon II")
                    .addProp("Goon IV", "Goon IV", "Goon IV")
                    .addProp("Goon V", "Goon VI", "Dalek V-VI")
                    .fact(db);
            dalekProps("Destiny of the Daleks").addProp("Dalek 6", "Dalek 5", "Dalek Six-5")
                    .addProp("Dalek 7", "Goon II", "Dalek Seven-II")
                    .addProp("Goon IV", "Exhibition", "Dalek IV-Ex")
                    .addProp("Goon V", "Goon VI", "Dalek V-VI")
                    .fact(db);
            dalekProps("The Five Doctors").addProp("Dalek 1", "Dalek 7", "Dalek One-7")
                    .fact(db);
            dalekProps("Resurrection of the Daleks").addProp("Dalek 1", "Dalek 7", "Dalek One-7")
                    .addProp("Dalek 6", "Exhibition", "Dalek Six-Ex")
                    .addProp("Dalek 7", "Goon V", "Dalek Seven-V")
                    .addProp("Goon V", "Dalek 5", "Dalek V-5")
                    .fact(db);
            dalekProps("Revelation of the Daleks").addProp("Dalek 1", "Dalek 7", "Dalek One-7")
                    .addProp("Dalek 6", "Exhibition", "Dalek Six-Ex")
                    .addProp("Dalek 7", "Goon V", "Dalek Seven-V")
                    .addProp("Goon V", "Dalek 5", "Dalek V-5")
                    .addProp("Necros 1", "Necros 1", "Necros 1")
                    .addProp("Necros 2", "Necros 2", "Necros 2")
                    .addProp("Necros 3", "Necros 3", "Necros 3")
                    .fact(db);
            dalekProps("Remembrance of the Daleks").addProp("Dalek 1", "Dalek 7", "Dalek One-7")
                    .addProp("Dalek 7", "Goon V", "Dalek Seven-V")
                    .addProp("Remembrance 1", "Remembrance 1", "Remembrance 1")
                    .addProp("Remembrance 2", "Remembrance 2", "Remembrance 2")
                    .addProp("Remembrance 3", "Remembrance 3", "Remembrance 3")
                    .addProp("Supreme Dalek", "Supreme Dalek", "Supreme Dalek")
                    .addProp("Imperial 1", "Imperial 1", "Imperial 1")
                    .addProp("Imperial 2", "Imperial 2", "Imperial 2")
                    .addProp("Imperial 3", "Imperial 3", "Imperial 3")
                    .addProp("Imperial 4", "Imperial 4", "Imperial 4")
                    .fact(db);
            tx.success();
        } finally
        {
            tx.finish();
        }
    }

}
