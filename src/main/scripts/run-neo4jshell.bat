REM Set this to your own project directory

set PROJECT_DIR=Z:\MYDO~0F4\neo\development\neo4j-tutorial
set LIB_DIR=%PROJECT_DIR%\lib


java -cp %LIB_DIR%/neo4j-jmx.jar;%LIB_DIR%/neo4j-community.jar;%LIB_DIR%/neo4j-server.jar;%LIB_DIR%/neo4j-cypher.jar;%LIB_DIR%/neo4j-kernel.jar;%LIB_DIR%/neo4j-shell.jar;%LIB_DIR%/neo4j-graph-algo.jar;%LIB_DIR%/neo4j-lucene-index.jar;%LIB_DIR%/lucene-core.jar;%LIB_DIR%/neo4j-graph-matching.jar;%LIB_DIR%/neo4j-server-static-web.jar;%LIB_DIR%/scala-library.jar;%LIB_DIR%/geronimo-jta_1.1_spec.jar org.neo4j.shell.StartClient -path %1