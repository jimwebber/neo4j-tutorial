Neo4j Koans
===========

!["New Daleks""](http://static.bbc.co.uk/images/ic/qe/crop/946x532/doctorwho/monsters/daleks/d11/s01/galleries/new_dalek_gallery/37.jpg)

This set of Koans provides a hands-on tutorial for learning the Neo4j open source graph database. It's part of a more comprehensive tutorial often presented by the [authors](#authors) and other folks.

What are Koans?
---------------

The Koan idea was borrowed from the [Ruby Koans](http://rubykoans.com/) which provide a number of broken unit tests, and in fixing those tests increasingly advanced Ruby features are learned. The Koan model provides very rapid feedback and a structured learning path wrapped in a pre-configured environment for effective learning. 

These are very desirable characteristics when it comes to learning Neo4j too, and so the Neo4j Koans have adopted the same model: there are a set of (broken) unit tests, and in fixing each of them we learn some aspect of using Neo4j. As we work through the Koans we'll learn increasingly sophisticated APIs, modeling and querying techniques, and become fluent in the Cypher query language. At the Koans we'll feel supremely confident about using Neo4j in production.

Prerequisites
-------------

You'll need to be familiar with the Java programming language for some of the early Koans and confident enough in Java to be able to run JUnit tests. If you like a particular IDE like Eclipse or IntelliJ, that's helpful but you can run these Koans from the command line too using an [Ant](http://ant.apache.org) script. All the Neo4j skills you'll need will be developed as part of completing the Koans.

Setting up Koans
----------------

The first step in setting up the koans is to clone this repository from github, or to download the latest tarball. To clone from github:

    git clone git://github.com/jimwebber/neo4j-tutorial.git

To download the latest version of the koans as a zipfile, click on the following URI:

[https://github.com/jimwebber/neo4j-tutorial/zipball/master](https://github.com/jimwebber/neo4j-tutorial/zipball/master)

Once you have cloned or downloaded and unzipped the koans, you're almost ready. You just need the Neo4j binary dependencies, which you can get by running the default Apache Ant target from the root directory:

    ant 

We use Apache Ivy for dependency resolution and for all the many wonderful benefits Ant and Ivy provide, speed isn't one of them so you might want to go fetch a cup of tea while you're waiting for (seemingly) most of the Internet to download. 

Once the default Ant target has completed, you'll find it has dropped a set of libraries in the lib directory immediately under your Koan directory. It has also run the Koans and the unit tests that validate that everything's ready and will have left a report under:

    target/koan/reports/output/index.html

Since the Koans come complete with the answers, if you're going to benefit from following them, you'll need to delete those answers. Fortunately we've provided a sed script that does this for you. From the root directory of your Koan download, run: 

    src/main/scripts/remove_snippets.sh

Sorry to Windows users, you'll have to run this with [Cygwin](http://www.cygwin.com/) for now.

The sed script removes all lines between

    //SNIPPET_START
    //SNIPPET_END

in the Koans, leaving them compiling but in a failing state. 

If you want to run individual Koans (rather than all of them) then just type the specific Koan's target on the command line. For example to run Koan 5:

   ant run.koan05

and to run all of Koan 8 (which has many parts):

   ant run.koan08

If you'd prefer to use an IDE, you can of course just use your favourite development environment to run the Koans too.

I want my IDE
-------------

If you'd like some help in setting up the Koans in your IDE, the Ant script contains a target called generate.eclipse.project. By issuing the command:

    ant generate.eclipse.project

an Eclipse project will be created in the Koan directory. This can be opened easily with Eclipse or Intellij where you can run individual koans as JUnit tests as normal.


Where are my Koans?
-------------------

The Koans themselves are in the root directory under:

    src/koan/java

and you'll see that they're numerically increasing in number. Start with *Koan01.java* and run it. If you're running the Koans in your IDE, then you can run Koan01.java as a unit test and verify that a Neo4j database can be created and populated with Doctor Who data, and that you have your IDE project set up properly.

Now that you're at this point, you're ready to move to *Koan02.java* and really start hacking on Neo4j.

Live Sessions
-------------

This tutorial is presented around the world. If you're interested in participating in a class, then it'll be taught at:

* Quarterly at [Skillsmatter](http://skillsmatter.com/course/nosql/neo4j-tutorial) London, UK
* Regularly at conferences around the world. Check the [Neo Technology calendar](http://www.neotechnology.com/calendar/) for more information

Authors <a name="authors">
-------

[Ian Robinson](http://iansrobinson.com), [@iansrobinson](http://twitter.com/iansrobinson)

[Jim Webber](http://jimwebber.org/), [@jimwebber](http://twitter.com/jimwebber)
   
Contributors <a name="contributors">
-------
 
[Glen Ford](http://usersource.net/), [@glen_ford](http://twitter.com/glen_ford)

Alistair Jones, [@apcj](http://twitter.com/apcj)

[Tobias Lindaaker](http://www.thobe.org/), [@thobe](http://twitter.com/thobe)

Peter Neubauer, [@peterneubauer](http://twitter.com/peterneubauer)

[Michael Peterson](http://thornydev.blogspot.com/), [@midpeter444](http://twitter.com/midpeter444)

Andres Taylor, [@andres_taylor](http://twitter.com/andres_taylor)

Niklas Uhrberg

[Aleksa Vukotic](http://aleksavukotic.com), [@aleksavukotic](http://twitter.com/aleksavukotic)

Links
-----
As well as being a contributor, [Michael Peterson](http://thornydev.blogspot.com/) has written a blog post on setting up the Koans from the point of view of a first-time user. You can find Michael's posting [here](http://thornydev.blogspot.com/2011/11/neo4j-koans-how-do-i-begin.html).


