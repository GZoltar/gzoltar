# Approximate Entropy Score (AES) maven plugin

A maven plugin that runs a Java project's test cases and computes several runtime metrics. 
It runs test cases via [Surefire](https://maven.apache.org/surefire/maven-surefire-plugin/), so it supports both JUnit3 and Junit4 (see [Caveats section](#caveats)).

The plugin generates a report for the following metrics:

* Matrix density (Rho);
* Simpson's diversity index;
* Component ambiguity score;
* Entropy;
* Approximate Entropy;
* Coverage with respect to the chosen instrumentation granularity.
 
Also generated is a heatmap-like tree visualization that depicts the amount of unique test coverage traces exercising each component in the system.

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
        <groupId>pt.up.fe.aes</groupId>
        <artifactId>aes-maven-plugin</artifactId>
        <version>1.1-SNAPSHOT</version>
      </plugin>
    </plugins>
  </pluginManagement>
</build>
```

To run the project's test cases and the metrics analysis, execute the command:
```
mvn aes:test
```


### Instrumentation Granularity Configuration
By default, `aes-maven-plugin` instruments classes at the `method` granularity level. The `line` and `basicblock` granularity levels are also available. To use them, simply add the following configuration to the plugin declaration:
```
<build>
  <pluginManagement>
    <plugins>
      <plugin>
        <groupId>pt.up.fe.aes</groupId>
        <artifactId>aes-maven-plugin</artifactId>
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

Report directory (default is `${project.build.directory}/aes-report`):
```
<reportDirectory>/temp/reports</reportDirectory>
```

Add arbitrary JVM options:
```
<argLine>-Xmx512m</argLine>
```

## Caveats

#### Junit3 and Junit4 Compatibility

Although `aes-maven-plugin` works for both Junit3 and Junit4 test cases, please make sure that the Junit dependency included in the project's `pom.xml` is at least **version 4.6**.
 
This requirement is due to the fact that Junit only provides their `org.junit.runner.notification.RunListener` API after version 4.6. The listener API is used so that per-test coverage can be gathered.
For Junit3 test cases, the appropriate test runner will still be used.

#### argLine

If there is an `argLine` parameter set in the declaration of `maven-surefire-plugin`, that will override `aes-maven-plugin`'s request to add an agent to the test JVM. 
To circumvent this, you can add your JVM options in `aes-maven-plugin`'s `argLine` parameter detailed [here](#other-useful-configurations).

#### restrictOutputDirectory

To know the location of each class at runtime, we use the `java.security.ProtectionDomain` API. Depending on the security policy being enforced, access to that specific API may be blocked.
