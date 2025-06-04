/**
 * Abstract base service to encapsulate reusable HTTP logic using WebClient.
 * Provides generic methods for HTTP GET, POST, PUT, and PATCH requests with error handling,
 * timeout, and retry strategies. Extend this class to easily perform HTTP operations in child services.
 */
package com.jmanzanog.ailab.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
public abstract class AbstractWebClientService {
    protected final WebClient webClient;

    protected AbstractWebClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Executes a POST request.
     */
    protected <T, R> Mono<R> postRequest(T requestBody, Class<R> responseType) {
        return webClient.post()
                .uri("")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handle4xx)
                .onStatus(HttpStatusCode::is5xxServerError, this::handle5xx)
                .bodyToMono(responseType)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(2, Duration.ofMillis(500)).filter(throwable -> throwable instanceof RuntimeException))
                .doOnError(throwable -> log.error("HTTP error on POST {}: {}", "", throwable.getMessage()));
    }

    /**
     * Executes a GET request.
     */
    protected <R> Mono<R> getRequest(String uri, Class<R> responseType) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handle4xx)
                .onStatus(HttpStatusCode::is5xxServerError, this::handle5xx)
                .bodyToMono(responseType)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(2, Duration.ofMillis(500)).filter(throwable -> throwable instanceof RuntimeException))
                .doOnError(throwable -> log.error("HTTP error on GET {}: {}", uri, throwable.getMessage()));
    }

    /**
     * Executes a PUT request.
     */
    protected <T, R> Mono<R> putRequest(String uri, T requestBody, Class<R> responseType) {
        return webClient.put()
                .uri(uri)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handle4xx)
                .onStatus(HttpStatusCode::is5xxServerError, this::handle5xx)
                .bodyToMono(responseType)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(2, Duration.ofMillis(500)).filter(throwable -> throwable instanceof RuntimeException))
                .doOnError(throwable -> log.error("HTTP error on PUT {}: {}", uri, throwable.getMessage()));
    }

    /**
     * Executes a PATCH request.
     */
    protected <T, R> Mono<R> patchRequest(String uri, T requestBody, Class<R> responseType) {
        return webClient.patch()
                .uri(uri)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handle4xx)
                .onStatus(HttpStatusCode::is5xxServerError, this::handle5xx)
                .bodyToMono(responseType)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(2, Duration.ofMillis(500)).filter(throwable -> throwable instanceof RuntimeException))
                .doOnError(throwable -> log.error("HTTP error on PATCH {}: {}", uri, throwable.getMessage()));
    }

    protected Mono<Throwable> handle4xx(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(body -> {
                    String msg = String.format("Error 4xx: status=%d, body=%s", response.statusCode().value(), body);
                    log.warn(msg);
                    return Mono.error(new IllegalArgumentException(msg));
                });
    }

    protected Mono<Throwable> handle5xx(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(body -> {
                    String msg = String.format("Error 5xx: status=%d, body=%s", response.statusCode().value(), body);
                    log.error(msg);
                    return Mono.error(new IllegalStateException(msg));
                });
    }
}
