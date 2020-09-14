
### What is JOL

JOL (Java Object Layout) is the tiny toolbox to analyze object layout schemes in JVMs. These tools are using Unsafe, 
JVMTI, and Serviceability Agent (SA) heavily to decoder the actual object layout, footprint, and references. This makes 
JOL much more accurate than other tools relying on heap dumps, specification assumptions, etc.


### How to use JOL

```bash
MAVEN_HOME=`mvn help:evaluate -Dexpression=settings.localRepository | grep -v '[INFO]'`
CLASSPATH=$MAVEN_HOME/org/openjdk/jol/jol-cli/0.5/jol-cli-0.5.jar:$MAVEN_HOME/org/openjdk/jol/jol-core/0.5/jol-core-0.5.jar:$MAVEN_HOME/net/sf/jopt-simple/jopt-simple/4.6/jopt-simple-4.6.jar

java -classpath $CLASSPATH org.openjdk.jol.Main footprint java.util.Hashtable

java -classpath $CLASSPATH org.openjdk.jol.Main estimates java.math.BigInteger
```