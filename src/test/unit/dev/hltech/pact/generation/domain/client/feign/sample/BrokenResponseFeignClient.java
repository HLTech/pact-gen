package dev.hltech.pact.generation.domain.client.feign.sample;

import dev.hltech.pact.generation.domain.BrokenResponseType;
import dev.hltech.pact.generation.domain.client.feign.ResponseInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("SpecProvider")
public interface BrokenResponseFeignClient {

    @GetMapping(path = "/")
    @ResponseInfo(status = HttpStatus.OK)
    BrokenResponseType getBrokenTestObject();
}
