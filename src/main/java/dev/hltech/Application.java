package dev.hltech;

import dev.hltech.pact.generation.FeignClientsFinder;
import dev.hltech.pact.generation.PactFactory;

/**
 * Main executable
 */
public class Application {

    public static void main(String[] args) {
        final FeignClientsFinder finder = new FeignClientsFinder();
        final PactFactory pactFactory = new PactFactory();
        finder.findFeignClients("dev.hltech.feign").stream()
            .map(pactFactory::create)
            .forEach(System.out::println);
    }
}
