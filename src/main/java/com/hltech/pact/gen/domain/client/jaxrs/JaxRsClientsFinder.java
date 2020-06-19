package com.hltech.pact.gen.domain.client.jaxrs;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JaxRsClientsFinder {

    public Set<Class<?>> findJaxRsClients(String packageRoot) {
        Set<Class<?>> jaxRsClients = new HashSet<>();
        jaxRsClients.addAll(classAnnotatedClients(packageRoot));
        jaxRsClients.addAll(methodAnnotatedClients(packageRoot));

        return jaxRsClients;
    }

    private Set<Class<?>> classAnnotatedClients(String packageRoot) {
        return new Reflections(packageRoot)
            .getTypesAnnotatedWith(Path.class);
    }

    private Set<Class<?>> methodAnnotatedClients(String packageRoot) {
        return new Reflections(packageRoot, new MethodAnnotationsScanner())
            .getMethodsAnnotatedWith(Path.class).stream()
            .map(Method::getDeclaringClass)
            .collect(Collectors.toSet());
    }
}
