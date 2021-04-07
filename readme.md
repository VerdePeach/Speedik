Speedik
------------------------------------------
Java-based application for course Techical Solution Support / Software Quality Assurance, Sumy Stete University, 2020.
It receives, collects, handles, saves, and presents data from the Linux monitoring agent.

Getting Started 
------------------------------------------
To install the project clone it firstly on your computer with the next command
or download it like an archive and unpack it.

`git clone https://tss2020.repositoryhosting.com/git/tss2020/t6.git`

Then execute the command:

`mvn package`

Finally, run command:

`java -classpath target/speedik-1.1-jar-with-dependencies.jar --enable-preview -javaagent:/home/test/t6/newrelic/newrelic.jar com.in726.app.Speedik`

The application should be started;

Full guide to start
------------------------------------------
1. We are using MySQL database, so you need to install it on your machine.
    1.1 After successful installation create user with the name vladbright and password vladbright
    1.2 Create the database speedik for app and speediktest for test.
2. Install maven and add MAVEN_HOME path variable.
3. Install java 14 and create JAVA_HOME path variable. 
4. For comfortable work install git.
5. Install lombok plugin.
6. Import all dependencies in pom.xml file.
7. Build app with command: `mvn package`
8. Run speedik application: `java -classpath target/speedik-1.1-jar-with-dependencies.jar --enable-preview -javaagent:{full path to jar}/newrelic/newrelic.jar com.in726.app.Speedik`
    8.1 Do not forget to specify the ful path to jar.
9. Enjoy!

Credits
------------------------------------------
Created by Speedik team:
 1. Vladyslav Derevianchuk
 2. Vyacheslav Fomenko
 3. Yevheniia Rudenko
 4. Viktor Malezhyk
 5. Zadesenec Denis
 
License
------------------------------------------
You can find information about licence in the file LICENSE.md

Install
------------------------------------------
To install the project execute the next command:

`mvn install`

Then to run it, execute the next command:

`mvn exec:java -Dexec.mainClass=com.in726.app.Speedik`

The application should be started;

Created with
------------------------------------------
Items used in the project and license of them in parentheses.

JDK 14.0.2 (GNU GPL 2)

maven (Apache 2.0)

javalin (Apache 2)

lombok (MIT)

hibernate (GNU LGPL)

slf4j (MIT)

jackson-core (Apache 2.0)

junit (Common Public License)

mail (GNU GPL 2 and Common Development and Distribution License version 1.1)

commons-validator (Apache License 2.0)

oro ( Apache License Version 2.0, January 2004)

mockito (The MIT License)

selenium (Apache License Version 2.0, January 2004)

