package com.coffeestore.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByName(String name);
}
