package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.OrderStatus;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.ProductInventory;
import com.ecommerce.ecommerce.service.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/administrator")
public class AdministratorController {

    private final Logger logger = LoggerFactory.getLogger(AdministratorController.class);

    private final IProductService productService;
    private final IUserService userService;
    private final IOrderService orderService;
    private final IOrderStatusService orderStatusService;
    private final IProductInventoryService productInventoryService;

    @Autowired
    public AdministratorController(IProductService productService, IUserService userService,
                                   IOrderService orderService, IOrderStatusService orderStatusService,
                                   IProductInventoryService productInventoryService) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
        this.orderStatusService = orderStatusService;
        this.productInventoryService = productInventoryService;
    }

    @GetMapping("")
    public String home(@RequestParam(required = false) String productName, Model model) {
        List<Product> products = productService.findAllActiveProducts();
        if (productName != null && !productName.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getName().toLowerCase().contains(productName.toLowerCase()))
                    .collect(Collectors.toList());
        }
        model.addAttribute("products", products);
        return "administrator/home";
    }

    @GetMapping("/users")
    public String users(Model model){
        model.addAttribute("users", userService.findAll());
        return "administrator/users";
    }

    @GetMapping("/producthomeadmin/{id}")
    public String productHome(@PathVariable Integer id, Model model) {
        logger.info("Product Id sent as argument {}", id);
        Product product = productService.getProduct(id).orElseThrow(() -> new RuntimeException("Product not found"));
        ProductInventory latestInventory = productInventoryService.findLastProduct(product);
        model.addAttribute("inventory", latestInventory);
        model.addAttribute("product", product);
        return "administrator/product_home";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "administrator/orders";
    }

    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable Integer id) {
        logger.info("Order id: {}", id);
        Order order = orderService.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        List<OrderStatus> orderStatusList = orderStatusService.findAll();
        model.addAttribute("orderStatusList", orderStatusList);
        model.addAttribute("order", order);
        model.addAttribute("orderdetails", order.getDetails());
        return "administrator/shoppingdetails";
    }

    @PostMapping("/updateOrderStatus")
    public String updateOrderStatus(@RequestParam("orderId") Integer orderId,
                                    @RequestParam("newStatus") String newStatus) {
        Order order = orderService.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        OrderStatus status = orderStatusService.findByStatus(newStatus);
        order.setStatus(status);
        orderService.save(order);
        return "redirect:/administrator/orders";
    }

    @PostMapping("/search")
    public String searchProduct(@RequestParam String productName, Model model) {
        logger.info("Product name: {}", productName);
        List<Product> products = productService.findAllActiveProducts().stream()
                .filter(p -> p.getName().toLowerCase().contains(productName.toLowerCase()))
                .collect(Collectors.toList());
        model.addAttribute("products", products);
        return "administrator/home";
    }

}
