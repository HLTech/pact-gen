package dev.hltech.feign.client.examples;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("AnotherProvider")
public interface AnotherFeignClient {

    Object getAnotherObject();

    Object getAnotherAnotherObject();
}
