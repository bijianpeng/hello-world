package com.bjp.helloworld.cxf;


import javax.jws.WebService;

@WebService(endpointInterface = "com.bjp.helloworld.cxf.HelloService")
public class HelloServiceImpl implements HelloService {
    public String sayHello(String name) {
        return "Hello," + name;
    }
}
