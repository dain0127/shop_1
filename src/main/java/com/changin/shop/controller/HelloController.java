package com.changin.shop.controller;

import com.changin.shop.dto.UserDto;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@Controller
public class HelloController {

    @RequestMapping(value="/rest", method=RequestMethod.GET)
    public UserDto hello(){
        UserDto userDto = UserDto.builder().name("ㅁㅁㅁㅁ").age(10).build();

        userDto.setAge(33);
        userDto.setName("aaaa");

        return userDto;
    }

    @GetMapping(value = "/test")
    public String test(){
        return "order/order";
    }
}
