package dev.hltech.pact.generation.domain.client.feign.sample;

import dev.hltech.pact.generation.domain.RequestType;
import dev.hltech.pact.generation.domain.client.feign.ResponseInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("SpecProvider")
public interface RequestBodyFeignClient {

    @GetMapping(path = "/")
    @ResponseInfo(status = HttpStatus.OK)
    void getTestObject(@RequestBody RequestType request);

    @PostMapping(path = "/")
    @ResponseInfo(status = HttpStatus.OK)
    void postTestObject(RequestType request);
}
