package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/administrator")
public class AdministratorController {


    @GetMapping("")
    public String home(Model model){

        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        return "administrator/home";
    }
    @Autowired
    private ProductService productService;
}
