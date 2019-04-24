package com.hltech.pact.gen.domain.client.feign.sample;

import com.hltech.pact.gen.domain.client.feign.InteractionInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("SpecProvider")
public interface RequestTypeFeignClient {

    @DeleteMapping("/")
    @InteractionInfo(responseStatus = HttpStatus.OK)
    void deleteTestObject();

    @GetMapping("/")
    @InteractionInfo(responseStatus = HttpStatus.OK)
    void getTestObject();

    @RequestMapping(path = "/", method = RequestMethod.HEAD)
    @InteractionInfo(responseStatus = HttpStatus.OK)
    void headTestObject();

    @RequestMapping(path = "/", method = RequestMethod.OPTIONS)
    @InteractionInfo(responseStatus = HttpStatus.OK)
    void optionsTestObject();

    @PatchMapping("/")
    @InteractionInfo(responseStatus = HttpStatus.OK)
    void patchTestObject();

    @PostMapping("/")
    @InteractionInfo(responseStatus = HttpStatus.OK)
    void postTestObject();

    @PutMapping("/")
    @InteractionInfo(responseStatus = HttpStatus.OK)
    void putTestObject();

    @RequestMapping(path = "/", method = RequestMethod.TRACE)
    @InteractionInfo(responseStatus = HttpStatus.OK)
    void traceTestObject();
}
