package com.egraika.retailrestful.service;

import com.egraika.retailrestful.entity.Price;
import com.egraika.retailrestful.model.ProductResponse;
import com.egraika.retailrestful.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.egraika.retailrestful.model.RedskyResponse;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PriceRepository priceRepository;

    @Value("${external.api.url}")
    private String externalApiUrl;

    @Value("${mongodb.user}")
    private String mongoDbUser;

    @Value("${mongodb.password}")
    private String mongoDbPassword;

    public CompletableFuture<Price> getPriceAsync(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return priceRepository.findById(id).orElseThrow(() -> new RuntimeException("Price not found"));
            } catch (RuntimeException e) {
                logger.error("Error fetching price for id {}: {}", id, e.getMessage());
                throw e;
            }
        });
    }

    public CompletableFuture<String> getProductNameAsync(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String externalUrl = externalApiUrl + id;
                // Serialize the response into RedskyResponse
                RedskyResponse response = restTemplate.getForObject(externalUrl, RedskyResponse.class);

                // Log the full response object
                logger.debug("Response from external API for id {}: {}", id, response);

                // Validate and extract the product name
                if (response == null || response.getData() == null ||
                        response.getData().getProduct() == null ||
                        response.getData().getProduct().getItem() == null ||
                        response.getData().getProduct().getItem().getProductDescription() == null) {
                    logger.error("Invalid response structure for id {}", id);
                    throw new RuntimeException("Invalid response structure");
                }

                String productName = response.getData().getProduct().getItem().getProductDescription().getTitle();

                // Log the extracted product name
                logger.debug("Extracted product name for id {}: {}", id, productName);

                return productName;
            } catch (Exception e) {
                logger.error("Error fetching product name for id {}: {}", id, e.getMessage());
                throw new RuntimeException("Error fetching product name", e);
            }
        });
    }

    public CompletableFuture<ProductResponse> getProductDetails(String id) {
        CompletableFuture<Price> priceFuture = getPriceAsync(id);
        CompletableFuture<String> nameFuture = getProductNameAsync(id);

        return priceFuture.thenCombine(nameFuture, (price, name) -> {
            try {
                ProductResponse response = new ProductResponse();
                response.setId(price.getId());
                response.setName(name);
                response.setCurrentPrice(new ProductResponse.CurrentPrice(
                        price.getCurrentPrice().getValue(),
                        price.getCurrentPrice().getCurrencyCode()
                ));
                return response;
            } catch (Exception e) {
                logger.error("Error combining product details for id {}: {}", id, e.getMessage());
                throw new RuntimeException("Error combining product details", e);
            }
        });
    }

    public void updateProductPrice(String id, Price price) {
        try {
            priceRepository.save(price);
        } catch (Exception e) {
            logger.error("Error updating price for id {}: {}", id, e.getMessage());
            throw new RuntimeException("Error updating price", e);
        }
    }
}