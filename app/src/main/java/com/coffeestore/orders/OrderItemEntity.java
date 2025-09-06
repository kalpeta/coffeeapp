package com.coffeestore.orders;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="order_id", nullable=false)
    private OrderEntity order;

    @Column(name="product_id", nullable=false)
    private Long productId;

    @Column(name="quantity", nullable=false)
    private Integer quantity;

    public Long getId() { return id; }
    public OrderEntity getOrder() { return order; }
    public Long getProductId() { return productId; }
    public Integer getQuantity() { return quantity; }

    public void setOrder(OrderEntity order) { this.order = order; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
