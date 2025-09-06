package com.coffeestore.orders;

import com.coffeestore.orders.api.OrderRequest;
import com.coffeestore.orders.events.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrdersService {
    private static final Logger log = LoggerFactory.getLogger(OrdersService.class);

    private final OrderJpaRepository orders;
    private final ApplicationEventPublisher events;

    public OrdersService(OrderJpaRepository orders, ApplicationEventPublisher events) {
        this.orders = orders;
        this.events = events;
    }

    @Transactional
    public String createOrder(OrderRequest request) {
        // persist order + items
        var order = new OrderEntity();
        String orderNumber = UUID.randomUUID().toString();
        order.setOrderNumber(orderNumber);

        request.items().forEach(i -> {
            var item = new OrderItemEntity();
            item.setProductId(i.productId());
            item.setQuantity(i.quantity());
            order.addItem(item);
        });

        orders.save(order); // within transaction

        // publish domain event (handler listens AFTER_COMMIT)
        var lines = request.items().stream()
                .map(i -> new OrderPlacedEvent.Line(i.productId(), i.quantity()))
                .toList();
        var event = new OrderPlacedEvent(orderNumber, lines);

        log.info("Publishing OrderPlacedEvent orderId={} lines={}", orderNumber, lines.size());
        events.publishEvent(event);

        return orderNumber;
    }
}
