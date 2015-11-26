Example Project for using ELKI
==============================

This project uses Maven to download ELKI.

The Eclipse m2e extensions should work fine with this project.
If necessary, convert to a Maven project, and run Maven Update.


Launching using Maven:
---------------------------------

To launch use the included Eclipse launch configuration "ELKI Example".

From command line, you can launch using `mvn -P launch test`.


Building a package:
-------------------

The `package` goal will produce a package in the output folder `target`.

All dependencies will be copied into the subdirectory `dependency`.


Known limitations:
------------------

As of ELKI 0.7.0, classes in the default package cause problems in the MiniGUI.
Therefore, *always use a package* for your classes. This will be fixed in the
next ELKI version.

To build and distribute an add-on for ELKI, set the `addClasspath` function
of the `maven-jar-plugin` to `false` to disable adding dependencies to the jar manifest.
