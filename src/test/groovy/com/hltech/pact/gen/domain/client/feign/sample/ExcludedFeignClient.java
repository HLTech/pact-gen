package com.hltech.pact.gen.domain.client.feign.sample;

import com.hltech.pact.gen.domain.client.feign.ExcludeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ExcludedFeignClient", path = "/")
@ExcludeFeignClient
public interface ExcludedFeignClient {

    @GetMapping
    void get();
}
