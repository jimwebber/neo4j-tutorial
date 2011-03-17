#!/bin/sh

# Script to remove any code between //SNIPPET_START and //SNIPPET_END in Java unit test files

find src/koan/java -name *.java -print | xargs sed -f src/main/scripts/remove_snippets.sed -i ""