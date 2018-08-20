package dev.hltech;

import dev.hltech.pact.generation.FeignClientsFinder;

/**
 * Main executable
 */
public class Application {

    public static void main(String[] args) {
        final FeignClientsFinder finder = new FeignClientsFinder();
        finder.findFeignClients("dev.hltech.feign")
            .forEach(System.out::println);
    }
}
