Neo4j Koans
===========

!["New Daleks""](http://static.bbc.co.uk/images/ic/qe/crop/946x532/doctorwho/monsters/daleks/d11/s01/galleries/new_dalek_gallery/37.jpg)

This set of Koans will provide practical guidance for getting to grips with graph data structures and operations using the Neo4j open source graph database. It's part of a more comprehensive tutorial presented by the [authors](#authors) and others at conferences and tutorials. In fact anyone can take these materials freely and run their own tutorials.

What are Koans?
---------------

The Koan idea was borrowed from the [Ruby Koans](http://rubykoans.com/) which provide a number of broken unit tests, and in fixing those tests increasingly advanced facets of Ruby are explored. The Koan model provides very rapid feedback and a structured learning path wrapped in a pre-configured environment that gets us up and running very quickly. These are very desirable characteristics when it comes to learning Neo4j, and so these Koans have adopted the same model - there are a set of (broken) unit tests, and in fixing each of them we learn some aspect of using Neo4j. As we work forwards through the Koans we'll learn more sophisticated APIs, query languages and techniques and by the end of the Koans we'll feel supremely confident about using Neo4j in production.

Prerequisites
-------------

You'll need to be familiar with the Java programming language, and it'd be helpful if you understood unit testing too. If you like a particular IDE like Eclipse or IntelliJ, that's fine but you can run these Koans from the command line if that's the way you're wired. All the graph database knowledge you'll needed will be developed as part of completing the Koans. 

Setting up Koans
----------------

The first step in setting up the koans is to clone this repository from github, or to download the latest tarball. To clone from github:

    git clone git@github.com:jimwebber/neo4j-tutorial.git

To download the latest version of the koans as a zipfile, click on the following URI:

[https://github.com/jimwebber/neo4j-tutorial/zipball/master](https://github.com/jimwebber/neo4j-tutorial/zipball/master)

Once you have cloned or downloaded and unzipped the koans, you're almost ready. Since the Koans come complete with the answers, if you're going to benefit from following them, you'll need to delete those answers. Fortunately we've provided a sed script that does this for you. From the root directory of your Koan download, run: 

    src/main/scripts/remove_snippets.sh

Sorry to Windows users, you'll have to run this with [http://www.cygwin.com/](Cygwin) for now.

The sed script removes all lines between

    //SNIPPET_START
    //SNIPPET_END

in the Koans, leaving them compiling but failing. 

Live Sessions
-------------

This tutorial is presented around the world. If you're interested in participating in a class, then it'll be taught at:

* Quarterly at [Skillsmatter](http://skillsmatter.com/course/nosql/neo4j-tutorial) London

* Once each in Melbourne, Brisbane, and Sydney at the [Yow! Australia 2012 conference](http://www.yowconference.com.au/YOW2011/general/workshopDetails.html?eventId=3488)

Authors <a name="authors">
-------

[Glen Ford](http://usersource.net/), [@glen_ford](http://twitter.com/glen_ford)

[Tobias Ivarsson](http://www.thobe.org/), [@thobe](http://twitter.com/thobe)

Peter Neubauer, [@peterneubauer](http://twitter.com/peterneubauer)

[Ian Robinson](http://iansrobinson.com), [@iansrobinson](http://twitter.com/iansrobinson)

[Aleksa Vukotic](http://aleksavukotic.com), [@aleksavukotic](http://twitter.com/aleksavukotic)

[Jim Webber](http://jimwebber.org/), [@jimwebber](http://twitter.com/jimwebber)

Thanks to
---------



