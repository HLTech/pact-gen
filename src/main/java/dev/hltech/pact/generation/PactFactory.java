package dev.hltech.pact.generation;

import org.springframework.cloud.openfeign.FeignClient;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PactFactory {

    public Pact create(Class<?> feignClient, String consumerName) {
        return Pact.builder()
            .provider(new Service(feignClient.getAnnotation(FeignClient.class).value()))
            .consumer(new Service(consumerName))
            .interactions(createInteractions(feignClient.getMethods()))
            .metadata(new Metadata())
            .build();
    }

    private static List<Interaction> createInteractions(Method[] feignClientMethods) {
        return Arrays.stream(feignClientMethods)
            .map(method -> new Interaction())
            .collect(Collectors.toList());
    }
}
