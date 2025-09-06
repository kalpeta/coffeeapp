package com.coffeestore.orders.events;

import java.time.OffsetDateTime;
import java.util.List;

public final class OrderPlacedEvent {
    public record Line(long productId, int quantity) {}

    private final String orderId;
    private final List<Line> lines;
    private final OffsetDateTime occurredAt = OffsetDateTime.now();

    public OrderPlacedEvent(String orderId, List<Line> lines) {
        this.orderId = orderId;
        this.lines = List.copyOf(lines);
    }

    public String orderId() { return orderId; }
    public List<Line> lines() { return lines; }
    public OffsetDateTime occurredAt() { return occurredAt; }
}
