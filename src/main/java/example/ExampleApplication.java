package example;

/* Example application boilerplate, using ELKI.
 *
 * Written in 2015 by Erich Schubert <schube@dbs.ifi.lmu.de>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright
 * and related and neighboring rights to this file to the public domain worldwide.
 *
 * This software is distributed without any warranty.
 *
 * For the CC0 Public Domain Dedication see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

import de.lmu.ifi.dbs.elki.application.AbstractApplication;
import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.logging.Logging;
import de.lmu.ifi.dbs.elki.logging.LoggingConfiguration;
import de.lmu.ifi.dbs.elki.logging.progress.FiniteProgress;
import de.lmu.ifi.dbs.elki.logging.statistics.DoubleStatistic;
import de.lmu.ifi.dbs.elki.logging.statistics.LongStatistic;
import de.lmu.ifi.dbs.elki.logging.statistics.MillisTimeDuration;
import de.lmu.ifi.dbs.elki.result.Result;
import de.lmu.ifi.dbs.elki.result.ResultUtil;
import de.lmu.ifi.dbs.elki.utilities.exceptions.UnableToComplyException;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.OptionID;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.constraints.CommonConstraints;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameterization.Parameterization;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameters.IntParameter;
import de.lmu.ifi.dbs.elki.workflow.InputStep;

/**
 * Trivial "main" application to run our example algorithm multiple times.
 *
 * To run this, you can do the following:
 *
 * <ul>
 * <li>in Eclipse, choose "Run as, Java Application" to create a launch
 * configuration. Then edit the configuration to add the missing parameters:
 * <tt>-dbc.in YourInputFile.csv</tt></li>
 * <li>in Eclipse, choose "Run as, Java Application" to create a launch
 * configuration. Then edit the configration, and use the main class
 * <tt>de.lmu.ifi.dbs.elki.gui.minigui.MiniGUI</tt> to start the MiniGUI, and as
 * first argument use <tt>example.ExampleApplication</tt>. The MiniGUI will
 * create a user interface for your application (notice the -repeat parameter).
 * </li>
 * <li>Run <tt>mvn package</tt> (in Eclipse: Run As, Maven Build) to build a
 * jar. Then invoke this jar on the command line as:
 * <tt>java -jar yourjar.jar example.ExampleApplication</tt> or in the MiniGUI
 * mode as: <tt>java -jar yourjar.jar minigui example.ExampleApplication</tt>
 * </li>
 * </ul>
 */
public class ExampleApplication extends AbstractApplication {
  /**
   * Class logger that will be used for error reporting.
   */
  private static final Logging LOG = Logging.getLogger(ExampleApplication.class);

  /**
   * Where we get our data from.
   */
  protected InputStep input;

  /**
   * Number of repetitions to do.
   */
  protected int repetitions = 1;

  /**
   * Constructor.
   *
   * The inner class {@link Parameterizer} knows how to set these parameters.
   *
   * @param input ELKI data loader
   * @param repetitions Number of repetitions
   */
  public ExampleApplication(InputStep input, int repetitions) {
    super();
    this.input = input;
    this.repetitions = repetitions;
  }

  @Override
  public void run() throws UnableToComplyException {
    // Force the logging level to include statistics, so we always see the
    // runtimes measured below.
    LoggingConfiguration.setStatistics();

    // Start a timer to measure overall runtime:
    MillisTimeDuration timerAll = new MillisTimeDuration("algorithm.runtime.all").begin();

    // We let the ELKI input step take care of loading the data
    // This way, the user can choose the file type, parser, add filters, and use
    // compressed files.
    Database database = input.getDatabase();

    // Create a progress bar (if -verbose is set).
    FiniteProgress progress = LOG.isVerbose() ? new FiniteProgress("Repetitions", repetitions, LOG) : null;
    MillisTimeDuration timer = new MillisTimeDuration("algorithm.runtime");
    for(int i = 0; i < repetitions; i++) {
      // Create our algorithm.
      // We want some extra control here, so we do this in plain Java:
      ExampleAlgorithm<NumberVector> alg = new ExampleAlgorithm<NumberVector>();

      // AbstractAlgorithm will choose the appropriate data relation
      // automatically, based on the data type information:
      timer.begin();
      Result result = alg.run(database);
      LOG.statistics(timer.end()); // Log the time taken.
      // Drop the result, to not leak memory across repetitions:
      ResultUtil.removeRecursive(database.getHierarchy(), result);

      LOG.incrementProcessed(progress);
    }
    // Close the progress bar
    LOG.ensureCompleted(progress);
    // Log the total runtime (including overhead!)
    LOG.statistics(timerAll.end());
    // Log the average runtime (including overhead!)
    LOG.statistics(new DoubleStatistic("algorithm.runtime.average", timerAll.getDuration() / (double) repetitions));
    // Log how many repetitions we did, for later analysis
    LOG.statistics(new LongStatistic("algorithm.repetitions", repetitions));
  }

  /**
   * This class is responsible for GUI and command line parameters.
   */
  public static class Parameterizer extends AbstractApplication.Parameterizer {
    /**
     * We add an additional parameter, -repeat.
     */
    public static final OptionID REPETITIONS_ID = new OptionID("repeat", "Number of repetitions to do.");

    /**
     * ELKI data input step.
     */
    protected InputStep input;

    /**
     * Number of repetitions to do.
     */
    protected int repetitions = 1;

    @Override
    protected void makeOptions(Parameterization config) {
      super.makeOptions(config);

      // This adds all parameters for data input:
      input = config.tryInstantiate(InputStep.class);

      // Create the parameter object, of type integer
      IntParameter repeatP = new IntParameter(REPETITIONS_ID) //
      // Add a constraint >= 1
      .addConstraint(CommonConstraints.GREATER_EQUAL_ONE_INT) //
      // By default, only 1 repetition:
      .setDefaultValue(1);

      // Add the parameter, and get the value (maybe the default value, but can
      // fail if e.g. a negative number was given incorrectly)
      if(config.grab(repeatP)) {
        repetitions = repeatP.intValue();
      }
    }

    @Override
    public ExampleApplication makeInstance() {
      return new ExampleApplication(input, repetitions);
    }
  }

  /**
   * Runs the application using command line parameters.
   *
   * @param args parameter list according to description
   */
  public static void main(String[] args) {
    runCLIApplication(ExampleApplication.class, args);
  }
}
