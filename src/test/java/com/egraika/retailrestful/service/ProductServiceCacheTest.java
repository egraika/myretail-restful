package com.egraika.retailrestful.service;

import com.egraika.retailrestful.model.RedskyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.ActiveProfiles;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EnableWireMock(
        @ConfigureWireMock(
                baseUrlProperties = { "external.api.url" },
                portProperties = "wiremock.server.port"
        )
)
@ActiveProfiles("test")
@EnableCaching
class ProductServiceCacheTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private CacheManager cacheManager;

    private final String productId = "12345";

    @BeforeEach
    void setUp() {
        // Set up WireMock stub
        stubFor(get(urlPathMatching("/redsky_aggregations/v1/redsky/case_study_v1"))
                .withQueryParam("key", equalTo("testkey"))
                .withQueryParam("tcin", equalTo(productId))
                .willReturn(okJson("{ \"data\": { \"product\": { \"item\": { \"product_description\": { \"title\": \"Mocked Product Name\" }}}}}")));
    }

    @Test
    void testWireMockCalledOnceWithCaching() {
        // First call - should hit the WireMock server and return a response
        RedskyResponse response1 = productService.RedSkyApiCall(productId);
        assertNotNull(response1);
        assertEquals("Mocked Product Name",
                response1.getData().getProduct().getItem().getProductDescription().getTitle());

        // Second call - should be served from cache, returning the same response as first call
        RedskyResponse response2 = productService.RedSkyApiCall(productId);
        assertNotNull(response2);
        assertEquals("Mocked Product Name",
                response2.getData().getProduct().getItem().getProductDescription().getTitle());

        // Verify WireMock was only called once for this endpoint
        verify(1, getRequestedFor(urlPathEqualTo("/redsky_aggregations/v1/redsky/case_study_v1")));
    }
}