package com.hltech.pact.gen.domain.client.feign.sample;

import com.hltech.pact.gen.domain.client.feign.ExcludeFeignInteraction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ExcludedFeignClient", path = "/")
public interface ExcludedInteractionFeignClient {

    @ExcludeFeignInteraction
    @GetMapping
    void get();
}
