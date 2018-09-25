package dev.hltech.pact.generation.domain.client.feign.sample;

import dev.hltech.pact.generation.domain.TestParam;
import dev.hltech.pact.generation.domain.client.feign.ResponseInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("SpecProvider")
public interface RequestParamFeignClient {

    @GetMapping(path = "/")
    @ResponseInfo(status = HttpStatus.OK)
    void getTestObject(@RequestParam(name = "longP", defaultValue = "123") Long longParam,
                       @RequestParam("parameter") String param,
                       @RequestParam("params") List<TestParam> prms);
}
