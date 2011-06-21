package org.neo4j.tutorial;

import static org.neo4j.tutorial.DalekPropBuilder.dalekProps;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

public class DalekProps {
	
	private final GraphDatabaseService db;
    
    public DalekProps(GraphDatabaseService db) {
        this.db = db;
    }

    public void insert() {
        Transaction tx = db.beginTx();
        try {
        	dalekProps("The Daleks")
            	.addProp("Dalek One Shoulder", "Dalek One Skirt")
            	.addProp("Dalek Two Shoulder", "Dalek Two Skirt")
            	.addProp("Dalek Three Shoulder", "Dalek Three Skirt")
            	.addProp("Dalek Four Shoulder", "Dalek Four Skirt")
            	.fact(db);
            dalekProps("The Dalek Invasion of Earth")
            	.addProp("Dalek One Shoulder", "Dalek One Skirt")
            	.addProp("Dalek Two Shoulder", "Dalek Two Skirt")
            	.addProp("Dalek Three Shoulder", "Dalek Three Skirt")
            	.addProp("Dalek Four Shoulder", "Dalek Four Skirt")
            	.addProp("Dalek Five Shoulder", "Dalek Five Skirt")
            	.addProp("Dalek Six Shoulder", "Dalek Six Skirt")
            	.fact(db);
            dalekProps("The Space Museum")
        		.addProp("Dalek One Shoulder", "Dalek One Skirt")
        		.fact(db);
            dalekProps("The Chase")
        		.addProp("Dalek One Shoulder", "Dalek One Skirt")
        		.addProp("Dalek Two Shoulder", "Dalek Two Skirt")
        		.addProp("Dalek Five Shoulder", "Dalek Five Skirt")
        		.addProp("Dalek Six Shoulder", "Dalek Six Skirt")
        		.addProp("Dalek Seven Shoulder", "Dalek Seven Skirt")
        		.fact(db);
            dalekProps("The Daleks' Master Plan")
    			.addProp("Dalek One Shoulder", "Dalek One Skirt")
    			.addProp("Dalek Two Shoulder", "Dalek Two Skirt")
    			.addProp("Dalek Five Shoulder", "Dalek Five Skirt")
    			.addProp("Dalek Six Shoulder", "Dalek Six Skirt")
    			.fact(db);
            dalekProps("The Power of the Daleks")
				.addProp("Dalek One Shoulder", "Dalek One Skirt")
				.addProp("Dalek Two Shoulder", "Dalek Two Skirt")
				.addProp("Dalek Six Shoulder", "Dalek Five Skirt")
				.addProp("Dalek Seven Shoulder", "Dalek Seven Skirt")
				.fact(db);
            dalekProps("The Evil of the Daleks")
				.addProp("Dalek Two Shoulder", "Dalek One Skirt")
				.addProp("Dalek Five Shoulder", "Dalek Six Skirt")
				.addProp("Dalek Six Shoulder", "Dalek Five Skirt")
				.addProp("Dalek Seven Shoulder", "Dalek Seven Skirt")
				.addProp("Dalek Eight Shoulder", "Dalek Eight Skirt")
				.addProp(null, "Dalek Two Skirt")
				.fact(db);
            dalekProps("The War Games")
				.addProp("Dalek Seven Shoulder", "Dalek Eight Skirt")
				.fact(db);
            dalekProps("Day of the Daleks")
			 	.addProp("Dalek Seven Shoulder", "Dalek Two Skirt")
			 	.addProp("Dalek One Shoulder", "Dalek Five Skirt")
			 	.addProp("Dalek Six Shoulder", "Dalek Seven Skirt")
			 	.addProp(null, "Dalek One Skirt")
			 	.fact(db);
            dalekProps("Frontier in Space")
		 		.addProp("Dalek Seven Shoulder", "Dalek Two Skirt")
		 		.addProp("Dalek One Shoulder", "Dalek Five Skirt")
		 		.addProp("Dalek Six Shoulder", "Dalek Seven Skirt")
		 		.fact(db);
            dalekProps("Planet of the Daleks")
	 			.addProp("Dalek One Shoulder", "Dalek Five Skirt")
	 			.addProp("Dalek Seven Shoulder", "Dalek Two Skirt")
	 			.addProp("Dalek Six Shoulder", "Dalek Seven Skirt")
	 			.addProp("Gold Movie Shoulder", "Gold Movie Skirt")
	 			.addProp("Goon i Shoulder", "Goon i Skirt")
	 			.addProp("Goon ii Shoulder", "Goon ii Skirt")
	 			.addProp("Goon iii Shoulder", "Goon iii Skirt")
	 			.addProp("Goon iv Shoulder", "Goon iv Skirt")
	 			.addProp("Goon v Shoulder", "Goon v Skirt")
	 			.addProp("Goon vi Shoulder", "Goon vi Skirt")
	 			.addProp("Goon vii Shoulder", "Goon vii Skirt")
	 			.fact(db);
            dalekProps("Death to the Daleks")
 				.addProp("Dalek One Shoulder", "Dalek Seven Skirt")
 				.addProp("Dalek Seven Shoulder", "Dalek Two Skirt")
 				.addProp("Dalek Six Shoulder", "Dalek Five Skirt")
 				.addProp("Goon i Shoulder", "Goon i Skirt")
 				.addProp("Goon vii Shoulder", "Goon vii Skirt")
 				.addProp("Goon iii Shoulder", "Goon iii Skirt")
 				.fact(db);
            dalekProps("Genesis of the Daleks")
				.addProp("Dalek One Shoulder", "Dalek Seven Skirt")
				.addProp("Dalek Seven Shoulder", "Dalek Two Skirt")
				.addProp("Dalek Six Shoulder", "Dalek Five Skirt")
				.addProp("Goon i Shoulder", "Goon i Skirt")
				.addProp("Goon ii Shoulder", "Goon ii Skirt")
				.addProp("Goon iv Shoulder", "Goon iv Skirt")
				.addProp("Goon v Shoulder", "Goon vi Skirt")
				.fact(db);
            dalekProps("Destiny of the Daleks")
				.addProp("Dalek Six Shoulder", "Dalek Five Skirt")
				.addProp("Dalek Seven Shoulder", "Goon ii Skirt")
				.addProp("Goon iv Shoulder", "Exhibition Skirt")
				.addProp("Goon v Shoulder", "Goon vi Skirt")
				.fact(db);
            dalekProps("The Five Doctors")
				.addProp("Dalek One Shoulder", "Dalek Seven Skirt")
				.fact(db);
            dalekProps("Resurrection of the Daleks")
				.addProp("Dalek One Shoulder", "Dalek Seven Skirt")
				.addProp("Dalek Six Shoulder", "Exhibition Skirt")
				.addProp("Dalek Seven Shoulder", "Goon v Skirt")
				.addProp("Goon v Shoulder", "Dalek Five Skirt")
				.fact(db);
            dalekProps("Revelation of the Daleks")
				.addProp("Dalek One Shoulder", "Dalek Seven Skirt")
				.addProp("Dalek Six Shoulder", "Exhibition Skirt")
				.addProp("Dalek Seven Shoulder", "Goon v Skirt")
				.addProp("Goon v Shoulder", "Dalek Five Skirt")
				.addProp("Necros1 Shoulder", "Necros1 Skirt")
				.addProp("Necros2 Shoulder", "Necros2 Skirt")
				.addProp("Necros3 Shoulder", "Necros3 Skirt")			
				.fact(db);
            dalekProps("Remembrance of the Daleks")
				.addProp("Dalek One Shoulder", "Dalek Seven Skirt")
				.addProp("Dalek Seven Shoulder", "Goon v Skirt")
				.addProp("Remembrance One Shoulder", "Remembrance One Skirt")
				.addProp("Remembrance Two Shoulder", "Remembrance Two Skirt")
				.addProp("Remembrance Three Shoulder", "Remembrance Three Skirt")
				.addProp("Supreme Shoulder", "Supreme Skirt")
				.addProp("Imperial One Shoulder", "Imperial One Skirt")
				.addProp("Imperial Two Shoulder", "Imperial Two Skirt")
				.addProp("Imperial Three Shoulder", "Imperial Three Skirt")
				.addProp("Imperial Four Shoulder", "Imperial Four Skirt")
				.fact(db);
            tx.success();
        } finally {
            tx.finish();
        }
    }

}
