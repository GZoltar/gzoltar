/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
package com.gzoltar.core.test;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.gzoltar.core.util.ClassType;
import com.gzoltar.core.listeners.Listener;

public class TestMethod implements Comparable<TestMethod> {

  private final ClassType testType;

  private final String testClassName;

  private final String testMethodName;

  /**
   * Unit test method constructor.
   * 
   * @param testType type of test class
   * @param testFullName test full name
   */
  public TestMethod(final ClassType testType, final String testFullName) {
    this.testType = testType;
    String[] str = testFullName.split(Listener.TEST_CLASS_NAME_SEPARATOR);
    this.testClassName = str[0];
    this.testMethodName = str[1];
  }

  /**
   * Unit test method constructor.
   * 
   * @param testType type of test class
   * @param testClassName parent class name of test method
   * @param testMethodName test method name
   */
  public TestMethod(final ClassType testType, final String testClassName,
      final String testMethodName) {
    this.testType = testType;
    this.testClassName = testClassName;
    this.testMethodName = testMethodName;
  }

  /**
   * Retrieves class type (e.g., JUnit).
   */
  public ClassType getClassType() {
    return this.testType;
  }

  /**
   * Retrieves test parent class name.
   */
  public String getTestClassName() {
    return this.testClassName;
  }

  /**
   * Retrieves test method name.
   */
  public String getTestMethodName() {
    return this.testMethodName;
  }

  public String getLongName() {
    return this.testClassName + Listener.TEST_CLASS_NAME_SEPARATOR + this.testMethodName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.getLongName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(this.testType);
    builder.append(this.testClassName);
    builder.append(this.testMethodName);
    return builder.toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof TestMethod)) {
      return false;
    }

    TestMethod testMethod = (TestMethod) obj;

    EqualsBuilder builder = new EqualsBuilder();
    builder.append(this.testType, testMethod.testType);
    builder.append(this.testClassName, testMethod.testClassName);
    builder.append(this.testMethodName, testMethod.testMethodName);

    return builder.isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final TestMethod obj) {
    if (obj == null) {
      return -1;
    }
    if (!(obj instanceof TestMethod)) {
      return -1;
    }

    TestMethod testMethod = (TestMethod) obj;

    CompareToBuilder builder = new CompareToBuilder();
    builder.append(this.testType, testMethod.testType);
    builder.append(this.testClassName, testMethod.testClassName);
    builder.append(this.testMethodName, testMethod.testMethodName);

    return builder.toComparison();
  }
}
