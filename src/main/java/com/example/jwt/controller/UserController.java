package com.example.jwt.controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @RequestMapping(value = "/current",method = RequestMethod.GET)
    public String getUser(){
           return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
