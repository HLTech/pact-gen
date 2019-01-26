package com.hltech.pact.gen.domain.client.annotation;

import com.google.common.collect.Lists;
import com.hltech.pact.gen.domain.client.annotation.handlers.AnnotatedMethodHandler;

import java.util.List;

public class HandlersFactory {

    List<AnnotatedMethodHandler> createHandlers(List<Class<?>> classes)
        throws IllegalAccessException, InstantiationException {

        List<AnnotatedMethodHandler> result = Lists.newArrayList();

        for (Class<?> handlerClass : classes) {
            result.add((AnnotatedMethodHandler) handlerClass.newInstance());
        }

        return result;
    }
}
