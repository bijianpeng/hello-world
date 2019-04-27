package com.bjp.helloworld.startertest.controller;

import com.bjp.helloworld.starter.autoconfiguration.MyBean;
import com.bjp.helloworld.starter.autoconfiguration.MyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author bijianpeng
 * @create 2019-04-27 22:57
 */
@RestController
public class IndexController {

    @Autowired
    private MyBean myConfig;

    @GetMapping
    public MyBean index() {
        return myConfig;
    }
}
