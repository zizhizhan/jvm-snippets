### What is Jasmin

Jasmin is an assembler for the Java Virtual Machine. It takes ASCII descriptions of Java classes, written in a simple assembler-like syntax using the Java Virtual Machine instruction set. It converts them into binary Java class files, suitable for loading by a Java runtime system.

Jasmin was originally created as a companion to the book "Java Virtual Machine", written by Jon Meyer and Troy Downing and published by O'Reilly Associates. Since then, it has become the de-facto standard assembly format for Java. It is used in dozens of compiler classes throughout the world, and has been ported and cloned multiple times. For better or worse, Jasmin remains the oldest and the original Java assembler.

The O'Reilly JVM book is now out of print. Jasmin continues to survive as a SourceForge Open Source project.

### How to use Jasmin

#### Usage

```java
.class public examples/HelloWorld
.super java/lang/Object
;
; standard initializer
.method public <init>()V
   aload_0
   invokenonvirtual java/lang/Object/<init>()V
   return
.end method
.method public static main([Ljava/lang/String;)V
   .limit stack 2
   ; push System.out onto the stack
   getstatic java/lang/System/out Ljava/io/PrintStream;
   ; push a string onto the stack
   ldc "Hello World!"
   ; call the PrintStream.println() method.
   invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
   ; done
   return
.end method
```

Save this content to HelloWorld.j

```bash
wget http://pilotfiber.dl.sourceforge.net/project/jasmin/jasmin/jasmin-2.4/jasmin-2.4.zip

// unzip the file and put jasmin.jar to local lib

java -jar lib/jasmin.jar -d /tmp HelloWorld.j

java -cp /tmp examples.HelloWorld   
```
