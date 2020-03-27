package com.hltech.pact.gen.domain.client.annotation;

import com.hltech.pact.gen.domain.client.annotation.handlers.AnnotatedMethodHandler;

import java.util.ArrayList;
import java.util.List;

public class HandlersFactory {

    List<AnnotatedMethodHandler> createHandlers(List<Class<?>> classes)
        throws IllegalAccessException, InstantiationException {

        List<AnnotatedMethodHandler> result = new ArrayList<>();

        for (Class<?> handlerClass : classes) {
            result.add((AnnotatedMethodHandler) handlerClass.newInstance());
        }

        return result;
    }
}
