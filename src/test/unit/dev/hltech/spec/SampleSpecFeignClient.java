package dev.hltech.spec;

import dev.hltech.pact.generation.ResponseInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("SpecProvider")
public interface SampleSpecFeignClient {

    @DeleteMapping(path = "/test/objects/1", headers = { "key1=val1", "key2=val2" })
    @ResponseInfo(status = HttpStatus.OK)
    ResponseType deleteTestObject(@RequestHeader HttpHeaders headers);

    @GetMapping(path = "/test/objects/2")
    @ResponseInfo(status = HttpStatus.OK, headers = {"key3=val3"})
    ResponseType getTestObject(@RequestHeader MultiValueMap<String, String> headers);

    @RequestMapping(path = "/test/objects/3", method = RequestMethod.HEAD)
    @ResponseInfo(status = HttpStatus.OK)
    ResponseType headTestObject(@RequestHeader Map<String, String> headers);

    @RequestMapping(path = "/test/objects/4", method = RequestMethod.OPTIONS)
    @ResponseInfo(status = HttpStatus.OK)
    ResponseType optionsTestObject(
        @RequestHeader(required = false, name = "key4", defaultValue = "val4") String header);

    @PatchMapping(path = "/test/objects/5")
    @ResponseInfo(status = HttpStatus.ACCEPTED)
    void patchTestObject(
        @RequestParam(name = "longP", defaultValue = "abc") Long longParam,
        @RequestParam(name = "very_long_name", defaultValue = "def") String stringParam);

    @PostMapping(path = "/test/objects", headers = { "key1=val1", "key2=val2" })
    @ResponseInfo(status = HttpStatus.ACCEPTED)
    ResponseType createTestObject(RequestType request);

    @PutMapping(path = "/test/objects/6")
    @ResponseInfo(status = HttpStatus.OK, headers = {"key3=val3", "key4=val4"})
    ResponseType updateTestObject(@RequestHeader(name = "key1") Long tipId,
                                  @RequestBody RequestType request);

    @RequestMapping(path = "/test/objects/7", method = RequestMethod.TRACE)
    @ResponseInfo(status = HttpStatus.OK)
    ResponseType traceTestObject(@RequestParam String param);
}
