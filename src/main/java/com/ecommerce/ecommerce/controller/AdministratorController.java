package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.service.IOrderService;
import com.ecommerce.ecommerce.service.IProductService;
import com.ecommerce.ecommerce.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/administrator")
public class AdministratorController {


    @GetMapping("")
    public String home(Model model){

        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        return "administrator/home";
    }

    @GetMapping("/users")
    public String users(Model model){

        model.addAttribute("users", userService.findAll());
        return "administrator/users";
    }

    @GetMapping("/orders")
    public String orders(Model model){

        /*
        List<Order> orders = orderService.findAll();

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");


        List <Order> ordersFormatted = orders.stream()
                        .map( order -> {
                            Order orderCopy = new Order();
                            orderCopy.setId(order.getId());
                            orderCopy.setNumber(order.getNumber());
                            orderCopy.setCreationDate(new Date(dayFormat.format(order.getCreationDate())));
                            orderCopy.setReceptionDate(order.getReceptionDate());
                            orderCopy.setTotal(order.getTotal());
                            return orderCopy;
                        })
                        .collect(Collectors.toList());

         finally decide to manage date format with thymeleaf

         */

        model.addAttribute("orders", orderService.findAll());

        return "administrator/orders";
    }

    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable Integer id){

        LOGG.info("Order id: {}", id);

        Order order = orderService.findById(id).get();

        model.addAttribute("orderdetails", order.getDetails());

        return "administrator/shoppingdetails";
    }



    private final Logger LOGG = LoggerFactory.getLogger(AdministratorController.class);

    @Autowired
    private IProductService productService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;
}
