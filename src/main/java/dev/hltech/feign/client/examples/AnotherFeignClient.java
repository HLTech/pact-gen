package dev.hltech.feign.client.examples;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("AnotherProvider")
public interface AnotherFeignClient {

    @RequestMapping(path = "/another/object", method = RequestMethod.GET)
    Object getAnotherObject();

    @RequestMapping(path = "/another/another/object", method = RequestMethod.DELETE)
    Object getAnotherAnotherObject();
}
