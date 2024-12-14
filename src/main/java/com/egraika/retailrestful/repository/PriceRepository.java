package com.egraika.retailrestful.repository;

import com.egraika.retailrestful.entity.Price;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends MongoRepository<Price, String> {
    // Additional query methods can be defined here if needed
}