package dev.hltech.pact.generation.domain.client.feign.sample;

import dev.hltech.pact.generation.domain.client.feign.ResponseInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;
import java.util.Set;

@FeignClient("SpecProvider")
public interface RequestHeadersFeignClient {

    @GetMapping(path = "/", headers = {"key1=val1", "key2=val2" })
    @ResponseInfo(status = HttpStatus.OK)
    void getTestObject(@RequestHeader(name = "key3", required = false) Long tipId,
                       @RequestHeader("key4") Set<String> ids,
                       @RequestHeader("key5") int[] heads,
                       @RequestHeader Map<String, String> headersMap,
                       @RequestHeader MultiValueMap<String, String> headersMultiValueMap,
                       @RequestHeader HttpHeaders httpHeaders);
}
