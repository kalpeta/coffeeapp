package com.coffeestore.orders;

import com.coffeestore.orders.api.CreateOrderResponse;
import com.coffeestore.orders.api.OrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersService service;

    public OrdersController(OrdersService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create order", description = "Creates an order and publishes OrderPlacedEvent after DB commit")
    public ResponseEntity<CreateOrderResponse> create(@RequestBody OrderRequest request) {
        String orderId = service.createOrder(request);
        return ResponseEntity.accepted().body(new CreateOrderResponse(orderId));
    }
}
