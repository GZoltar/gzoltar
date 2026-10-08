package org.gzoltar.java17;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
 * Exercises the Enhanced Pseudo-Random Number Generators API (JEP 356, Java 17): a common
 * RandomGenerator interface implemented by many interchangeable algorithms (not just the classic
 * java.util.Random), selectable and seedable by name through RandomGeneratorFactory.
 */
public class RandomGeneratorFeatures {

  private static final String ALGORITHM = "Xoshiro256PlusPlus";

  public boolean sameSeedProducesSameValue(long seed) {
    RandomGenerator first = RandomGeneratorFactory.of(ALGORITHM).create(seed);
    RandomGenerator second = RandomGeneratorFactory.of(ALGORITHM).create(seed);
    return first.nextInt(1000) == second.nextInt(1000);
  }

  public boolean valueWithinBound(long seed, int bound) {
    RandomGenerator generator = RandomGeneratorFactory.of(ALGORITHM).create(seed);
    int value = generator.nextInt(bound);
    return value >= 0 && value < bound;
  }

}
