package com.bjp.helloworld.mongodb;

import com.bjp.helloworld.mongodb.dao.ProductDao;
import com.bjp.helloworld.mongodb.entity.Product;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    public void testSave() {
        Product product = new Product();
        product.setName("IPhone8");
        product.setCategory("手机");
        product.setPrice(2999);
        productDao.save(product);
    }

    @Test
    public void testSave2() {
        Product product = new Product();
        product.setName("JAVA编程思想");
        product.setCategory("图书");
        product.setPrice(99);
        product.setTags(new String[]{"JAVA", "计算机", "编程"});
        Product.Author author = product.getAuthor();
        author.setName("Bruce Eckel");
        author.setFrom("美国");
        product.setAuthor(author);
        productDao.save(product);
    }

    @Test
    public void testUpdate() {
        Product product = productDao.findByName("小米MIX3");
        product.setId(new ObjectId("5c3d50ba09315220c0d901dc"));
        product.setTags(new String[]{"手机", "发烧"});
        productDao.save(product);
    }

    @Test
    public void testDelete() {
        productDao.deleteById("5c3d50ba09315220c0d901dc");
    }

    @Test
    public void testFindByName() {
        List<Product> productList = productDao.findByAuthorNotNull();
        for (Product product : productList) {
            System.out.println(product);
        }
    }

    @Test
    public void testFindByTagsIn() {
        List<Product> productList = productDao.findByTagsIn(new String[]{"JAVA"});
        for (Product product : productList) {
            System.out.println(product);
        }
    }
}
