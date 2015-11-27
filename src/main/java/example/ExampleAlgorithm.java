package example;

/* Example algorithm boilerplate, using ELKI.
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

import de.lmu.ifi.dbs.elki.algorithm.AbstractAlgorithm;
import de.lmu.ifi.dbs.elki.algorithm.AbstractDistanceBasedAlgorithm.Parameterizer;
import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.data.type.NoSupportedDataTypeException;
import de.lmu.ifi.dbs.elki.data.type.TypeInformation;
import de.lmu.ifi.dbs.elki.data.type.TypeUtil;
import de.lmu.ifi.dbs.elki.database.ids.DBIDIter;
import de.lmu.ifi.dbs.elki.database.ids.DBIDs;
import de.lmu.ifi.dbs.elki.database.relation.Relation;
import de.lmu.ifi.dbs.elki.database.relation.RelationUtil;
import de.lmu.ifi.dbs.elki.logging.Logging;
import de.lmu.ifi.dbs.elki.math.MeanVariance;
import de.lmu.ifi.dbs.elki.result.BasicResult;
import de.lmu.ifi.dbs.elki.result.Result;

/**
 * Trivial algorithm example, computing mean and variance of each attribute.
 *
 * @param <V> Vector type
 */
public class ExampleAlgorithm<V extends NumberVector> extends AbstractAlgorithm<Result> {
  /**
   * Class logger that will be used for error reporting.
   */
  private static final Logging LOG = Logging.getLogger(ExampleAlgorithm.class);

  /**
   * Constructor. This constructor MUST be public, and it MUST be parameterless,
   * OR you need to add a {@link Parameterizer}, as detailed on:
   * http://elki.dbs.ifi.lmu.de/wiki/Parameterization
   *
   * Otherwise, the MiniGUI and command line cannot create this object.
   *
   * The Parameterizer contains the command-line / GUI options, and is
   * responsible for creating the instance if all parameters are ok.
   */
  public ExampleAlgorithm(/* Read Parameterizer documentation first! */) {
    super();
  }

  /**
   * Specify the <i>input data types</i> your algorithm supports.
   */
  @Override
  public TypeInformation[] getInputTypeRestriction() {
    return TypeUtil.array(TypeUtil.NUMBER_VECTOR_FIELD);
  }

  /**
   * Run the algorithm.
   *
   * Note: the super class, {@link AbstractAlgorithm}, takes care of finding a
   * matching relation in the database. If no relation is found, a
   * {@link NoSupportedDataTypeException} will be thrown.
   */
  public Result run(Relation<V> relation) {
    final DBIDs ids = relation.getDBIDs();
    // Get the global dimensionality. By the getInputTypeRestrition()
    // requirements (vector FIELD), all vectors must have the same!
    final int gdim = RelationUtil.dimensionality(relation);
    // You will only see this, if you enable -verbose logging:
    LOG.verbose("Our vector space has " + gdim + " dimensions.");

    // ELKI provides lots of utilities, including incremental mean/variance:
    MeanVariance[] meanVariances = MeanVariance.newArray(gdim);

    // Iterate over all objects
    for(DBIDIter it = ids.iter(); it.valid(); it.advance()) {
      // Get the actual vector:
      V vector = relation.get(it);
      assert (vector.getDimensionality() == gdim) : "Vector data had varying dimensionality!";
      // Update variances:
      for(int d = 0; d < gdim; ++d) {
        meanVariances[d].put(vector.doubleValue(d));
      }
    }

    StringBuffer buf = new StringBuffer();
    buf.append("This algorithm does not do anything useful yet, except computing mean and variances:");
    for(int d = 0; d < gdim; ++d) {
      buf.append('\n').append(meanVariances[d].toString());
    }
    LOG.warning(buf);

    // Note: if you change the result, also modify the method and class
    // signatures.
    return new BasicResult("empty", "empty");
  }

  @Override
  protected Logging getLogger() {
    return LOG;
  }
}
