package com.bjp.helloworld.starter.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author bijianpeng
 * @create 2019-04-27 22:31
 */
@ConfigurationProperties(prefix = "my-config")
public class MyConfig {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
