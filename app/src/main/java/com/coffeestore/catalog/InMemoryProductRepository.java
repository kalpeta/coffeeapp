package com.coffeestore.catalog;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final List<Product> products = List.of(
            new Product(1L, "Espresso", "Strong, rich single shot", 300),
            new Product(2L, "Americano", "Espresso with hot water", 350),
            new Product(3L, "Latte", "Espresso with steamed milk", 450),
            new Product(4L, "Cappuccino", "Foamy, balanced classic", 450)
    );

    @Override
    public List<Product> findAll() {
        return products;
    }
}
