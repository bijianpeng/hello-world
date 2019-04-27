package com.bjp.helloworld.mongodb.dao;

import com.bjp.helloworld.mongodb.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

public interface ProductDao extends MongoRepository<Product, String>, QuerydslPredicateExecutor<Product> {

    public Product findByName(String name);

    public List<Product> findByCategory(String category);

    public List<Product> findByPriceGreaterThan(double price);

    public List<Product> findByAuthorNotNull();

    public List<Product> findByTagsIn(String[] tags);
}
