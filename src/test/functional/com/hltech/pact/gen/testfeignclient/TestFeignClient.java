package com.hltech.pact.gen.testfeignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(decode404 = true, name = "test-provider", url = "test-url")
public interface TestFeignClient {

    @GetMapping(value = "/{integerClassPathVariable}/{booleanPathVariable}/}")
    Optional<TestDto> getTestDto(
        @PathVariable("integerClassPathVariable") Integer integerClassPathVariable,
        @PathVariable("booleanPathVariable") boolean booleanPathVariable,
        @RequestParam(name = "floatRequestParam", defaultValue = "1.2312342") float floatRequestParam,
        @RequestParam(name = "stringRequestParam", defaultValue = "abcde") String stringRequestParam
    );

    @PostMapping(value = "/")
    GenericDto<TestDto> postTestDto(TestDto testDto);
}
