#!/bin/sh

# Script to remove any code between //SNIPPET_START and //SNIPPET_END in Java unit test files
# Must be run from the top level Koan directory

dashi='-i .deleteme'  # default is version of sed that works on Mac OS X

# sed on Linux/Cygwin is different and wants no space between -i and the arg
# or can leave the arg off entirely if empty
os=`uname`
os_lwr=$(echo "$os" | sed 's/^\(\w\{5\}\).*/\1/' | tr '[A-Z]' '[a-z]')

case "$os_lwr" in
    "linux" )  dashi='-i' ;;
    "cygwi" )  dashi='-i' ;;
    "mingw" )  dashi='-i' ;;
esac

find src/koan/java -name *.java -print | xargs sed -f src/main/scripts/remove_snippets.sed $dashi
find src/koan/java -name *.deleteme -print | xargs rm
