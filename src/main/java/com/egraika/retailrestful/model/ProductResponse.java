package com.egraika.retailrestful.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private String id; // Product ID
    private String name; // Product name fetched from external API
    private CurrentPrice currentPrice; // Current price details

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrentPrice {
        private double value; // Price value
        private String currencyCode; // Currency code, e.g., USD
    }
}