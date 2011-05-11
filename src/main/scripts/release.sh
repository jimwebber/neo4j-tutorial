#!/bin/sh

# Naive release script that copies the Koans, without the Git repo, to the specified release direction and runs the sed scripts to strip out the Java code, thus readying the Koans for use

CURRENT_DIR=`pwd`

TEMP_DIR=/tmp/$RANDOM
mkdir -p $TEMP_DIR
echo using temp directory [$TEMP_DIR]
echo delivering gzipped tarball to [$1]

cp -r presentation $TEMP_DIR
cp -r lib $TEMP_DIR
cp -r build.xml	$TEMP_DIR	
cp -r settings $TEMP_DIR
cp -r src $TEMP_DIR
cp -r tools $TEMP_DIR

cd $TEMP_DIR

bash src/main/scripts/remove_snippets.sh

TAR_DIR=$1
mkdir -p $TAR_DIR

tar -cf /$TAR_DIR/koan.tar .
gzip /$TAR_DIR/koan.tar

cd $CURRENT_DIR