package dev.hltech.pact.generation.domain.client.feign.sample;

import dev.hltech.pact.generation.domain.BrokenResponseType;
import dev.hltech.pact.generation.domain.client.feign.InteractionInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("SpecProvider")
public interface BrokenResponseFeignClient {

    @GetMapping(path = "/")
    @InteractionInfo(responseStatus = HttpStatus.OK)
    BrokenResponseType getBrokenTestObject();
}
