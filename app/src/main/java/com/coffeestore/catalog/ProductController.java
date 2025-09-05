package com.coffeestore.catalog;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
            summary = "List products",
            description = "Retrieve a paginated list of products. Use `page` and `size` query parameters to control paging."
    )
    public List<Product> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        List<Product> all = service.getAllProducts();

        int fromIndex = Math.min(page * size, all.size());
        int toIndex = Math.min(fromIndex + size, all.size());

        return all.subList(fromIndex, toIndex);
    }
}
