package dev.hltech.pact.generation.domain.client.feign.multiple;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("SpecProvider")
public interface SecondSampleSpecFeignClient {

}
