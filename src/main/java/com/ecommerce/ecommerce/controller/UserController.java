package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/registry")
    public String create(){

        return "user/registry";
    }

    @PostMapping("/save")
    public String save(User user){

        LOG.info("User registry: {}", user);
        user.setUserRol("USER");
        userService.save(user);
        return "redirect:/";
    }


    @Autowired
    private IUserService userService;

    private final Logger LOG= LoggerFactory.getLogger(UserController.class);
}
