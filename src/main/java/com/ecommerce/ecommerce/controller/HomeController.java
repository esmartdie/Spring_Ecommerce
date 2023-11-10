package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.OrderDetail;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.service.ProductService;
import org.apache.coyote.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;


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
    public String getCart(Model model){

        model.addAttribute("cart", details);
        model.addAttribute("order", order);

        return "/user/cart";
    }

    @Autowired
    private ProductService productService;
    private final Logger LOG = LoggerFactory.getLogger(HomeController.class);
    List<OrderDetail> details = new ArrayList<OrderDetail>();
    Order order = new Order();

}
