REM Set this to your own project directory

set PROJECT_DIR=Z:\MYDO~0F4\Neo\Development\neo4j-tutorial
set LIB_DIR=%PROJECT_DIR%\lib


java -cp %LIB_DIR%\neo4j-shell-1.3-SNAPSHOT.jar;%LIB_DIR%\neo4j-kernel-1.3-SNAPSHOT.jar;%LIB_DIR%\geronimo-jta_1.1_spec-1.1.1.jar org.neo4j.shell.StartClient -path %1
 
