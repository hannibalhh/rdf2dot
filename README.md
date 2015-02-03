RDF2Dot
======

RDF2Dot is a simplified lib to convert an RDF file to dot. It dont convert to graphic formats.
It is a fork of rdfdot, which includes graphViz directly. 

First install

	mvn clean install -DskipTests

Because of closures Java 8 is required. Probably in Mac OS you need to set JAVA_HOME

	export JAVA_HOME=$(/usr/libexec/java_home)
	
If you have a dot compatible lib you can convert to grahics such as

	dot -Tpdf units.dot > units.pdf

    