package com.bjp.helloworld.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "helloService", targetNamespace = "http://service.helloworld.com")
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.ENCODED)
public interface HelloService {

    @WebMethod
    public String sayHello(@WebParam(name = "name") String name);
}
