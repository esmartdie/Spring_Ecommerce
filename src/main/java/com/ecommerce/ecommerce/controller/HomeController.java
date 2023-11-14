package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.OrderDetail;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.service.IOrderDetailService;
import com.ecommerce.ecommerce.service.IOrderService;
import com.ecommerce.ecommerce.service.IProductService;
import com.ecommerce.ecommerce.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("")
    public String home(Model model, HttpSession session){

        LOG.info("User session: {}", session.getAttribute("userId"));
        model.addAttribute("products", productService.findAll());

        model.addAttribute("session", session.getAttribute("userId"));

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

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer quantity, Model model){
        OrderDetail orderDetail = new OrderDetail();
        Product product = new Product();
        double totalSum = 0;
        Optional<Product> optionalProduct = productService.getProduct(id);

        LOG.info("Product added: {}", optionalProduct.get());
        LOG.info("Quantity: {}", quantity);

        product=optionalProduct.get();
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(product.getPrice());
        orderDetail.setName(product.getName());
        orderDetail.setTotal(product.getPrice()*quantity);
        orderDetail.setProduct(product);

        Integer productId = product.getId();
        boolean inserted = details.stream().anyMatch(p ->p.getProduct().getId()==productId);

        if(!inserted){
            details.add(orderDetail);
        }



        totalSum = details.stream().mapToDouble(dt->dt.getTotal()).sum();

        order.setTotal(totalSum);

        model.addAttribute("cart", details);
        model.addAttribute("order", order);

        return "user/cart";
    }

    @GetMapping("/delete/cart/{id}")
    public String removeProductCart(@PathVariable Integer id, Model model){
        List <OrderDetail> newOrders = new ArrayList<OrderDetail>();

        for(OrderDetail orderDetail: details){
            if(orderDetail.getProduct().getId() != id){
                newOrders.add(orderDetail);
            }
        }

        details = newOrders;

        double totalSum = 0;
        totalSum = details.stream().mapToDouble(dt->dt.getTotal()).sum();

        order.setTotal(totalSum);

        model.addAttribute("cart", details);
        model.addAttribute("order", order);

        return "user/cart";
    }

    @GetMapping("/getCart")
    public String getCart(Model model, HttpSession session){

        model.addAttribute("cart", details);
        model.addAttribute("order", order);

        model.addAttribute("session", session.getAttribute("userId"));

        return "/user/cart";
    }

    @GetMapping("/order")
    public String order(Model model, HttpSession session){

        User user = userService.findById(Integer.parseInt(session.getAttribute("userId").toString())).get();

        model.addAttribute("cart", details);
        model.addAttribute("order", order);
        model.addAttribute("user", user);

        return "user/ordersummary";
    }

    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession session){

        Date creationDate = new Date();
        order.setCreationDate(creationDate);
        order.setNumber(orderService.generateOrderIdentification());

        User user = userService.findById(Integer.parseInt(session.getAttribute("userId").toString())).get();
        order.setUser(user);

        orderService.save(order);

        for (OrderDetail od:details){
            od.setOrder(order);
            orderDetailService.save(od);
        }

        order = new Order();
        details.clear();

        return "redirect:/";
    }

    @PostMapping("/search")
    public String searchProduct(@RequestParam String productName, Model model){
        LOG.info("Product name: {}", productName);

        List<Product> products = productService.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(productName.toLowerCase())).collect(Collectors.toList());

        model.addAttribute("products", products);
        return"user/home";
    }

    @Autowired
    private IProductService productService;
    private final Logger LOG = LoggerFactory.getLogger(HomeController.class);
    List<OrderDetail> details = new ArrayList<OrderDetail>();

    Order order = new Order();
    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderDetailService orderDetailService;

}
