package com.jmanzanog.ailab.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${embedding.api.base-url}")
    private String embeddingApiBaseUrl;

    @Bean
    public WebClient embeddingWebClient() {
        var strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)
                )
                .build();

        return WebClient.builder()
                .baseUrl(embeddingApiBaseUrl)
                .exchangeStrategies(strategies)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}