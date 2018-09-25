package dev.hltech.pact.generation.domain.client.feign.sample;

import dev.hltech.pact.generation.domain.GenericResponseType;
import dev.hltech.pact.generation.domain.ResponseType;
import dev.hltech.pact.generation.domain.TestParam;
import dev.hltech.pact.generation.domain.client.feign.ResponseInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("SpecProvider")
public interface ResponseBodyFeignClient {

    @GetMapping(path = "/")
    @ResponseInfo(status = HttpStatus.OK)
    void getTestObject();

    @PostMapping(path = "/")
    @ResponseInfo(status = HttpStatus.OK)
    ResponseType postTestObject();

    @PutMapping(path = "/")
    @ResponseInfo(status = HttpStatus.OK)
    GenericResponseType<TestParam> putTestObject();
}
