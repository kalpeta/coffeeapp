package com.coffeestore.catalog.inventory;

import com.coffeestore.orders.events.OrderPlacedEvent;
import com.coffeestore.catalog.ProductJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class InventoryEventHandler {
    private static final Logger log = LoggerFactory.getLogger(InventoryEventHandler.class);

    private final ProductJpaRepository products;

    public InventoryEventHandler(ProductJpaRepository products) {
        this.products = products;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW) // <-- open a transaction for the update
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(OrderPlacedEvent event) {
        log.info("Handling OrderPlacedEvent AFTER_COMMIT orderId={} lines={}", event.orderId(), event.lines().size());
        event.lines().forEach(line -> {
            int updated = products.decrementStock(line.productId(), line.quantity());
            if (updated == 0) {
                log.warn("Insufficient stock for productId={} qty={}", line.productId(), line.quantity());
            } else {
                log.info("Stock decremented for productId={} by {}", line.productId(), line.quantity());
            }
        });
    }
}
