package com.egraika.retailrestful.controller;

import com.egraika.retailrestful.entity.Price;
import com.egraika.retailrestful.model.ProductResponse;
import com.egraika.retailrestful.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ProductResponse>> getProductById(@PathVariable String id) {
        return productService.getProductDetails(id)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProductPrice(@PathVariable String id, @RequestBody Price price) {
        productService.updateProductPrice(id, price);
        return ResponseEntity.noContent().build();
    }
}