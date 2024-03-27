package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.ProductInventory;
import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.service.IProductInventoryService;
import com.ecommerce.ecommerce.service.IProductService;
import com.ecommerce.ecommerce.service.IUserService;
import com.ecommerce.ecommerce.service.UploadFileService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    @GetMapping("")
    public String show(Model model){
        model.addAttribute("products", ProductService.findAllActiveProducts());
        return "products/show";
    }

    @GetMapping("/create")
    public String create(){
        return "products/create";
    }

    @PostMapping("/save")
    public String save(Product product, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
        LOGGER.info("This is the object product {}", product);
        User u = userService.findById(Integer.parseInt(session.getAttribute("userId").toString())).get();
        product.setUser(u);

        if(product.getId()==null){
            String imageName= upload.saveImage(file);
            product.setImage(imageName);
        }
        ProductService.save(product);

        createProductInventoryLog(product);

        return "redirect:/products";
    }

    private void createProductInventoryLog(Product product) {
        ProductInventory pI = new ProductInventory();

        pI.setDate(logDate);
        pI.setOperationName("Product Available to Sale");
        pI.setInitialQuantity(0);
        pI.setOperationQuantity(product.getQuantity());
        pI.setFinalQuantity(pI.getInitialQuantity()+ pI.getOperationQuantity());
        pI.setProduct(product);

        productInventoryService.save(pI);

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Product product = new Product();
        Optional<Product> optionalProduct= ProductService.getProduct(id);
        product=optionalProduct.get();

        LOGGER.info("Searched product: {}", product);
        model.addAttribute("product", product);

        return "products/edit";
    }

    @PostMapping("/update")
    public String update(Product product, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
        Product p = new Product();
        p= ProductService.getProduct(product.getId()).get();
        product.setActive(p.getActive());
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

        if(product.getQuantity()==0 && !p.getActive()){
            session.setAttribute("activeProduct", product.getId());
            return "redirect:/products/inactive";
        }else if(product.getQuantity()>0 && !p.getActive()){
            product.setActive(true);
            ProductService.update(product);
            updateProductInventoryFinalExistence(product);
            return "redirect:/products";
        }

        ProductService.update(product);
        updateProductInventoryFinalExistence(product);
        return "redirect:/products";
    }

    private void updateProductInventoryFinalExistence(Product product){

        List<ProductInventory> productInventoryList = productInventoryService.findByProduct(product);

        if (!productInventoryList.isEmpty()) {

            productInventoryList.sort(Comparator.comparing(ProductInventory::getId).reversed());

            ProductInventory latestInventory = productInventoryList.get(0);

            int initialExistence = latestInventory.getFinalQuantity();

            pI.setDate(logDate);
            pI.setOperationName("Admin update final quantity by GUI");
            pI.setInitialQuantity(initialExistence);
            pI.setFinalQuantity(product.getQuantity());
            pI.setOperationQuantity(pI.getFinalQuantity() - pI.getInitialQuantity());
            pI.setProduct(product);

            productInventoryService.save(pI);

        }

        pI = new ProductInventory();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        Product product = ProductService.getProduct(id).orElse(null);

        if (product != null) {
            product.setActive(false);
            ProductService.update(product);
            //createDeletedProductInventoryLog(product);
        }

        return "redirect:/products";
    }
/*
    private void createDeletedProductInventoryLog(Product product){

        List<ProductInventory> productInventoryList = productInventoryService.findByProduct(product);

        if (!productInventoryList.isEmpty()) {

            productInventoryList.sort(Comparator.comparing(ProductInventory::getDate).reversed());

            ProductInventory latestInventory = productInventoryList.get(0);

            int finalExistence = latestInventory.getFinalQuantity();

            ProductInventory pI = new ProductInventory();

            pI.setDate(logDate);
            pI.setOperationName("Admin delete product by GUI");
            pI.setInitialQuantity(finalExistence);
            pI.setFinalQuantity(0);
            pI.setOperationQuantity(pI.getFinalQuantity() - pI.getInitialQuantity());
            pI.setProduct(product);

            productInventoryService.save(pI);

        }
    }

 */
    @GetMapping("/inactive")
    public String showInactive(Model model, HttpSession session){

        Integer activeProductId = (Integer) session.getAttribute("activeProduct");
        List<Product> inactiveProducts = ProductService.findAllInactiveProducts();
        model.addAttribute("products", ProductService.findAllInactiveProducts());
        String message = "To active the product, the quantity must be higher than 0";

        Map<Integer, String> messages = new HashMap<>();
        for(Product product : inactiveProducts) {
            if (product.getId().equals(activeProductId)) {
                messages.put(product.getId(), message);
            } else {
                messages.put(product.getId(), "");
            }
        }
        model.addAttribute("messages", messages);
        return "products/inactive";
    }

    @GetMapping("/active/{id}")
    public String active(@PathVariable Integer id, Model model){
        Product product = new Product();
        Optional<Product> optionalProduct= ProductService.getProduct(id);
        product=optionalProduct.get();

        if(product.getQuantity()>0){
            product.setActive(true);
            ProductService.update(product);
        }else{
            LOGGER.info("Searched product: {}", product);
            model.addAttribute("product", product);
            return "products/edit";
        }
        return "redirect:/products/inactive";
    }

    private ProductInventory pI = new ProductInventory();
    private Date logDate = new Date();
    private final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private IProductService ProductService;
    @Autowired
    private UploadFileService upload;
    @Autowired
    private IUserService userService;
    @Autowired
    private IProductInventoryService productInventoryService;
}
