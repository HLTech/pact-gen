package com.hltech.pact.gen.domain.client.feign.sample;

import com.hltech.pact.gen.domain.client.feign.InteractionInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("SpecProvider")
public interface ExpectedEmptyResponseBodyFeignClient {

    @PostMapping(path = "/")
    @InteractionInfo(responseStatus = HttpStatus.OK, emptyBodyExpected = true)
    void createTestObject();
}
