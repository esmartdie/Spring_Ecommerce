package com.ecommerce.ecommerce.controller;


import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.service.IOrderService;
import com.ecommerce.ecommerce.service.IUserService;
import com.ecommerce.ecommerce.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final BCryptPasswordEncoder passwordEncoder;
    private final IUserService userService;
    private final IOrderService orderService;
    private final SessionUtils sessionUtils;

    @Autowired
    public UserController(BCryptPasswordEncoder passwordEncoder, IUserService userService,
                          IOrderService orderService,SessionUtils sessionUtils) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.orderService = orderService;
        this.sessionUtils= sessionUtils;
    }

    @GetMapping("/registry")
    public String create(){
        return "user/registry";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        logger.info("User registry: {}", user);
        user.setUserRol("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "user/login";
    }

    @GetMapping("/access")
    public String access(User user, HttpSession session){
        logger.info("Access : {}", user);

        Integer userId = sessionUtils.getUserIdFromSession(session);
        Optional<User> userOp = userService.findById(userId);
        if (userOp.isPresent()) {
            session.setAttribute("userId", userOp.get().getId());
            String userRol = userOp.get().getUserRol();
            return userRol.equals("ADMIN") ? "redirect:/administrator" : "redirect:/";
        } else {
            logger.info("User doesn't exist");
            return "redirect:/";
        }
    }

    @GetMapping("/shopping")
    public String getShopping(Model model, HttpSession session){
        Integer userId = sessionUtils.getUserIdFromSession(session);
        model.addAttribute("session", userId);

        User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Order> orders = orderService.findByUser(user);
        model.addAttribute("orders", orders);

        return "/user/shopping";
    }

    @GetMapping("/details/{id}")
    public String shoppingDetails(@PathVariable Integer id, HttpSession session, Model model){
        logger.info("Order id: {}", id);

        Optional<Order> order = orderService.findById(id);
        model.addAttribute("details", order.get().getDetails());
        model.addAttribute("session", session.getAttribute("userId"));

        return "user/shoppingdetails";
    }

    @GetMapping("/close")
    public String closeSession(HttpSession session){

        sessionUtils.removeAllAttributes(session);
        return "redirect:/";
    }
}
