# Command line example

The script `run.sh` performs fault-localization on a Java toy project using the
[GZoltar command line interface](../com.gzoltar.cli). Fault-localization can
either be performed with offline or online (i.e., at runtime) instrumentation.
Note that the GZoltar project must be 'packaged' before running the following
command, i.e., `mvn package` must be executed on the root directory.

Usage:

```
  ./run.sh
    --instrumentation <online|offline>
    [--help]
```

After executing the script, the following files can be find under the `build/`
directory:

* `build/gzoltar.ser` - a serialized file with the coverage collected by GZoltar.
* `build/sfl/txt/tests.csv` - a list of all test cases (one per row) and its
  correspondent outcome (either `PASS` or `FAIL`), runtime in nanoseconds, and
  stacktrace (if it is a failing test case).
* `build/sfl/txt/spectra.csv` - a list of all lines of code identified by
  GZoltar (one per row) of all classes under test. Each row follows the
  following format:
  `<class name>#<method name>(<method parameters>):<line number>` e.g.,
  `org.gzoltar.examples$CharacterCounter#processString(java.lang.String):37`.
* `build/sfl/txt/matrix.txt` - a binary coverage matrix produced by GZoltar
  where each row represents the coverage of each individual test case and its
  outcome ("-" if the test case failed, "+" otherwise), and each column a line
  of code. 1 means that a test case covered a line of code, 0 otherwise.
* `build/sfl/txt/ochiai.ranking.csv` - the fault localization report, i.e.,
  the ranking of lines of code (one per row), produced by the spectrum-based
  fault localization formula `ochiai`.
* `build/sfl/txt/statistics.csv` - some statistics information about the ranking
  produced.

