# Command line interface

GZoltar comes with a very basic command line interface to perform basic
operations from the command line. The command line tools with all dependencies
are packaged in `gzoltarcli.jar` and are available with the GZoltar's
distribution package.

GZoltar's command line interface can be called as follows:

```
java -jar gzoltarcli.jar <command> [parameters]
```

To see the available parameters of each command, use:

```
java -jar gzoltarcli.jar <command> --help
```


## listTestMethods

Writes to a file named "tests.txt" all JUnit / TestNG unit test cases in a
provided set of directories and/or .jar files. Usage:

```
java -cp <project_classpath:gzoltarcli.jar> com.gzoltar.cli.Main listTestMethods \
  <list of folders that contain test classes> \
  --outputFile <path> \
  --includes <test classes/cases to consider, e.g., org.TestFoo#* to include all test cases of test class TestFoo>
```


## version

Prints out GZoltar version information. Usage:

```
java -jar gzoltarcli.jar version
```
