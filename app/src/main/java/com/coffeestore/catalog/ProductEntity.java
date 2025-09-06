package com.coffeestore.catalog;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=100)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name="price_cents", nullable=false)
    private Integer priceCents;

    @Column(name="stock", nullable=false)
    private Integer stock = 0;

    @Column(name="created_at", nullable=false, columnDefinition="timestamptz")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public ProductEntity() {}
    public ProductEntity(String name, String description, Integer priceCents, Integer stock) {
        this.name = name;
        this.description = description;
        this.priceCents = priceCents;
        this.stock = stock;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Integer getPriceCents() { return priceCents; }
    public Integer getStock() { return stock; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPriceCents(Integer priceCents) { this.priceCents = priceCents; }
    public void setStock(Integer stock) { this.stock = stock; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
