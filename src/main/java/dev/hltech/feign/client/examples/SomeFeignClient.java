package dev.hltech.feign.client.examples;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("SomeProvider")
public interface SomeFeignClient {

    Object getSomeObject();
}
