package dev.hltech.pact.generation.domain.client.util

import dev.hltech.pact.generation.domain.TestParam
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.lang.reflect.Parameter

@Unroll
class TypeExtractorSpec extends Specification {

    @Subject
    def parameterTypeExtractor = new TypeExtractor()

    def "should correctly extract parameter type"(Parameter param, Class<?> type) {
        expect:
            parameterTypeExtractor.extractTypesFromParameter(param) == type

        where:
            param << TypeExtractorSpec.methods.find {it.name == 'testMethod'}.getParameters()
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
