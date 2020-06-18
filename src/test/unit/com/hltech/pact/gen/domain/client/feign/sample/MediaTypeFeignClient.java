package com.hltech.pact.gen.domain.client.feign.sample;

import com.hltech.pact.gen.domain.client.feign.InteractionInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("SpecProvider")
public interface MediaTypeFeignClient {

    @PostMapping(path = "/", consumes = {
        MediaType.APPLICATION_JSON_UTF8_VALUE,
        MediaType.APPLICATION_PDF_VALUE}, produces = {
        MediaType.APPLICATION_PROBLEM_JSON_VALUE,
        MediaType.APPLICATION_PROBLEM_XML_VALUE
    })
    @InteractionInfo(responseStatus = HttpStatus.OK, emptyBodyExpected = true)
    void createTestObject();

    @RequestMapping(method = RequestMethod.POST, path = "/", consumes = {
        MediaType.APPLICATION_JSON_UTF8_VALUE,
        MediaType.APPLICATION_PDF_VALUE}, produces = {
        MediaType.APPLICATION_PROBLEM_JSON_VALUE,
        MediaType.APPLICATION_PROBLEM_XML_VALUE})
    @InteractionInfo(responseStatus = HttpStatus.OK, emptyBodyExpected = true)
    void createTestObject2();
}
