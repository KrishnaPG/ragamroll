Exported from original: https://code.google.com/p/ragamroll/ since google code is going to be shutdown (or whatever).

Install groovy (from http://groovy.codehaus.org/Download) and ensure that it works
Download jfugue 4.0.3 ( from http://www.jfugue.org/jfugue-4.0.3.jar ) or later version.

Files:
ragamroll.groovy - UI code
NotationParser.groovy - compiles srgm notation to JFugue.
RagaAnalyer.groovy - analyzes a composition and builds a note transition graph
RagaImitator.groovy - generates new composition based on a given composition
raga_base.txt - contains the raga definitons
GroovyWrapper.groovy - compiles groovy scripts into executable jars

grun.sh - script to invoke groovy with jfugue in classpath

To run, from the directory where you have downloaded the above files, run

groovy -cp <path to jfugue jar> ragamroll.groovy

or

grun.sh ragamroll.groovy

To build an executable jar file

grun.sh GroovyWrapper.groovy -c -m ragamroll
