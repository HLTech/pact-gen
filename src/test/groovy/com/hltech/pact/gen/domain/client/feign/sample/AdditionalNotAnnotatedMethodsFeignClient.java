package com.hltech.pact.gen.domain.client.feign.sample;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@FeignClient("SpecProvider")
public interface AdditionalNotAnnotatedMethodsFeignClient {

    @GetMapping(path = "/")
    Optional<String> getTestObject();

    default String fetchSomething() {
        return "testResponse";
    }
}
