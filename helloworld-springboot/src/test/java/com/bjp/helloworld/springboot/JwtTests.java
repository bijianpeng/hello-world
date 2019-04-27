package com.bjp.helloworld.springboot;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class JwtTests {
    String secret = "bijianpeng";

    @Test
    public void test() {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Map<String, Object> head = new HashMap<String, Object>();
        head.put("alg", "HS256");
        head.put("typ", "JWT");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 3);

        String token = JWT.create()
                .withHeader(head)//头部信息
                .withIssuer("SERVICE")//签名是由谁生成
                .withSubject("test jwt")//签名的主题
                .withAudience("APP")//签名的观众
                .withIssuedAt(new Date())//生成签名的时间
                .withExpiresAt(calendar.getTime())//签名过期的时间
                .withClaim("userName", "bjp")//载荷Payload
                .sign(algorithm);
        System.out.println("token=" + token);

        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT jwt = jwtVerifier.verify(token);
        System.out.println(jwt.getExpiresAt());
        System.out.println(jwt.getSubject());
    }
}
