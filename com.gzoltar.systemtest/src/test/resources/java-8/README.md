# GZoltar System Test for java 8

This document describes some of the new features introduced in Java 8 in order to be tested by GZoltar.

To run the system test, execute the following command:

```
mvn clean test -Dtest=org.gzoltar.systemtest.Java8SystemTest
```

## Interfaces

A functional interface is an interface that contains only one abstract method.

In Java 8, many functional interfaces have been included in java.util.function package.

In [ExampleInterface.java](com.gzoltar.systemtest/src/test/resources/java-8/src/main/java/org/gzoltar/examples/ExampleInterface.java) can be found a few usage of functional interfaces.

## Lambda Expressions

Lambda expressions provide a way to write cleaner and more flexible code.

An example of a program that tests a few usage of Lambda expressions can be found at [ExampleLambda.java](com.gzoltar.systemtest/src/test/resources/java-8/src/main/java/org/gzoltar/examples/ExampleLambda.java).

## Stream API

The Stream API was also introduced in Java 8. It is used to process collections of objects.

The project example to test the usage of this API can be found at [ExampleStream.java](com.gzoltar.systemtest/src/test/resources/java-8/src/main/java/org/gzoltar/examples/ExampleStream.java).

