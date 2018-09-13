package dev.hltech.pact.generation.domain.client.util

import dev.hltech.pact.generation.domain.TestParam
import spock.lang.Specification
import spock.lang.Subject

import java.lang.reflect.Parameter


class ParameterTypeExtractorSpec extends Specification {

    @Subject
    def parameterTypeExtractor = new ParameterTypeExtractor()

    def "should correctly extract parameter type"(Parameter param, Class<?> type) {
        expect:
            parameterTypeExtractor.extractParameterType(param) == type

        where:
            param << ParameterTypeExtractorSpec.methods.find {it.name == 'testMethod'}.getParameters()
            type << [String, Long, TestParam, Long, TestParam, int]
    }

    static void testMethod(String string,
                           Long aLong,
                           TestParam testParam,
                           List<Long> longList,
                           Set<TestParam> paramSet,
                           int[] ids) {
    }
}
