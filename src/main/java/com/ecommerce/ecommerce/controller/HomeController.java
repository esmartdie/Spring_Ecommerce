package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.*;
import com.ecommerce.ecommerce.repository.IOrderStatusRepository;
import com.ecommerce.ecommerce.service.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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

        ProductInventory latestInventory = productInventoryService.findLastProduct(product);

        model.addAttribute("inventory", latestInventory);
        model.addAttribute("product", product);

        return "/user/product_home.html";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer quantity, Model model){

        int availableQuantity = productInventoryService.findLastProduct(
                productService.getProduct(id).get()
        ).getFinalQuantity();

        if (quantity > availableQuantity) {
            model.addAttribute("error", "Not enough available units to complete the purchase");
            return "redirect:/producthome/" + id;
        }


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

        model.addAttribute("availableQuantity",availableQuantity);
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

    @PostMapping("/updateQuantity")
    public String updateQuantity(@RequestParam Integer productId, @RequestParam Integer quantity, Model model, HttpSession session) {

        User user = userService.findById(Integer.parseInt(session.getAttribute("userId").toString())).get();

        Optional<OrderDetail> orderDetailOptional = details.stream()
                .filter(detail -> detail.getProduct().getId().equals(productId))
                .findFirst();

        if (orderDetailOptional.isPresent()) {
            OrderDetail orderDetail = orderDetailOptional.get();
            orderDetail.setQuantity(quantity);
            orderDetail.setTotal(orderDetail.getPrice() * quantity);
        }

        order.setTotal(details.stream().mapToDouble(OrderDetail::getTotal).sum());

        model.addAttribute("cart", details);
        model.addAttribute("order", order);
        model.addAttribute("user", user);

        return "user/ordersummary";
    }

    @GetMapping("/order")
    public String order(Model model, HttpSession session) {

        Object userIdAttribute = session.getAttribute("userId");

        if (userIdAttribute != null) {
            try {
                Integer userId = Integer.parseInt(userIdAttribute.toString());

                Optional<User> userOp = userService.findById(userId);

                User user = userOp.get();
                model.addAttribute("cart", details);
                model.addAttribute("order", order);
                model.addAttribute("user", user);

                return "user/ordersummary";

            } catch (NumberFormatException e) {
                return "redirect:/user/login";
            }
        } else {
          return "redirect:/user/login";
       }
    }



    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession session, Model model){

        Date creationDate = new Date();
        order.setCreationDate(creationDate);
        order.setNumber(orderService.generateOrderIdentification());

        User user = userService.findById(Integer.parseInt(session.getAttribute("userId").toString())).get();
        order.setUser(user);

        OrderStatus orderStatus = orderStatusService.findById(9).get();
        order.setStatus(orderStatus);

        orderService.save(order);

        for (OrderDetail od:details){
            od.setOrder(order);
            orderDetailService.save(od);
            createProductInventoryLog(od,order, creationDate);
        }

        String url= "redirect:/user/details/"+order.getId();


        order = new Order();
        details.clear();

        return url;

    }

    private void createProductInventoryLog(OrderDetail od,Order order, Date logDate) {

        Product product = od.getProduct();

        List<ProductInventory> productInventoryList = productInventoryService.findByProduct(product);

        if (!productInventoryList.isEmpty()) {

            productInventoryList.sort(Comparator.comparing(ProductInventory::getDate).reversed());

            ProductInventory latestInventory = productInventoryList.get(0);

            int finalExistence = latestInventory.getFinalQuantity();

            ProductInventory pI = new ProductInventory();
            pI.setDate(logDate);
            pI.setOperationName("Order created: "+order.getNumber());
            pI.setInitialQuantity(finalExistence);
            pI.setOperationQuantity((int) od.getQuantity());
            pI.setFinalQuantity(pI.getInitialQuantity()-pI.getOperationQuantity());
            pI.setProduct(product);
            pI.setDetails(od);

            productInventoryService.save(pI);

        }
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

    @Autowired
    private IOrderStatusService orderStatusService;

    private ProductInventory pI = new ProductInventory();

    @Autowired
    private IProductInventoryService productInventoryService;

}
