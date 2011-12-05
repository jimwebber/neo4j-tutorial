#!/bin/sh

# Script to remove any code between //SNIPPET_START and //SNIPPET_END in Java unit test files
# Must be run from the top level Koan directory

find src/koan/java -name *.java -print | xargs sed -f src/main/scripts/remove_snippets.sed -i ""