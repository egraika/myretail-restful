package com.egraika.retailrestful.controller;

import com.egraika.retailrestful.entity.Price;
import com.egraika.retailrestful.model.ProductResponse;
import com.egraika.retailrestful.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get product details by ID", description = "Fetches product details including name and price using the product ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched product details",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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

    @Operation(summary = "Update product price", description = "Updates the price for the specified product ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully updated the price"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProductPrice(
            @PathVariable String id,
            @RequestBody(description = "Price details to update", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Price.class)))
            Price price) {
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