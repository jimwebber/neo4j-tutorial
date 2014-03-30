#!/bin/bash

# Naive release script that copies the Koans, without the Git repo, to the specified release directory and runs the sed scripts to strip out the Java code, thus readying the Koans for use

quitWithMessage () {
    echo >&2 "$@"
    exit 1
}

[ "$#" -eq 2 ] || quitWithMessage "This script requires an argument for the root of the koans exercises, and an argument for the output directory. $# argument(s) provided"

KOANS_ROOT_DIR=$1

pushd $KOANS_ROOT_DIR

OUTPUT_DIR=$2
mkdir -p $OUTPUT_DIR
echo using working directory [$OUTPUT_DIR]

cp -r lib $OUTPUT_DIR
cp -r build.xml	$OUTPUT_DIR	
cp -r settings $OUTPUT_DIR
cp -r src $OUTPUT_DIR
cp -r .doctorwhouniverse $OUTPUT_DIR
cp -r downloads $OUTPUT_DIR

popd

pushd $OUTPUT_DIR

bash src/main/scripts/remove_snippets.sh

popd    