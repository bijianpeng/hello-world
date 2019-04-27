package com.bjp.helloworld.mongodb;

import com.bjp.helloworld.mongodb.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void testFind1() {
//        List<Product> products = mongoTemplate.find(Query.query(Criteria.where("name").is("小米MIX3")), Product.class);
//        List<Product> products = mongoTemplate.find(Query.query(Criteria.where("name").regex("\\d$")), Product.class);
        Query query = Query.query(Criteria.where("price").gt(3000));
        query.with(new Sort(Sort.Direction.DESC, "price"));
        query.with(PageRequest.of(1, 1));
        List<Product> products = mongoTemplate.find(query, Product.class);
        products.stream().forEach(product -> {
            System.out.println(product);
        });
    }
}
