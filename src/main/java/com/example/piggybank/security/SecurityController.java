package com.example.piggybank.security;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityController {

    @RequestMapping("/login")
    public String login(){

        return "login";
    }
    @RequestMapping("/403")
    public String accessDined(){

        return "403";
    }
}
