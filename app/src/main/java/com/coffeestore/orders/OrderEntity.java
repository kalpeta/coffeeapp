package com.coffeestore.orders;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="order_number", nullable=false, unique=true, length=40)
    private String orderNumber;

    @Column(name="created_at", nullable=false, columnDefinition="timestamptz")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();

    public Long getId() { return id; }
    public String getOrderNumber() { return orderNumber; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public List<OrderItemEntity> getItems() { return items; }

    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public void addItem(OrderItemEntity item) {
        item.setOrder(this);
        this.items.add(item);
    }
}
