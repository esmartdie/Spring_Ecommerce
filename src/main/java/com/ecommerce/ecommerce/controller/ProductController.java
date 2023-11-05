package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.service.ProductService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    @GetMapping("")
    public String show(Model model){
        model.addAttribute("products", productService.findAll());
        return "products/show";
    }

    @GetMapping("/create")
    public String create(){
        return "products/create";
    }

    @PostMapping("/save")
    public String save(Product product){
        LOGGER.info("This is the object product {}", product);

        User u = new User(1,"","","","","","","");
        product.setUser(u);
        productService.save(product);
        return "redirect:/products";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Product product = new Product();
        Optional<Product> optionalProduct=productService.getProduct(id);
        product=optionalProduct.get();

        LOGGER.info("Searched product: {}", product);
        model.addAttribute("product", product);

        return "products/edit";
    }

    @PostMapping("/update")
    public String update(Product product){
        productService.update(product);
        return "redirect:/products";
    }

    private final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;
}
