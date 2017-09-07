# GZoltar Java Library for Automatic Debugging

A maven plugin that runs a Java project's test cases and computes several runtime metrics.
It runs test cases via [Surefire](https://maven.apache.org/surefire/maven-surefire-plugin/), so it supports both JUnit3 and Junit4 (See [Caveats section](#caveats)).

The plugin generates a report for the following metrics:

* Matrix density (Rho);
* Simpson's diversity index;
* Component ambiguity score (Uniqueness);
* DDU;
* Entropy;
* Coverage with respect to the chosen instrumentation granularity.

This plugin uses some of the internals of [Crowbar](http://crowbar.io/) and [GZoltar](http://gzoltar.com/).

## Compilation and Installation

To compile the project and install it in your local maven repository, simply run the command:
```
mvn install
```

## Usage

Add the following to a Java project's `pom.xml`:
```
<build>
  <pluginManagement>
    <plugins>
      <plugin>
        <groupId>com.gzoltar</groupId>
        <artifactId>com.gzoltar.maven</artifactId>
        <version>0.0.1-SNAPSHOT</version>
      </plugin>
    </plugins>
  </pluginManagement>
</build>
```

To run the project's test cases and the metrics analysis, execute the command:
```
mvn gzoltar:test
```


### Instrumentation Granularity Configuration
By default, `com.gzoltar.maven` instruments classes at the `method` granularity level. The `line` and `basicblock` granularity levels are also available. To use them, simply add the following configuration to the plugin declaration:
```
<build>
  <pluginManagement>
    <plugins>
      <plugin>
        <groupId>com.gzoltar</groupId>
        <artifactId>com.gzoltar.maven</artifactId>
        <version>1.1-SNAPSHOT</version>
        <configuration>
          <granularityLevel>line</granularityLevel>
        </configuration>
      </plugin>
    </plugins>
  </pluginManagement>
</build>
```

### Other Useful Configurations

To filter classes from being instrumented, match prefixes (i.e., by using the `String#startsWith` method) against each fully qualified class name as classes are being loaded by the JVM:
```
<prefixesToFilter>
  <prefix>filter.this.package</prefix>
  <prefix>filter.this.Class</prefix>
</prefixesToFilter>
```

Restrict classes allowed for instrumentation:
```
<classesToInstrument>
  <class>fully.qualified.ClassName</class>
  <class>fully.qualified.ClassName2</class>
</classesToInstrument>
```

Only instrument classes located inside `${project.build.directory}` (default is `true`):
```
<restrictOutputDirectory>true</restrictOutputDirectory>
```

Only instrument public methods (default is `false`):
```
<restrictToPublicMethods>true</restrictToPublicMethods>
```

Report directory (default is `${project.build.directory}/gzoltar-report`):
```
<reportDirectory>/temp/reports</reportDirectory>
```

Add arbitrary JVM options:
```
<argLine>-Xmx512m</argLine>
```

## Caveats

#### Junit3 and Junit4 Compatibility

Although `com.gzoltar.maven` works for both Junit3 and Junit4 test cases, please make sure that the Junit dependency included in the project's `pom.xml` is at least **version 4.6**.

This requirement is due to the fact that Junit only provides their `org.junit.runner.notification.RunListener` API after version 4.6. The listener API is used so that per-test coverage can be gathered.
For Junit3 test cases, the appropriate test runner will still be used.

#### argLine

If there is an `argLine` parameter set in the declaration of `maven-surefire-plugin`, that will override `com.gzoltar.maven`'s request to add an agent to the test JVM.
To circumvent this, you can add your JVM options in `com.gzoltar.maven`'s `argLine` parameter detailed [here](#other-useful-configurations).

#### restrictOutputDirectory

To know the location of each class at runtime, we use the `java.security.ProtectionDomain` API. Depending on the security policy being enforced, access to that specific API may be blocked.
