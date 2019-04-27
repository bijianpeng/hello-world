package com.bjp.helloworld.starter.autoconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @Author bijianpeng
 * @create 2019-04-27 22:19
 */
@Configuration
@EnableConfigurationProperties(MyConfig.class)
public class MyAutoConfiguration {

    @Autowired
    private MyConfig myConfig;

    @Bean
    @ConditionalOnMissingBean
    public MyBean myBean() {
        return new MyBean(myConfig.getName(), myConfig.getAge());
    }
}
