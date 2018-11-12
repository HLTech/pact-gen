# PACT Generator

[![Build Status](https://travis-ci.org/HLTech/pact-gen.svg?branch=master)](https://travis-ci.org/HLTech/pact-gen.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/HLTech/pact-gen/badge.svg?branch=master)](https://coveralls.io/github/HLTech/pact-gen?branch=master)

## Table of Contents
1. [**Overview**](#Overview)
2. [**Prerequisites**](#Prerequisites)
3. [**Quick start**](#QuickStart)
4. [**Built with**](#BuiltWith)
5. [**Authors**](#Authors)
6. [**License**](#License)

## Overview <a name="Overview"></a>

This repository contains library for generating [pact](https://pact.io) files out of [Feign](https://github.com/OpenFeign/feign) clients.

## Prerequisites <a name="Prerequisites"></a>

First, the most important thing is that pact-gen is only able to generate pact 
files out of Feign clients (at the moment - we have support for 
other http clients in our roadmap).

Our main goal was to create library that doesn't make you write additional boilerplate. However
it was not possible to generate fully useful pact files by using only Feign clients features.

Here are conventions that we made:
* `@FeignClient` annotation has a property `name` - value of that property will be used
as a provider name
* Feign client's method name will be used as a name for pact interaction
* we have added custom annotation `@ResponseInfo` that contains information about
expected status(es) and header(s) of REST call
* example request/response body is generated using [PODAM](http://mtedone.github.io/podam/) library - so our pojos 
have to be compliant with its requirements: 
    * no argument constructor and setters
    * all argument constructor for immutable objects
* request/response classes must have getters - otherwise pact-gen won't be able to serialize it

## Quick start <a name="QuickStart"></a>

### Add pact-gen dependency

Add pact-gen as a dependency to your project:

if you are using gradle add it to build.gradle:
```groovy
dependencies {
    ...
    compile "com.hltech:pact-gen:version"
}
```

if you are using maven add it to pom.xml:
```xml
  <dependencies>
    <dependency>
      <groupId>com.hltech</groupId>
      <artifactId>pact-gen</artifactId>
      <version>version</version>
    </dependency>
  </dependencies>
```

After your build tool will be able to resolve that dependency you just have to 
write simple integration test (you need your application context so your object mapper 
is properly configured). Such test may look like this:

### Generate pact files

```groovy
package com.hltech.pact.gen.example

import com.fasterxml.jackson.databind.ObjectMapper
import com.hltech.pact.gen.PactGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import spock.lang.Specification

@SpringBootTest
class ContractTestsGenerator extends Specification {

    @Autowired
    private ObjectMapper objectMapper

    private PactGenerator pactGenerator = new PactGenerator()

    def "should generate pact file"() {
        expect:
            pactGenerator.writePactFiles("com.hltech.rest", "consumer-name", objectMapper, new File("build/pacts/"))
    }
}
```

This test will:
* scan package `com.hltech.rest` and its children looking for Feign clients
* generate separate pact files for each provider that your application 
communicates with via previously found Feign clients
* bodies of request/response will be serialized to json using YOUR object mapper 
so pact-gen doesn't overwrite any of your configurations
* write generated pact files to root_of_your_app/build/pacts/ directory

### Further steps

You can take profit of your generated pact files by testing via [Judge-D](https://github.com/HLTech/judge-d) - an
open-source engine for contract testing. 

## Built with <a name="BuiltWith"></a>

* [Gradle](https://gradle.org/) - dependency management & build tool 
* [Reflections](https://github.com/ronmamo/reflections) - runtime metadata analysis
* [PODAM](http://mtedone.github.io/podam/) - POJO filler
* [Lombok](https://projectlombok.org/) - because who likes boilerplate
* [Spock](http://spockframework.org/) - for beautiful tests

## Authors <a name="Authors"></a>

* **Filip Łazarski** - *Development* - [Felipe444](https://github.com/Felipe444)
* **Adrian Michalik** - *Development* - [garlicsauce](https://github.com/garlicsauce)

## License <a name="License"></a>

TODO
