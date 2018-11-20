package com.hltech.pact.gen.domain.client.feign.sample;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@FeignClient("SpecProvider")
public interface OptionalResponseFeignClient {

    @GetMapping(path = "/")
    Optional<String> getTestObject();
}
