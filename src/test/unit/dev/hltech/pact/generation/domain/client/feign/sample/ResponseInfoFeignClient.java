package dev.hltech.pact.generation.domain.client.feign.sample;

import dev.hltech.pact.generation.domain.client.feign.InteractionInfo;
import dev.hltech.pact.generation.domain.client.feign.InteractionsInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("SpecProvider")
public interface ResponseInfoFeignClient {

    @GetMapping(path = "/")
    @InteractionsInfo({
            @InteractionInfo(responseStatus = HttpStatus.NOT_FOUND),
            @InteractionInfo(responseStatus = HttpStatus.ACCEPTED)
    })
    void getTestObject();

    @PostMapping(path = "/")
    void postTestObject();
}
