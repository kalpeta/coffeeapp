package com.coffeestore.catalog;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductReadController {

    private final ProductJpaRepository repo;

    public ProductReadController(ProductJpaRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id", description = "Returns product details including current stock")
    public ResponseEntity<?> getById(@PathVariable long id) {
        Optional<ProductEntity> p = repo.findById(id);
        return p.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
