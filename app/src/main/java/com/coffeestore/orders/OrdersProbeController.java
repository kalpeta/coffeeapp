package com.coffeestore.orders;

import com.coffeestore.orders.client.InventoryClient;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrdersProbeController {

    private final InventoryClient inventoryClient;

    public OrdersProbeController(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    @GetMapping(value = "/probe-inventory", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Probe inventory stub via Resilience4j",
            description = "Calls WireMock at :8089/inventory/check with timeout+retry+circuit breaker")
    public ResponseEntity<String> probe(@RequestParam(defaultValue = "1") long productId) throws Exception {
        // Block here only for simple demo; our client itself is async and time-limited.
        String body = inventoryClient.check(productId).toCompletableFuture().get();
        return ResponseEntity.ok(body);
    }
}
