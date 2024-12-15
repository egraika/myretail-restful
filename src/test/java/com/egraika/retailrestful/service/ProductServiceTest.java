package com.egraika.retailrestful.service;

import com.egraika.retailrestful.entity.Price;
import com.egraika.retailrestful.entity.Price.CurrentPrice;
import com.egraika.retailrestful.model.ProductResponse;
import com.egraika.retailrestful.model.RedskyResponse;
import com.egraika.retailrestful.repository.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ProductService.class})
@EnableCaching
class ProductServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private ProductService productService;

    private final String id = "13860428";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reset(restTemplate, priceRepository);
    }

    @Test
    void testGetPriceAsync_Success() throws ExecutionException, InterruptedException {
        Price mockPrice = new Price(id, new CurrentPrice(13.49, "USD"));

        when(priceRepository.count()).thenReturn(1L);
        when(priceRepository.findById(id)).thenReturn(Optional.of(mockPrice));

        CompletableFuture<Price> result = productService.getPriceAsync(id);
        assertEquals(mockPrice, result.get());

        verify(priceRepository, times(1)).findById(id);
    }

    @Test
    void testGetPriceAsync_NotFound() {
        when(priceRepository.count()).thenReturn(1L);
        when(priceRepository.findById(id)).thenReturn(Optional.empty());

        CompletableFuture<Price> result = productService.getPriceAsync(id);

        ExecutionException exception = assertThrows(ExecutionException.class, result::get);
        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("Price not found", exception.getCause().getMessage());

        verify(priceRepository, times(1)).findById(id);
    }

    @Test
    void testGetProductNameAsync_Success() throws ExecutionException, InterruptedException {
        RedskyResponse mockResponse = new RedskyResponse();
        RedskyResponse.RedskyData redskyData = new RedskyResponse.RedskyData();
        RedskyResponse.Product product = new RedskyResponse.Product();
        RedskyResponse.Item item = new RedskyResponse.Item();
        RedskyResponse.ProductDescription description = new RedskyResponse.ProductDescription();

        description.setTitle("The Big Lebowski (Blu-ray)");
        item.setProductDescription(description);
        product.setItem(item);
        redskyData.setProduct(product);
        mockResponse.setData(redskyData);

        when(restTemplate.getForObject(anyString(), eq(RedskyResponse.class))).thenReturn(mockResponse);

        CompletableFuture<String> result = productService.getProductNameAsync(id);
        assertEquals("The Big Lebowski (Blu-ray)", result.get());

        verify(restTemplate, times(1)).getForObject(anyString(), eq(RedskyResponse.class));
    }

    @Test
    void testGetProductNameAsync_Failure() {
        when(restTemplate.getForObject(anyString(), eq(RedskyResponse.class))).thenThrow(new RuntimeException("API error"));

        CompletableFuture<String> result = productService.getProductNameAsync(id);

        ExecutionException exception = assertThrows(ExecutionException.class, result::get);
        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("Error fetching product name", exception.getCause().getMessage());

        verify(restTemplate, times(1)).getForObject(anyString(), eq(RedskyResponse.class));
    }

    @Test
    void testGetProductDetails_Success() throws ExecutionException, InterruptedException {
        Price mockPrice = new Price(id, new CurrentPrice(13.49, "USD"));

        RedskyResponse mockResponse = new RedskyResponse();
        RedskyResponse.RedskyData redskyData = new RedskyResponse.RedskyData();
        RedskyResponse.Product product = new RedskyResponse.Product();
        RedskyResponse.Item item = new RedskyResponse.Item();
        RedskyResponse.ProductDescription description = new RedskyResponse.ProductDescription();

        description.setTitle("The Big Lebowski (Blu-ray)");
        item.setProductDescription(description);
        product.setItem(item);
        redskyData.setProduct(product);
        mockResponse.setData(redskyData);

        when(priceRepository.count()).thenReturn(1L);
        when(priceRepository.findById(id)).thenReturn(Optional.of(mockPrice));
        when(restTemplate.getForObject(anyString(), eq(RedskyResponse.class))).thenReturn(mockResponse);

        CompletableFuture<ProductResponse> result = productService.getProductDetails(id);
        ProductResponse productResponse = result.get();

        assertEquals(id, productResponse.getId());
        assertEquals("The Big Lebowski (Blu-ray)", productResponse.getName());
        assertEquals(13.49, productResponse.getCurrentPrice().getValue());
        assertEquals("USD", productResponse.getCurrentPrice().getCurrencyCode());

        verify(priceRepository, times(1)).findById(id);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(RedskyResponse.class));
    }
}
