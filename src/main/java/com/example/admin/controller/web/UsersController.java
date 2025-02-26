package com.example.admin.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {

    @GetMapping("/signin")
    public String signIn(){

        return"users/signIn";

    }

    @GetMapping("/signup")
    public String signUp(){

        return "users/signUp";

    }
}
