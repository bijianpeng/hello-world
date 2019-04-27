package com.bjp.helloworld.springboot.cors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController
//@RequestMapping("/user")
public class UserController {

    @RequestMapping("/login")
    public Response login(@RequestBody User user) {
        System.out.println("name=" + user.getUsername());
        return new Response(20000).data("token", "admin");
    }

    @RequestMapping("/info")
    public Response info(String token) {
        List<String> list = new ArrayList<>();
        list.add("admin");
        list.add("editor");
        return new Response(20000)
                .data("roles", list)
                .data("name", "bjp")
                .data("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

    class Response implements Serializable {
        private int code;
        private Map<String, Object> data;

        public Response(int code) {
            this.code = code;
            this.data = new HashMap<String, Object>();
        }

        public Response data(String key, Object value) {
            data.put(key, value);
            return this;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public Map<String, Object> getData() {
            return data;
        }

        public void setData(Map<String, Object> data) {
            this.data = data;
        }
    }
}
