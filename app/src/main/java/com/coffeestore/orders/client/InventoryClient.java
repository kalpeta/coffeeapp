package com.coffeestore.orders.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Component
public class InventoryClient {
    private static final Logger log = LoggerFactory.getLogger(InventoryClient.class);
    private final WebClient inventoryWebClient;

    public InventoryClient(WebClient inventoryWebClient) {
        this.inventoryWebClient = inventoryWebClient;
    }

    /**
     * Calls the stubbed inventory service:
     * GET http://localhost:8089/inventory/check?productId=...
     *
     * Wrapped with:
     * - @TimeLimiter(name="inventory") -> fails if it takes > configured timeout (1s)
     * - @Retry(name="inventory")       -> retries quickly before giving up
     * - @CircuitBreaker(name="inventory", fallbackMethod="fallback")
     *
     * We return CompletionStage for @TimeLimiter to work at method level.
     */
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallback")
    public CompletionStage<String> check(long productId) {
        log.info("Calling inventory /check for productId={}", productId);
        return inventoryWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/inventory/check")
                        .queryParam("productId", productId)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .toFuture(); // CompletionStage required by @TimeLimiter
    }

    // Fallback signature must match the original return type and accept the Throwable
    private CompletionStage<String> fallback(long productId, Throwable t) {
        log.warn("Inventory fallback for productId={}, cause={}", productId,
                t.getClass().getSimpleName() + ": " + t.getMessage());
        String body = """
                { "status": "FALLBACK", "reason": "%s" }
                """.formatted(t.getClass().getSimpleName());
        return CompletableFuture.completedFuture(body);
    }
}
