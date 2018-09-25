package dev.hltech.pact.generation.domain.client.feign.sample;

import dev.hltech.pact.generation.domain.client.feign.ResponseInfo;
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
    @ResponseInfo(status = HttpStatus.OK)
    void deleteTestObject();

    @GetMapping("/")
    @ResponseInfo(status = HttpStatus.OK)
    void getTestObject();

    @RequestMapping(path = "/", method = RequestMethod.HEAD)
    @ResponseInfo(status = HttpStatus.OK)
    void headTestObject();

    @RequestMapping(path = "/", method = RequestMethod.OPTIONS)
    @ResponseInfo(status = HttpStatus.OK)
    void optionsTestObject();

    @PatchMapping("/")
    @ResponseInfo(status = HttpStatus.OK)
    void patchTestObject();

    @PostMapping("/")
    @ResponseInfo(status = HttpStatus.OK)
    void postTestObject();

    @PutMapping("/")
    @ResponseInfo(status = HttpStatus.OK)
    void putTestObject();

    @RequestMapping(path = "/", method = RequestMethod.TRACE)
    @ResponseInfo(status = HttpStatus.OK)
    void traceTestObject();
}
