package dev.hltech.feign.client.examples;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("SomeProvider")
public interface SomeFeignClient {

    @RequestMapping(path = "/some/object", method = RequestMethod.POST)
    Object getSomeObject();
}
