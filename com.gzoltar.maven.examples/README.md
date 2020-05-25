# Maven example

This document lists a few usage examples of the
[GZoltar Maven plug-in](../com.gzoltar.maven). Note that the GZoltar project
must be 'packaged' before running any of the usage examples described below,
i.e., `mvn package` must be executed on the root directory.


### Online instrumentation with custom test case executor

```
mvn clean test-compile
mvn -P custom -Dgzoltar.includes=org.gzoltar.examples.CharacterCounterTest#*:org.gzoltar.examples.StaticFieldTest#* gzoltar:list-test-methods
mvn -P custom -Dgzoltar.offline=false -Dgzoltar.collectCoverage=true gzoltar:run-test-methods
mvn gzoltar:fl-report
```


### Online instrumentation with maven-sufire test case executor

```
mvn clean test-compile
mvn -P sufire gzoltar:prepare-agent test
mvn gzoltar:fl-report
```


### Offline instrumentation with custom test case executor

```
mvn clean test-compile
mvn -P custom gzoltar:instrument
mvn -P custom -Dgzoltar.includes=org.gzoltar.examples.CharacterCounterTest#*:org.gzoltar.examples.StaticFieldTest#* gzoltar:list-test-methods
mvn -P custom -Dgzoltar.offline=true -Dgzoltar.collectCoverage=true gzoltar:run-test-methods
cp -R target/gzoltar-backup-classes/* target/classes
mvn gzoltar:fl-report
```


### Offline instrumentation with maven-sufire test case executor

```
mvn clean test-compile
mvn -P sufire gzoltar:instrument
mvn -P sufire test
cp -R target/gzoltar-backup-classes/* target/classes
mvn gzoltar:fl-report
```


After executing one of the above options the following files can be find under
the `target/` directory:

* `target/gzoltar.ser` - a serialized file with the coverage collected by
  GZoltar.
* `target/site/gzoltar/sfl/txt/tests.csv` - a list of all test cases (one per
  row) and its correspondent outcome (either `PASS` or `FAIL`), runtime in
  nanoseconds, and stacktrace (if it is a failing test case).
* `target/site/gzoltar/sfl/txt/spectra.csv` - a list of all lines of code
  identified by GZoltar (one per row) of all classes under test. Each row
  follows the following format:
  `<class name>#<method name>(<method parameters>):<line number>` e.g.,
  `org.gzoltar.examples$CharacterCounter#processString(java.lang.String):37`.
* `target/site/gzoltar/sfl/txt/matrix.txt` - a binary coverage matrix produced
  by GZoltar where each row represents the coverage of each individual test case
  and its outcome ("-" if the test case failed, "+" otherwise), and each column
  a line of code. 1 means that a test case covered a line of code, 0 otherwise.
* `target/site/gzoltar/sfl/txt/{barinel|dstar|ochiai|tarantula}.ranking.csv` -
  the fault localization report, i.e., the ranking of lines of code (one per
  row), produced by the spectrum-based fault localization formula
  `barinel|dstar|ochiai|tarantula`.
* `target/site/gzoltar/sfl/txt/statistics.csv` - some statistics information
  about the ranking produced.

