package dev.hltech.pact.generation.domain.client.feign.sample;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("SpecProvider")
public interface SecondEmptyFeignClient {

}
