package dev.hltech.pact.generation;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

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
            .map(PactFactory::createInteraction)
            .collect(Collectors.toList());
    }

    private static Interaction createInteraction(Method feignClientMethod) {
        return Interaction.builder()
            .description(feignClientMethod.getName())
            .request(createInteractionRequest(feignClientMethod))
            .build();
    }

    private static InteractionRequest createInteractionRequest(Method feignClientMethod) {
        return InteractionRequest.builder()
            .method(feignClientMethod.getAnnotation(RequestMapping.class).method()[0].name())
            .path(feignClientMethod.getAnnotation(RequestMapping.class).path()[0])
            .build();
    }
}
