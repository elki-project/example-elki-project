package example;

import de.lmu.ifi.dbs.elki.algorithm.Algorithm;
import de.lmu.ifi.dbs.elki.data.type.TypeInformation;
import de.lmu.ifi.dbs.elki.data.type.TypeUtil;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.result.Result;
import de.lmu.ifi.dbs.elki.utilities.exceptions.AbortException;

/**
 * Algorithm that always causes an AbortException.
 */
public class AbortAlgorithm implements Algorithm {
  /**
   * Run the algorithm.
   *
   * Note: if you inherit from AbstractAlgorithm, you can also use signatures
   * such as {@code run(Relation<NumberVector> relation)}, which is easier to
   * use.
   */
  public Result run(Database database) {
    throw new AbortException("This 'algorithm' is always supposed to throw an exception.");
  }

  /**
   * Specify the <i>input data types</i>!
   */
  public TypeInformation[] getInputTypeRestriction() {
    return TypeUtil.array(/* e.g. TypeUtil.NUMBER_VECTOR_FIELD */);
  }
}
