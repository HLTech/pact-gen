package com.hltech.pact.gen

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.node.DecimalNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.LongNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.hltech.pact.gen.domain.client.feign.FeignClientsFinder
import com.hltech.pact.gen.domain.pact.PactFactory
import com.hltech.pact.gen.domain.pact.PactJsonGenerator
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

class PactGeneratorFT extends Specification {

    static PACKAGE_ROOT = "com.hltech.pact.gen.testfeignclient"
    static PACT_DIRECTORY = "pacts/"
    static CONSUMER_NAME = "test-consumer"
    static PROVIDER_NAME = "test-provider"
    static SCHEMA_FILE_PATH = "src/test/resources/pact-json-schema-v1.json"
    static TEST_PACT_FILE_PATH = "${PACT_DIRECTORY}${CONSUMER_NAME}-${PROVIDER_NAME}.json"

    def feignClientsFinder = new FeignClientsFinder()
    def pactFactory = new PactFactory()
    def jsonGenerator = new PactJsonGenerator()
    def pactGenerator = new PactGenerator(feignClientsFinder, pactFactory, jsonGenerator)
    def mapper = new ObjectMapper()

    def schemaFactory = JsonSchemaFactory.byDefault()

    def setup() {
        mapper.registerModule(new JavaTimeModule())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    def cleanup() {
        new File(TEST_PACT_FILE_PATH).delete()
    }

    def "should create pact file with proper json schema and valid values"() {
        given: "expected pact json schema for version 1"
            def schema = getExpectedJsonSchema()

        when: "write pact files method is executed"
            pactGenerator.writePactFiles(PACKAGE_ROOT, CONSUMER_NAME, mapper, new File(PACT_DIRECTORY))

        then: "pact file is written with correct schema"
            def testFile = new File(TEST_PACT_FILE_PATH)
            def testJSON = JsonLoader.fromFile(testFile)
            def report = schema.validate(testJSON)
            report.isSuccess()

        and: "has correct interactions size"
            testJSON["interactions"].size() == 2

        and: "all required values are generated for GET request"
            def getInteraction = findInteraction(testJSON["interactions"], '"GET"')
            getInteraction["request"]["query"].toString() == '"floatRequestParam=1.2312342&stringRequestParam=abcde"'
            assertPathParams(getInteraction["request"]["path"])
            assertAllRequiredBodyValuesAreGenerated(getInteraction["response"]["body"])
            getInteraction["request"]["headers"].toString() == '{"Accept":"application/problem+xml","Content-Type":"application/json;charset=UTF-8"}'
            getInteraction["response"]["headers"].toString() == '{"Content-Type":"application/problem+xml"}'

        and: "all required values are generated for POST request"
            def postInteraction = findInteraction(testJSON["interactions"], '"POST"')
            assertAllRequiredBodyValuesAreGenerated(postInteraction["request"]["body"])
            postInteraction["response"]["body"]["data"].size() == 1
            assertAllRequiredBodyValuesAreGenerated(postInteraction["response"]["body"]["data"][0])
    }

    def getExpectedJsonSchema() {
        def schemaFile = new File(SCHEMA_FILE_PATH)
        def jsonNode = JsonLoader.fromFile(schemaFile)
        schemaFactory.getJsonSchema(jsonNode)
    }

    def assertAllRequiredBodyValuesAreGenerated(body) {
        isBoolean(body["booleanField"])
        isBoolean(body["booleanClassField"])
        isShort(body["shortField"])
        isShort(body["shortClassField"])
        isInteger(body["intField"])
        isInteger(body["integerClassField"])
        isInteger(body["bigIntegerField"])
        isLong(body["longField"])
        isLong(body["longClassField"])
        isFloat(body["floatField"])
        isFloat(body["floatClassField"])
        isDouble(body["doubleField"])
        isDouble(body["doubleClassField"])
        isBigDecimal(body["bigDecimalField"])
        LocalDate.parse(getDateAsString(body,"localDate"))
        LocalDateTime.parse(getDateAsString(body,"localDateTime"))
        ZonedDateTime.parse(getDateAsString(body,"zonedDateTime"))
        assert body["annotatedStringField"].toString() == '"testDefaultValue"'
        assert body["stringField"] instanceof TextNode
        assert body["charField"] instanceof TextNode
        return true
    }

    def assertPathParams(path) {
        def pathParams = path.toString().split("/").findAll{ it.length() > 2}
        isInteger(IntNode.valueOf(pathParams[0] as Integer))
        assert pathParams[1] == "true" || pathParams[1] == "false"
        return true
    }

    def isShort(value) {
        assert value instanceof IntNode && getDigitsNumber(value) in 1..5
    }

    def isInteger(value) {
        assert value instanceof IntNode && getDigitsNumber(value) in 6..10
    }

    def isLong(value) {
        assert value instanceof LongNode
    }

    def isBoolean(value) {
        assert value.class.simpleName == "BooleanNode"
    }

    def isFloat(value) {
        assert value instanceof DecimalNode && getDecimalScale(value) in 1..10
    }

    def isDouble(value) {
        assert value instanceof DecimalNode && getDecimalScale(value) > 11
    }

    def isBigDecimal(value) {
        def regexMatchingDecimalAndScientificNotation = '[+-]?([0-9]*[.])?[0-9]+(E-?[0-9]+)?'
        assert value.toString().matches(regexMatchingDecimalAndScientificNotation)
    }

    def getDecimalScale(value) {
        value.toString().split("\\.")[1].length()
    }

    def getDigitsNumber(value) {
        value.toString().length()
    }

    def getDateAsString(body, date) {
        body["dateTimeDto"][date].toString().replace('"','')
    }

    def findInteraction(interactions, method) {
        interactions.find {
            it["request"]["method"].toString() == method
        }
    }
}
