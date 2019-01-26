package com.hltech.pact.gen.domain.client.annotation

import com.hltech.pact.gen.domain.client.annotation.handlers.DeleteMappingMethodsHandler
import com.hltech.pact.gen.domain.client.annotation.handlers.GetMappingMethodsHandler
import com.hltech.pact.gen.domain.client.annotation.handlers.PatchMappingMethodsHandler
import com.hltech.pact.gen.domain.client.annotation.handlers.PostMappingMethodsHandler
import com.hltech.pact.gen.domain.client.annotation.handlers.PutMappingMethodsHandler
import com.hltech.pact.gen.domain.client.annotation.handlers.RequestMappingMethodsHandler
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class HandlersFactorySpec extends Specification {

    @Subject
    private HandlersFactory factory = new HandlersFactory()

    def 'should create instances of given classes'() {
        given:
            def classes = [
                DeleteMappingMethodsHandler, GetMappingMethodsHandler, PatchMappingMethodsHandler,
                PostMappingMethodsHandler, PutMappingMethodsHandler, RequestMappingMethodsHandler
            ]

        expect:
            factory.createHandlers(classes).size() == classes.size()
    }

    @Unroll
    def 'should throw exception when cannot create instance'() {
        when:
            factory.createHandlers([classToGetInstance])

        then:
            thrown expectedException

        where:
            classToGetInstance  || expectedException
            SampleAbstractClass || InstantiationException
            Class               || IllegalAccessException
    }


    abstract class SampleAbstractClass {
    }
}
