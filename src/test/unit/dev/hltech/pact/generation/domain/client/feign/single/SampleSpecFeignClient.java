package dev.hltech.pact.generation.domain.client.feign.single;

import dev.hltech.pact.generation.domain.GenericResponseType;
import dev.hltech.pact.generation.domain.RequestType;
import dev.hltech.pact.generation.domain.ResponseType;
import dev.hltech.pact.generation.domain.TestParam;
import dev.hltech.pact.generation.domain.client.feign.ResponseInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient("SpecProvider")
public interface SampleSpecFeignClient {

    @DeleteMapping(path = "/test/{testId}/objects/1", headers = { "key1=val1", "key2=val2" })
    @ResponseInfo(status = HttpStatus.OK)
    @ResponseInfo(status = HttpStatus.BAD_GATEWAY, headers = {"key3=val3"})
    ResponseType deleteTestObject(@RequestHeader HttpHeaders headers, @PathVariable("testId") String id);

    @GetMapping(path = "/test/{testId}/objects/{anotherTestId}/item")
    @ResponseInfo(status = HttpStatus.OK, headers = {"key3=val3"})
    ResponseType getTestObject(@RequestHeader MultiValueMap<String, String> headers,
                               @PathVariable("testId") Long id,
                               @PathVariable("anotherTestId") Long anotherId);

    @RequestMapping(path = "/test/objects/3", method = RequestMethod.HEAD)
    @ResponseInfo(status = HttpStatus.OK)
    ResponseType headTestObject(@RequestHeader Map<String, String> headers);

    @RequestMapping(value = "/test/objects/4", method = RequestMethod.OPTIONS)
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
    ResponseType createTestObject(RequestType request,
                                  @RequestParam("parameters") List<TestParam> prms,
                                  @RequestHeader("id") Set<String> ids);

    @PutMapping(path = "/test/objects/6")
    @ResponseInfo(status = HttpStatus.OK, headers = {"key3=val3", "key4=val4"})
    ResponseType updateTestObject(@RequestHeader(name = "key1") Long tipId,
                                  @RequestBody RequestType request);

    @RequestMapping(path = "/test/objects/7", method = RequestMethod.TRACE)
    @ResponseInfo(status = HttpStatus.OK)
    GenericResponseType<TestParam> traceTestObject(
        @RequestParam("param") String param, @RequestHeader(name = "type") int[] headers);
}
