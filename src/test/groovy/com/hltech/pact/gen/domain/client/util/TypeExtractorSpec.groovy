package com.hltech.pact.gen.domain.client.util


import com.hltech.pact.gen.domain.GenericResponseType
import com.hltech.pact.gen.domain.TestParam
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.lang.reflect.Type

@Unroll
class TypeExtractorSpec extends Specification {

    @Subject
    def parameterTypeExtractor = new TypeExtractor()

    def "should correctly extract parameter type"(Type type, List<Class<?>> clazz) {
        expect:
            parameterTypeExtractor.extractParameterTypesFromType(type) == clazz

        where:
            type << TypeExtractorSpec.methods.find {it.name == 'testMethod'}.getParameters().collect { param ->
                param.getParameterizedType()
            }
            clazz << [[], [], [], [Long], [TestParam, String], [int]]
    }

    static void testMethod(String string,
                           Long aLong,
                           TestParam testParam,
                           GenericResponseType<Long> longList,
                           Map<TestParam, String> paramSet,
                           int[] ids) {
    }
}
