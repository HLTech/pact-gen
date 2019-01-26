package com.hltech.pact.gen.domain.client.annotation;

import com.google.common.collect.Lists;
import com.hltech.pact.gen.domain.client.annotation.handlers.AnnotatedMethodHandler;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;

import java.util.List;

@RequiredArgsConstructor
public class MappingHandlerFactory {

    private static final String PACKAGE = "com.hltech.pact.gen";

    private final HandlersFactory handlersFactory;

    public List<AnnotatedMethodHandler> createAll() {
        try {
            return handlersFactory.createHandlers(findClasses());
        } catch (IllegalAccessException | InstantiationException exception) {
            throw new MappingMethodCreationException("Cannot create mapping handler", exception);
        }
    }

    private List<Class<?>> findClasses() {
        return Lists.newArrayList(new Reflections(PACKAGE).getTypesAnnotatedWith(MappingMethodHandler.class));
    }
}
