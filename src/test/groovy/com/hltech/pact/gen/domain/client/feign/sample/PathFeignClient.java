package com.hltech.pact.gen.domain.client.feign.sample;

import com.hltech.pact.gen.domain.client.feign.InteractionInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface PathFeignClient {

    @FeignClient(name = "SpecProvider", path = "/common")
    interface FirstPathFeignClient {

        @GetMapping(path = "/test/{testId}/objects/{anotherTestId}/item")
        @InteractionInfo(responseStatus = HttpStatus.OK)
        void getTestObject(@PathVariable("testId") Long id, @PathVariable("anotherTestId") String anotherId);
    }

    @FeignClient(name = "SpecProvider", path = "/common/")
    interface SecondPathFeignClient {

        @GetMapping(path = "/test/{testId}/objects/{anotherTestId}/item")
        @InteractionInfo(responseStatus = HttpStatus.OK)
        void getTestObject(@PathVariable("testId") Long id, @PathVariable("anotherTestId") String anotherId);
    }

    @FeignClient(name = "SpecProvider", path = "/common")
    interface ThirdPathFeignClient {

        @GetMapping(path = "test/{testId}/objects/{anotherTestId}/item")
        @InteractionInfo(responseStatus = HttpStatus.OK)
        void getTestObject(@PathVariable("testId") Long id, @PathVariable("anotherTestId") String anotherId);
    }

    @FeignClient(name = "SpecProvider", path = "/common/")
    interface FourthPathFeignClient {

        @GetMapping(path = "test/{testId}/objects/{anotherTestId}/item")
        @InteractionInfo(responseStatus = HttpStatus.OK)
        void getTestObject(@PathVariable("testId") Long id, @PathVariable("anotherTestId") String anotherId);
    }
}
