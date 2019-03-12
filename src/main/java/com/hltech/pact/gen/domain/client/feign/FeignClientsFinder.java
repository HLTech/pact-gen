package com.hltech.pact.gen.domain.client.feign;

import org.reflections.Reflections;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Set;
import java.util.stream.Collectors;

public class FeignClientsFinder {

    public Set<Class<?>> findFeignClients(String packageRoot) {
        return new Reflections(packageRoot).getTypesAnnotatedWith(FeignClient.class).stream()
            .filter(feignClient -> !feignClient.isAnnotationPresent(ExcludeFeignClient.class))
            .collect(Collectors.toSet());
    }
}
