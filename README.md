Example Project for using ELKI
==============================

This project uses Gradle for building and dependency management.

You need at least Java 1.8.

When using Eclipse, it is recommended to use at minimum Eclipse Neon,
although it is generally a good idea to install the current version.


Compilation
-----------

Use `./gradlew assemble` to build the project. This will get the Gradle version,
download the ELKI modules, and the necessary dependencies.


Custom Algorithm Example:
=========================

This example algorithm computes the mean and variance of each attribute.


Launching from the jar package:
-------------------------------

First, build a package as explained above via `./gradlew assemble`.

`java -jar example-elki-project-*.jar` will spawn the MiniGUI,
where you can choose your algorithm in the `-algorithm` parameter dropdown.


Launching in Eclipse:
---------------------

To launch use the included Eclipse launch configuration "ELKI Example".


Custom Application Example:
===========================

This application runs above algorithm multiple times.

Launching from the jar package:
-------------------------------

First, build a package as explained above via `mvn package`.

To launch the command line version (*great* for automated experiments):

   java -jar yourjar.jar example.ExampleApplication

To launch the MiniGUI version, use:

    java -jar yourjar.jar minigui example.ExampleApplication

As you can see, ELKI added an option to choose the number of repetitions,
the new parameter that we have added in our application.
