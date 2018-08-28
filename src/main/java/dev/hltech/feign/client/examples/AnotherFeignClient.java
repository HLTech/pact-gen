package dev.hltech.feign.client.examples;

import dev.hltech.pact.generation.ResponseInfo;
import dev.hltech.pact.generation.model.Metadata;
import dev.hltech.pact.generation.model.Pact;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("AnotherProvider")
public interface AnotherFeignClient {

    @RequestMapping(path = "/another/object", method = RequestMethod.GET)
    @ResponseInfo(status = HttpStatus.OK)
    Metadata getAnotherObject(Pact pact);

    @RequestMapping(path = "/another/another/object", method = RequestMethod.DELETE)
    @ResponseInfo(status = HttpStatus.OK)
    void getAnotherAnotherObject();
}
