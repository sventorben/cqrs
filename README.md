# CQRS
A lightweight CQRS framework.

[![Build Status](https://travis-ci.org/sventorben/cqrs.svg?branch=master)](https://travis-ci.org/sventorben/cqrs)

[![Code Coverage](https://img.shields.io/codecov/c/github/sventorben/cqrs/master.svg)](https://codecov.io/github/sventorben/cqrs?branch=master)

## Maven Artifacts

To integrate CQRS in your projects, you can use a maven dependency as follows:

```
    <dependency>
        <groupId>de.sven-torben.cqrs</groupId>
        <artifactId>domain</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
```

You will also need to integrate the Sonatype Maven repositories. 

```
    <repository>
        <id>sonatype-nexus-snapshots</id>
        <name>Sonatype Nexus Snapshots</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository
    
    <repository>
        <id>sonatype-nexus-releases</id>
        <name>Sonatype Nexus Snapshots</name>
        <url>https://oss.sonatype.org/content/repositories/releases</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
```