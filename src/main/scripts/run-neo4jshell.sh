#!/bin/sh

# Set this to your own project directory
PROJECT_DIR=/Users/jim/Neo/Development/neo4j-tutorial
LIB_DIR=$PROJECT_DIR/lib

java -cp $LIB_DIR/neo4j-shell-1.3-SNAPSHOT.jar:$LIB_DIR/neo4j-kernel-1.3-SNAPSHOT.jar:$LIB_DIR/geronimo-jta_1.1_spec-1.1.1.jar org.neo4j.shell.StartClient -path $1
