#!/bin/sh

# Set this to your own project directory
PROJECT_DIR=/Users/jim/neo/development/neo4j-tutorial
LIB_DIR=$PROJECT_DIR/lib

echo $LIB_DIR/neo4j-community.jar

java -cp $LIB_DIR/neo4j-shell.jar:$LIB_DIR/neo4j-kernel.jar:$LIB_DIR/geronimo-jta_1.1_spec-1.1.1.jar org.neo4j.shell.StartClient -path $1
