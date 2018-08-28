package dev.hltech.feign.client.examples;

import dev.hltech.pact.generation.ResponseInfo;
import dev.hltech.pact.generation.model.Metadata;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("SomeProvider")
public interface SomeFeignClient {

    @RequestMapping(path = "/some/object", method = RequestMethod.POST)
    @ResponseInfo(status = HttpStatus.OK)
    Metadata getSomeObject();
}
