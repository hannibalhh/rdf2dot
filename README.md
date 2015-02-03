RDF2Dot
======

RDF2Dot is a simplified lib to convert an RDF file to dot. It dont convert to graphic formats.
It is a fork of rdfdot, which includes graphiz directly. 

First install

	mvn clean install -DskipTests

Java 8 is because of closures required. Probably in Mac OS you need to set JAVA_HOME

	export JAVA_HOME=$(/usr/libexec/java_home)

    