# PACT Generator

[![Build Status](https://travis-ci.org/HLTech/pact-gen.svg?branch=master)](https://travis-ci.org/HLTech/pact-gen)
[![Coverage Status](https://coveralls.io/repos/github/HLTech/pact-gen/badge.svg?branch=master)](https://coveralls.io/github/HLTech/pact-gen?branch=master)
[![Scrutinizer Code Quality](https://scrutinizer-ci.com/g/HLTech/pact-gen/badges/quality-score.png?b=master)](https://scrutinizer-ci.com/g/HLTech/pact-gen/?branch=master)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Table of Contents
1. [**Overview**](#Overview)
2. [**Motivation**](#Motivation)
3. [**Prerequisites**](#Prerequisites)
4. [**Quick start**](#QuickStart)
5. [**Interaction info**](#InteractionInfo)
6. [**Built with**](#BuiltWith)
7. [**Authors**](#Authors)
8. [**License**](#License)

## Overview <a name="Overview"></a>

This repository contains library for generating [pact](https://pact.io) files out of [Feign](https://github.com/OpenFeign/feign) clients.

## Motivation

To generate Pact files, tests for each interaction between service consumer and provider must be written. As number of 
microservices and hence number of interaction grows, writing such tests becomes bigger burden. As pact files are currently
the only format of expectations supported by Judge Dredd, automatic generation of Pact files makes it much faster to perform
contract tests. What is more developers don't need to update Pact tests each time interactions between services is changed.

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
* we have added custom annotation `@InteractionInfo` - more about this annotation can be found [here](#InteractionInfo)
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

## Interaction info annotation <a name="InteractionInfo"></a>

We have added custom annotation `@InteractionInfo` that contains information about:
* HTTP status(es) (required)

`@InteractionInfo(responseStatus = HttpStatus.OK)` - will add information to pact file that expected
status is `200 OK`

* header(s) (optional)

`@InteractionInfo(responseStatus = HttpStatus.OK, responseHeaders = {"key1=val1", "key2=val2"})` - will
add information to pact file, that expected status is `200 OK` and that we expect two headers in response:
first with name `key1` and value `val1` and second with name `key2` and value `val2`
 
* interaction name (optional)

`@InteractionInfo(responseStatus = HttpStatus.OK, description = "Update test object in the test service")` - will
add information to pact file, that expected status is `200 OK` and that interaction is called 
`Update test object in the test service`

* if expecting empty response (optional)

`@InteractionInfo(responseStatus = HttpStatus.OK, emptyBodyExpected = true)` - will add information to pact file
that expected status is `200 OK` and that (despite declared return type) we expect response to be empty.

`@InteractionInfo` can be aggregated thanks to `@InteractionsInfo` annotation - example usage:

```
@InteractionsInfo({
    @InteractionInfo(responseStatus = HttpStatus.NOT_FOUND),
    @InteractionInfo(responseStatus = HttpStatus.ACCEPTED)
})
```
such annotations on method will add information to pact file that we expect rest service to be able 
to return any of `404 NOT FOUND` and `202 ACCEPTED` HTTP statuses.

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

pact-gen is [MIT licensed](./LICENSE).
