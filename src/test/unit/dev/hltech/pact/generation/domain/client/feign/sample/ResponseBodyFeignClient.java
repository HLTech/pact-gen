package dev.hltech.pact.generation.domain.client.feign.sample;

import dev.hltech.pact.generation.domain.GenericResponseType;
import dev.hltech.pact.generation.domain.ResponseType;
import dev.hltech.pact.generation.domain.TestParam;
import dev.hltech.pact.generation.domain.client.feign.InteractionInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("SpecProvider")
public interface ResponseBodyFeignClient {

    @GetMapping(path = "/")
    @InteractionInfo(responseStatus = HttpStatus.OK)
    void getTestObject();

    @PostMapping(path = "/")
    @InteractionInfo(responseStatus = HttpStatus.OK)
    ResponseType postTestObject();

    @PutMapping(path = "/")
    @InteractionInfo(responseStatus = HttpStatus.OK)
    GenericResponseType<TestParam> putTestObject();
}
