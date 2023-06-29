# GZoltar System Test for java 9

This document describes some of the new features introduced in Java 9 in order to be tested by GZoltar.

To run the system test, execute the following command:

```
mvn clean test -Dtest=org.gzoltar.systemtest.Java9SystemTest
```

## Interface Private Methods

Private methods inside an interface are introduced in Java 9.

## Stream API

In Java 9, the Stream API has included some new methods: takeWhile, dropWhile, iterate, ofNullable.