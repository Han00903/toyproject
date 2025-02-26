package com.example.admin.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {

    @GetMapping("/signin")
    public String sign(){

        return"users/signin";

    }

    @GetMapping("/signup")
    public String signup(){

        return "users/signup";

    }
}
