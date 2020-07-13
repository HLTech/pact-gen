package com.hltech.pact.gen.domain.client.feign.sample;

import com.hltech.pact.gen.domain.TestParam;
import com.hltech.pact.gen.domain.client.feign.InteractionInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("SpecProvider")
public interface RequestParamFeignClient {

    @GetMapping(path = "?aaa=first&bbb=111")
    @InteractionInfo(responseStatus = HttpStatus.OK)
    void getTestObject(@RequestParam(name = "longP", defaultValue = "123") Long longParam,
                       @RequestParam("parameter") String param,
                       @RequestParam("params") List<TestParam> prms);
}
