package com.egraika.retailrestful.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "price")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Price {

    @Id
    private String id; // MongoDB _id field mapped to the product ID
    private CurrentPrice currentPrice; // Nested object for price details

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrentPrice {
        private double value; // Price value
        private String currencyCode; // Currency code (e.g., USD)
    }
}