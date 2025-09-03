package com.coffeestore.catalog;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();
}
