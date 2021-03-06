package com.hltech.pact.gen.domain.client.util


import com.hltech.pact.gen.domain.GenericResponseType
import com.hltech.pact.gen.domain.TestParam
import spock.lang.Specification
import spock.lang.Unroll

import java.lang.reflect.Type

@Unroll
class TypeExtractorUT extends Specification {

    def "should correctly extract parameter type"(Type type, List<Class<?>> clazz) {
        expect:
            TypeExtractor.extractParameterTypesFromType(type) == clazz

        where:
            type << TypeExtractorUT.methods.find {it.name == 'testMethod'}.getParameters().collect { param ->
                param.getParameterizedType()
            }
            clazz << [[], [], [], [Long], [TestParam, String], [int]]
    }

    def "should return empty list for null type"() {
        expect:
            TypeExtractor.extractParameterTypesFromType(null).size() == 0
    }

    static void testMethod(String string,
                           Long aLong,
                           TestParam testParam,
                           GenericResponseType<Long> longList,
                           Map<TestParam, String> paramSet,
                           int[] ids) {
    }
}
