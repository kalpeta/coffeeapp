package com.coffeestore.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    boolean existsByName(String name);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
       UPDATE ProductEntity p
          SET p.stock = p.stock - :qty
        WHERE p.id = :productId
          AND p.stock >= :qty
       """)
    int decrementStock(long productId, int qty);
}
