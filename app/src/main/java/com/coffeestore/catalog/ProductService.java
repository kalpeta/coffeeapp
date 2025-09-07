package com.coffeestore.catalog;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductJpaRepository repo;
    private final Timer productLookupTimer;

    public ProductService(ProductJpaRepository repo, MeterRegistry registry) {
        this.repo = repo;
        this.productLookupTimer = Timer.builder("catalog.product.lookup")
                .description("Time taken to list products (DB-backed) for the catalog endpoint")
                .publishPercentileHistogram()
                .register(registry);
    }

    /** Called by GET /products; wraps DB fetch in a Micrometer Timer. */
    public List<Product> getAllProducts() {
        return productLookupTimer.record(() -> repo.findAll().stream()
                .map(e -> new Product(
                        e.getId(),
                        e.getName(),
                        e.getDescription(),
                        e.getPriceCents()
                ))
                .toList()
        );
    }
}
