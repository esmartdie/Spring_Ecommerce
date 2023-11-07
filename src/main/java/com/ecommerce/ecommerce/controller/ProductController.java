package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.service.ProductService;
import com.ecommerce.ecommerce.service.UploadFileService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public String save(Product product, @RequestParam("img") MultipartFile file) throws IOException {
        LOGGER.info("This is the object product {}", product);
        User u = new User(1,"","","","","","","");
        product.setUser(u);

        if(product.getId()==null){
            String imageName= upload.saveImage(file);
            product.setImage(imageName);
        }else{

        }


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
    public String update(Product product, @RequestParam("img") MultipartFile file) throws IOException {
        Product p = new Product();
        p=productService.getProduct(product.getId()).get();
        if(file.isEmpty()){
            product.setImage(p.getImage());
        }else{
            if(!p.getImage().equals("default.jpg")){
                upload.deleteImage(p.getImage());
            }

            String imageName = upload.saveImage(file);
            product.setImage(imageName);
        }
        product.setUser(p.getUser());
        productService.update(product);
        return "redirect:/products";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        Product p = new Product();
        p=productService.getProduct(id).get();

        if(!p.getImage().equals("default.jpg")){
            upload.deleteImage(p.getImage());
        }

        productService.delete(id);
        return "redirect:/products";
    }

    private final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;
    @Autowired
    private UploadFileService upload;
}
