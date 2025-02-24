package com.example.admin.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard"; // 타임리프 템플릿
    }
}
