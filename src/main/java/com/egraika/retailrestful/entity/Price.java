package com.egraika.retailrestful.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Document(collection = "price")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Price {

    @Id
    @NotNull(message = "ID cannot be null")
    private String id;

    @NotNull(message = "Current price cannot be null")
    @Valid
    private CurrentPrice currentPrice;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrentPrice {
        @NotNull(message = "Value cannot be null")
        private Double value;

        @NotNull(message = "Currency code cannot be null")
        private String currencyCode;
    }
}