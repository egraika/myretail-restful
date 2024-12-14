package com.egraika.retailrestful.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedskyResponse {

    @JsonProperty("data")
    private RedskyData data; // Rename the nested class to RedskyData

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RedskyData { // Updated class name
        @JsonProperty("product")
        private Product product;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Product {
        @JsonProperty("tcin")
        private String tcin;

        @JsonProperty("item")
        private Item item;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        @JsonProperty("product_description")
        private ProductDescription productDescription;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductDescription {
        @JsonProperty("title")
        private String title;

        @JsonProperty("downstream_description")
        private String downstreamDescription;
    }
}