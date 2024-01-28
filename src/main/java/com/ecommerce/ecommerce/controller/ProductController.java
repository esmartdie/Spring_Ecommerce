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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    @GetMapping("")
    public String show(Model model){
        model.addAttribute("products", IProductService.findAll());
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
        IProductService.save(product);

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
        Optional<Product> optionalProduct= IProductService.getProduct(id);
        product=optionalProduct.get();

        LOGGER.info("Searched product: {}", product);
        model.addAttribute("product", product);

        return "products/edit";
    }

    @PostMapping("/update")
    public String update(Product product, @RequestParam("img") MultipartFile file) throws IOException {
        Product p = new Product();
        p= IProductService.getProduct(product.getId()).get();
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
        IProductService.update(product);

        updateProductInventoryFinalExistence(product);
        return "redirect:/products";
    }

    private void updateProductInventoryFinalExistence(Product product){

        List<ProductInventory> productInventoryList = productInventoryService.findByProduct(product);

        if (!productInventoryList.isEmpty()) {

            productInventoryList.sort(Comparator.comparing(ProductInventory::getDate).reversed());

            ProductInventory latestInventory = productInventoryList.get(0);

            int finalExistence = latestInventory.getFinalQuantity();

            pI.setDate(logDate);
            pI.setOperationName("Admin update final quantity by GUI");
            pI.setInitialQuantity(finalExistence);
            pI.setFinalQuantity(product.getQuantity());
            pI.setOperationQuantity(pI.getFinalQuantity() - pI.getInitialQuantity());
            pI.setProduct(product);

            productInventoryService.save(pI);

        }

        pI = new ProductInventory();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        Product p = new Product();
        p= IProductService.getProduct(id).get();

        if(!p.getImage().equals("default.jpg")){
            upload.deleteImage(p.getImage());
        }

        deleteProductInventory(p);
        IProductService.delete(id);

        return "redirect:/products";
    }

    private void deleteProductInventory(Product product){

        List<ProductInventory> productInventoryList = productInventoryService.findByProduct(product);

        if (!productInventoryList.isEmpty()) {

            productInventoryList.sort(Comparator.comparing(ProductInventory::getDate).reversed());

            ProductInventory latestInventory = productInventoryList.get(0);

            int finalExistence = latestInventory.getFinalQuantity();

            pI.setDate(logDate);
            pI.setOperationName("Admin delete product by GUI");
            pI.setInitialQuantity(finalExistence);
            pI.setFinalQuantity(0);
            pI.setOperationQuantity(pI.getFinalQuantity() - pI.getInitialQuantity());
            pI.setProduct(product);

            productInventoryService.save(pI);

        }

        pI = new ProductInventory();
    }


    private ProductInventory pI = new ProductInventory();
    private Date logDate = new Date();
    private final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private IProductService IProductService;
    @Autowired
    private UploadFileService upload;
    @Autowired
    private IUserService userService;
    @Autowired
    private IProductInventoryService productInventoryService;
}
