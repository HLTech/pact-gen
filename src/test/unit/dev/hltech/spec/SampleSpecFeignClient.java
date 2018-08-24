package dev.hltech.spec;

import dev.hltech.pact.generation.ResponseInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("SpecProvider")
public interface SampleSpecFeignClient {

    @DeleteMapping(path = "/test/objects/1", headers = { "key1=val1", "key2=val2" })
    @ResponseInfo
    Object deleteTestObject();

    @GetMapping(path = "/test/objects/2")
    @ResponseInfo
    Object getTestObject();

    @RequestMapping(path = "/test/objects/3", method = RequestMethod.HEAD)
    @ResponseInfo
    Object headTestObject();

    @RequestMapping(path = "/test/objects/4", method = RequestMethod.OPTIONS)
    @ResponseInfo
    Object optionsTestObject();

    @PatchMapping(path = "/test/objects/5")
    @ResponseInfo
    Object patchTestObject();

    @PostMapping(path = "/test/objects", headers = { "key1=val1", "key2=val2" })
    @ResponseInfo
    Object createTestObject();

    @PutMapping(path = "/test/objects/6")
    @ResponseInfo
    Object updateTestObject();

    @RequestMapping(path = "/test/objects/7", method = RequestMethod.TRACE)
    @ResponseInfo
    Object traceTestObject();
}
