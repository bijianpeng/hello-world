package com.bjp.helloworld.starter.autoconfiguration;

/**
 * @Author bijianpeng
 * @create 2019-04-27 23:10
 */
public class MyBean {

    private String name;
    private int age;

    public MyBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

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
