# Change Log

All notable changes will be documented in this file.

## [Unreleased]

- GZoltar now depends on javassist v3.23.1-GA

### New Features

- GZoltar Agent options:
  - `buildlocation` - directory with classes to instrument
  - `destfile` - the output file for spectrum data
  - `includes` - list of classes, separated by ':', allowed to be instrumented
  (by default all classes are) (wildcards are allowed)
  - `excludes` - list of classes, separated by ':', not allowed to be
  instrumented (by default no class is excluded) (wildcards are allowed)
  - `exclclassloader` - list of Java ClassLoaders not allowed to be instrumented
  (wildcards are allowed)
  - `inclnolocationclasses` - specifies whether also classes without a source
  location should be instrumented
  - `output` -- specifies the output mode, valid options are file, console, or
  none
  - `inclPublicMethods` - specifies whether public methods of each class under
  test should be instrumented
  - `inclStaticConstructors` - specifies whether public static constructors of
  each class under test should be instrumented
  - `inclDeprecatedMethods` - specifies whether methods annotated with the Java
  tag `@deprecated` should be instrumented

- Coverage
  - Listener for test cases written in JUnit, and TestNG
  - Excluded from instrumentation
    - methods 'values' and 'valueOf' of enum classes
    - synthetic methods (unless they represent bodies of lambda expressions)
    - test classes (i.e., test cases that either contain JUnit or TestNG test
      cases)
  - Coverage is serialised to a file called (by default) 'gzoltar.ser' once all
  test cases have been executed

- Offline coverage is defined by two steps: 1) offline instrumentation, and
2) coverage

- Fault localization report
  - families: spectrum-based fault localization (sfl)
    - formulas: Anderberg, Barinel, DStar, Ideal, Jaccard, Kulczynski2, Naish1,
    Ochiai, Ochiai2, Opt, RogersTanimoto, RusselRao, SBI, SimpleMatching,
    SorensenDice, Tarantula
  - format
    - txt - simple text-based fault localization report
    - html - three different html-based fault localization report
    (BubbleHierarchy, Sunburst, VerticalPartition)

- Plugins/Tasks
  - Ant (tasks: coverage, instrument, fl-report)
  - Maven (goals: prepare-agent, instrument, fl-report)
  - Command line interface (commands: instrument, listTests, version)

### Non-functional Changes

- Rename all modules to 'com.gzoltar'
- Format all .java files according to google style guide
- Improve build infrastructure
- Remove FlexJSON dependency
- Generate Javadoc for each module and include it in the distribution package
- Generate jar files with source files
