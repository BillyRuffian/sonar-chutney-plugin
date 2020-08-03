SonarQube Cucumber Gherkin Lint Plugin
==========

<div align="center">

  ![CircleCI branch](https://img.shields.io/circleci/project/github/BillyRuffian/sonar-chutney-plugin/master.svg?style=flat-square)
  ![GitHub tag (latest SemVer)](https://img.shields.io/github/tag/BillyRuffian/sonar-chutney-plugin.svg?style=flat-square)

</div>

This plugin uses the [Chutney](https://billyruffian.github.io/chutney/) linter and integrates into SonarQube 8.x.

Prerequistes
--------

* SonarQube 8.x
* Chutney (chutney is a ruby gem, but, once installed, will lint any language's feature files).
* A Chutney configuration file (see chutney's documentation) 

### Building

To build the plugin JAR file, call:

```
mvn clean package
```

The JAR will be deployed to `target/sonar-chutney-plugin-VERSION.jar`. Copy this to your SonarQube's `extensions/plugins/` directory, and re-start SonarQube.

Configuration
---------

You can set the location of the chutney executable via the `sonar.chutney.executable` property in your `sonar-project.properties`. It defaults to `chutney` which will work for most cases if the executable is on your path. 