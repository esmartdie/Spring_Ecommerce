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
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


@Controller
@RequestMapping("/")
public class HomeController {
    private final Logger LOG = LoggerFactory.getLogger(HomeController.class);
    //List<OrderDetail> details = new ArrayList<OrderDetail>();
    Order order = new Order();
    private final IUserService userService;
    private final IOrderService orderService;
    private final IOrderDetailService orderDetailService;
    private final IOrderStatusService orderStatusService;
    private final IProductInventoryService productInventoryService;
    private final IProductService productService;
    private final ShoppingService shoppingService;

    @Autowired
    public HomeController(IUserService userService, IOrderService orderService, IOrderDetailService orderDetailService,
                          IOrderStatusService orderStatusService, IProductInventoryService productInventoryService,
                          IProductService productService, ShoppingService shoppingService) {
        this.userService = userService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.orderStatusService = orderStatusService;
        this.productInventoryService = productInventoryService;
        this.productService = productService;
        this.shoppingService=shoppingService;
    }


    @GetMapping("")
    public String home(Model model, HttpSession session){
        LOG.info("User session: {}", session.getAttribute("userId"));
        model.addAttribute("products", productService.findAllActiveProducts());
        model.addAttribute("session", session.getAttribute("userId"));
        return "/user/home";
    }

    @PostMapping("/search")
    public String searchProduct(@RequestParam String productName, Model model){
        LOG.info("Product name: {}", productName);
        List<Product> products = productService.findAllActiveProducts().stream()
                .filter(p -> p.getName().toLowerCase().contains(productName.toLowerCase()))
                .collect(Collectors.toList());
        model.addAttribute("products", products);
        return"user/home";
    }

    @GetMapping("/producthome/{id}")
    public String productHome(@PathVariable Integer id, Model model){
        LOG.info("Product Id sent as argument {}", id);
        Optional<Product> optionalProduct = productService.getProduct(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            ProductInventory latestInventory = productInventoryService.findLastProduct(product);
            model.addAttribute("inventory", latestInventory);
            model.addAttribute("product", product);
            return "/user/product_home.html";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer quantity, Model model, HttpSession session) {

        if (shoppingService.isUserNotLoggedIn(session)) {
            return "redirect:/user/login";
        }

        Optional<Product> optionalProduct = productService.getProduct(id);
        if (optionalProduct.isEmpty()) {
            return "redirect:/";
        }

        Product product = optionalProduct.get();
        List<OrderDetail> details = shoppingService.getSessionCart(session);

        Integer availableQuantity = shoppingService.getAvailableQuantity(product);
        if (quantity > availableQuantity ) {
            model.addAttribute("error", "Not enough available units to complete the purchase");
            return "redirect:/producthome/" + id;
        }

        shoppingService.updateOrCreateOrderDetail(quantity, product, details);

        Map<Integer, Integer> availableQuantitiesForCart = shoppingService.getAvailableQuantitiesForCart(details);
        double totalSum = shoppingService.calculateTotalSum(details);
        order.setTotal(totalSum);

        model.addAttribute("availableQuantitiesForCart", availableQuantitiesForCart);
        model.addAttribute("cart", details);
        model.addAttribute("order", order);

        return "user/cart";
    }

    @GetMapping("/delete/cart/{id}")
    public String removeProductCart(@PathVariable Integer id, Model model, HttpSession session) {

        List<OrderDetail> details = shoppingService.getSessionCart(session);

        if (details != null) {
            shoppingService.removeProductFromCart(id, details);

            session.setAttribute("cart", details);

            double totalSum = shoppingService.calculateTotalSum(details);
            order.setTotal(totalSum);

            Map<Integer, Integer> availableQuantitiesForCart = shoppingService.getAvailableQuantitiesForCart(details);

            session.setAttribute("orderTotal", totalSum);
            model.addAttribute("cart", details);
            model.addAttribute("order", order);
            model.addAttribute("availableQuantitiesForCart", availableQuantitiesForCart);
        }

        return shoppingService.redirectToCartOrHomePage(details);

    }

    @GetMapping("/getCart")
    public String getCart(Model model, HttpSession session){

        List<OrderDetail> details = shoppingService.getSessionCart(session);

        boolean isEmptyCart = details == null || details.isEmpty();
        if (isEmptyCart) {
            model.addAttribute("isEmptyCart", true);
        }else{
            Map<Integer, Integer> availableQuantitiesForCart = shoppingService.getAvailableQuantitiesForCart(details);
            model.addAttribute("availableQuantitiesForCart", availableQuantitiesForCart);
        }
        model.addAttribute("cart", details);
        model.addAttribute("order", order);
        model.addAttribute("session", session.getAttribute("userId"));

        return "/user/cart";
    }

    @PostMapping("/update/cart")
    public String updateCart(@RequestParam MultiValueMap<String, String> quantitiesMap,
                             @RequestParam(value = "productIds", required = false) List<Integer> productIds,
                             HttpSession session, Model model) {

        List<OrderDetail> cart = shoppingService.getSessionCart(session);

        if (cart != null && productIds != null) {
            shoppingService.updateCartQuantities(quantitiesMap, productIds, cart);
            double totalSum = shoppingService.updateOrderTotal(session, cart);
            order.setTotal(totalSum);
            session.setAttribute("totalSum", totalSum);
            model.addAttribute("order", order);
        }
        session.setAttribute("cart", cart);

        return "redirect:/getCart";
    }

    @PostMapping("/order")
    public String order(Model model, HttpSession session) {

        Object userIdAttribute = session.getAttribute("userId");

        if (userIdAttribute == null || !(userIdAttribute instanceof Integer)) {
            return "redirect:/user/login";
        }

        Optional<User> userOp = userService.findById((Integer) userIdAttribute);

        if (!userOp.isPresent()) {
            return "redirect:/user/login";
        }

        User user = userOp.get();
        List<OrderDetail> details = shoppingService.getSessionCart(session);

        model.addAttribute("cart", details);
        model.addAttribute("order", order);
        model.addAttribute("user", user);
        model.addAttribute("totalSum", order.getTotal());

        return "user/ordersummary";
    }

    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession session, Model model){

        Date creationDate = new Date();
        OrderStatus orderStatus = orderStatusService.findById(9).get();
        User user = userService.findById(Integer.parseInt(session.getAttribute("userId").toString())).get();
        order.setCreationDate(creationDate);
        order.setNumber(orderService.generateOrderIdentification());
        order.setUser(user);
        order.setStatus(orderStatus);
        orderService.save(order);

        List<OrderDetail> details = shoppingService.getSessionCart(session);;

        for (OrderDetail od:details){
            od.setOrder(order);
            orderDetailService.save(od);
            productInventoryService.newOrderProductInventoryLog(od,order, creationDate);
            if(od.getProduct().getQuantity()==0){
                od.getProduct().setActive(false);
                productService.update(od.getProduct());
            }
        }

        String url= "redirect:/user/details/"+order.getId();

        order = new Order();
        details.clear();
        session.removeAttribute("cart");
        session.removeAttribute("sumTotal");

        return url;
    }
}
