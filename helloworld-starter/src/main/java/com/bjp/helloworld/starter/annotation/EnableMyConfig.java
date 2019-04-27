package com.bjp.helloworld.starter.annotation;

import com.bjp.helloworld.starter.autoconfiguration.MyAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author bijianpeng
 * @create 2019-04-27 22:36
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(MyAutoConfiguration.class)
public @interface EnableMyConfig {
}
