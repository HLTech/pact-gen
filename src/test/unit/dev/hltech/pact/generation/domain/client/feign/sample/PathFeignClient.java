package dev.hltech.pact.generation.domain.client.feign.sample;

import dev.hltech.pact.generation.domain.client.feign.InteractionInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("SpecProvider")
public interface PathFeignClient {

    @GetMapping(path = "/test/{testId}/objects/{anotherTestId}/item")
    @InteractionInfo(responseStatus = HttpStatus.OK)
    void getTestObject(@PathVariable("testId") Long id, @PathVariable("anotherTestId") String anotherId);
}
