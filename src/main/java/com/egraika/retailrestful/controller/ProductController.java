package com.egraika.retailrestful.controller;

import com.egraika.retailrestful.entity.Price;
import com.egraika.retailrestful.model.ProductResponse;
import com.egraika.retailrestful.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ProductResponse>> getProductById(@PathVariable String id) {
        logger.info("Received request to fetch product details for id {}", id);
        return productService.getProductDetails(id)
                .thenApply(productResponse -> {
                    logger.info("Successfully fetched product details for id {}: {}", id, productResponse);
                    return ResponseEntity.ok(productResponse);
                })
                .exceptionally(ex -> {
                    logger.error("Error fetching product details for id {}: {}", id, ex.getMessage());
                    return ResponseEntity.notFound().build();
                });
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProductPrice(@PathVariable String id, @RequestBody Price price) {
        logger.info("Received request to update price for id {}: {}", id, price);
        try {
            productService.updateProductPrice(id, price);
            logger.info("Successfully updated price for id {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            logger.error("Error updating price for id {}: {}", id, ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}