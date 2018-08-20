package dev.hltech.spec;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("SpecProvider")
public interface SampleSpecFeignClient {

    @RequestMapping(path = "/test/objects", method = RequestMethod.GET)
    Object getTestObject();

    @RequestMapping(path = "/test/objects", method = RequestMethod.DELETE)
    Object deleteTestObject();
}
