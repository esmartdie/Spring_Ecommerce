package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("")
    public String home(Model model){

        model.addAttribute("products", productService.findAll());

        return "/user/home";
    }

    @GetMapping("/producthome/{id}")
    public String productHome(@PathVariable Integer id, Model model){
        LOG.info("Product Id sent as argument {}", id);

        Product product = new Product();
        Optional<Product> optionalProduct = productService.getProduct(id);
        product = optionalProduct.get();

        model.addAttribute("product", product);

        return "/user/product_home.html";
    }



    @Autowired
    private ProductService productService;

    private final Logger LOG = LoggerFactory.getLogger(HomeController.class);

}
