package com.hltech.pact.gen.domain.client.feign.sample;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("SpecProvider")
public interface SecondEmptyFeignClient {

}
