package com.hltech.pact.gen.domain.client.annotation

import com.hltech.pact.gen.domain.client.annotation.handlers.DeleteMappingMethodsHandler
import com.hltech.pact.gen.domain.client.annotation.handlers.JaxRsGetMappingMethodsHandler
import com.hltech.pact.gen.domain.client.annotation.handlers.PatchMappingMethodsHandler
import com.hltech.pact.gen.domain.client.annotation.handlers.PostMappingMethodsHandler
import com.hltech.pact.gen.domain.client.annotation.handlers.PutMappingMethodsHandler
import com.hltech.pact.gen.domain.client.annotation.handlers.RequestMappingMethodsHandler
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class MappingHandlerFactoryUT extends Specification {

    private HandlersFactory handlersFactoryMock = Mock()

    @Subject
    private MappingHandlerFactory factory = new MappingHandlerFactory(handlersFactoryMock)

    def 'should create all instances of handlers'() {
        given:
            final def expectedClasses = [
                    DeleteMappingMethodsHandler, JaxRsGetMappingMethodsHandler, PatchMappingMethodsHandler,
                    PostMappingMethodsHandler, PutMappingMethodsHandler, RequestMappingMethodsHandler,
                    JaxRsGetMappingMethodsHandler
            ]

            1 * handlersFactoryMock.createHandlers(_ as List<Class<?>>) >> { arguments ->
                ((List<Class<?>>) arguments[0]).collect { it.newInstance() }
            }

        expect:
            with(factory.createAll().collect { it.getClass() }) {
                it.size() == expectedClasses.size()
                it.containsAll(expectedClasses)
            }
    }

    @Unroll
    def 'should throw MappingMethodCreationException when handler factory throw exception'() {
        given:
            1 * handlersFactoryMock.createHandlers(_ as List<Class<?>>) >> { throw exception }

        when:
            factory.createAll()

        then:
            def thrownException = thrown MappingMethodCreationException
            thrownException.cause == exception

        where:
            exception << [new IllegalAccessException(), new InstantiationException()]
    }
}
