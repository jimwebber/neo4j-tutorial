#!/bin/sh

# Naive release script that copies the Koans, without the Git repo, to the specified release directory and runs the sed scripts to strip out the Java code, thus readying the Koans for use

CURRENT_DIR=`pwd`

TEMP_DIR=/tmp/$RANDOM/koan
mkdir -p $TEMP_DIR
echo using temp directory [$TEMP_DIR]
echo delivering zipped tutorial to [$1]

cp -r presentation $TEMP_DIR
cp -r lib $TEMP_DIR
cp -r build.xml	$TEMP_DIR	
cp -r settings $TEMP_DIR
cp -r src $TEMP_DIR
cp -r tools $TEMP_DIR

cd $TEMP_DIR

bash src/main/scripts/remove_snippets.sh

ZIP_DIR=$1
mkdir -p $ZIP_DIR

zip -r $ZIP_DIR/koan.zip .

cd $CURRENT_DIR