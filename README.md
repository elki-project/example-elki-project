Example Project for using ELKI
==============================

This project uses Maven to download ELKI.

The Eclipse m2e extensions should work fine with this project.
If necessary, convert to a Maven project, and run Maven Update.


Building a package:
-------------------

The Maven `package` goal will produce a package in the output folder `target`.

All dependencies will be copied into the subdirectory `dependency`.

To launch this, use `mvn package` on the command line, or in Eclipse
Run As, Maven Build and specify the goal `package`.


Custom Algorithm Example:
=========================

This example algorithm computes the mean and variance of each attribute.


Launching using Maven:
----------------------

From command line, you can launch using `mvn -P launch test`.


Launching from the jar package:
-------------------------------

First, build a package as explained above via `mvn package`.

`java -jar yourpackage.jar` will spawn the MiniGUI, where you can choose
your algorithm in the `-algorithm` parameter dropdown.


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


Known limitations:
==================

As of ELKI 0.7.0, classes in the default package cause problems in the MiniGUI.
Therefore, *always use a package* for your classes. This will be fixed in the
next ELKI version.

To build and distribute an add-on for ELKI, set the `addClasspath` function
of the `maven-jar-plugin` to `false` to disable adding dependencies to the jar manifest.
