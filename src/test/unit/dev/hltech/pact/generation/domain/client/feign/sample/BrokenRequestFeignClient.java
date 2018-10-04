package dev.hltech.pact.generation.domain.client.feign.sample;

import dev.hltech.pact.generation.domain.BrokenRequestType;
import dev.hltech.pact.generation.domain.client.feign.InteractionInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("SpecProvider")
public interface BrokenRequestFeignClient {

    @GetMapping(path = "/")
    @InteractionInfo(responseStatus = HttpStatus.OK)
    void getTestObject(@RequestBody BrokenRequestType request);
}
