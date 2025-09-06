package com.coffeestore.orders.api;

import java.util.List;

public record OrderRequest(List<Item> items) {
    public record Item(Long productId, int quantity) {}
}
