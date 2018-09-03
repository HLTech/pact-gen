package dev.hltech.pact.generation.domain.client.feign;

import org.reflections.Reflections;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Set;

public class FeignClientsFinder {

    public Set<Class<?>> findFeignClients(String packageRoot) {
        return new Reflections(packageRoot).getTypesAnnotatedWith(FeignClient.class);
    }
}
