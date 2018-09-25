package dev.hltech.pact.generation.domain.client.feign.sample;

import dev.hltech.pact.generation.domain.client.feign.ResponseInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("SpecProvider")
public interface ResponseHeadersFeignClient {

    @GetMapping(path = "/")
    @ResponseInfo(status = HttpStatus.OK, headers = {"key1=val1", "key2=val2"})
    void getTestObject();
}
