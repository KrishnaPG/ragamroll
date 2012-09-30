Install groovy (from http://groovy.codehaus.org/Download) and ensure that it works
Download jfugue 4.0.3 ( from http://www.jfugue.org/jfugue-4.0.3.jar ) or later version.

Files:
ragamroll.groovy - UI code
NotationParser.groovy - compiles srgm notation to JFugue.
raga_base.txt - contains the raga definitons

From the directory where you have downloaded the above files, run

groovy -cp <path to jfugue jar> ragamroll.groovy

The application jar file was built using GroovyWrapper script available in 
http://groovy.codehaus.org/WrappingGroovyScript
