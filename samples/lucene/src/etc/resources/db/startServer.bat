set JAVA_HOME=C:\applications\jdk1.5.0_07
set HSQLDB_LIB=..\..\..\..\lib\global\hsqldb.jar

%JAVA_HOME%\bin\java -classpath %HSQLDB_LIB% org.hsqldb.Server -database.0 test