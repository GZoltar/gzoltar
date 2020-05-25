# Ant example

The following command performs fault-localization on a Java toy project using
the [GZoltar Ant tasks](../com.gzoltar.ant). Note that the GZoltar project must
be 'packaged' before running the following command, i.e., `mvn package` must be
executed on the root directory.

```
ant -f build.xml clean report
```

After executing the `ant` command in one of the subdirectories (i.e., `simple`
or `offline-instrumentation`) the following files can be find under the `build/`
directory:

* `build/gzoltar.exec` - a serialized file with the coverage collected by
  GZoltar.
* `build/gzoltar/sfl/txt/tests.csv` - a list of all test cases (one per row) and
  its correspondent outcome (either `PASS` or `FAIL`), runtime in nanoseconds,
  and stacktrace (if it is a failing test case).
* `build/gzoltar/sfl/txt/spectra.csv` - a list of all lines of code identified
  by GZoltar (one per row) of all classes under test. Each row follows the
  following format:
  `<class name>#<method name>(<method parameters>):<line number>` e.g.,
  `org.gzoltar.examples$CharacterCounter#processString(java.lang.String):37`.
* `build/gzoltar/sfl/txt/matrix.txt` - a binary coverage matrix produced by
  GZoltar where each row represents the coverage of each individual test case
  and its outcome ("-" if the test case failed, "+" otherwise), and each column
  a line of code. 1 means that a test case covered a line of code, 0 otherwise.
* `build/gzoltar/sfl/txt/{barinel|dstar|ochiai|tarantula}.ranking.csv` - the
  fault localization report, i.e., the ranking of lines of code (one per row),
  produced by the spectrum-based fault localization formula
  `barinel|dstar|ochiai|tarantula`.
* `build/gzoltar/sfl/txt/statistics.csv` - some statistics information about the
  ranking produced.
