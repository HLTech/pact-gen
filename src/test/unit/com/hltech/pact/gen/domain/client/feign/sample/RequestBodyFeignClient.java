package com.hltech.pact.gen.domain.client.feign.sample;

import com.hltech.pact.gen.domain.RequestType;
import com.hltech.pact.gen.domain.client.feign.InteractionInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("SpecProvider")
public interface RequestBodyFeignClient {

    @GetMapping(path = "/")
    @InteractionInfo(responseStatus = HttpStatus.OK)
    void getTestObject(@RequestBody List<RequestType> request);

    @PostMapping(path = "/")
    @InteractionInfo(responseStatus = HttpStatus.OK)
    void postTestObject(RequestType request);
}
