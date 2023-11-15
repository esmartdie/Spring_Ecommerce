package com.ecommerce.ecommerce.controller;


import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.service.IOrderService;
import com.ecommerce.ecommerce.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/registry")
    public String create(){

        return "user/registry";
    }

    @PostMapping("/save")
    public String save(User user){

        LOG.info("User registry: {}", user);
        user.setUserRol("USER");
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "user/login";
    }

    @PostMapping("/access")
    public String access(User user, HttpSession session){
        LOG.info("Access : {}", user);

        Optional<User> userOp = userService.findByEmail(user.getEmail());
        LOG.info("User of db: {}", userOp.get());

        if(userOp.isPresent()){
            session.setAttribute("userId", userOp.get().getId());
            if(userOp.get().getUserRol().equals("ADMIN")){
                return "redirect:/administrator";
            }else{
                return "redirect:/";
            }
        }else{
            LOG.info("User doesn't exists");
        }

        return "redirect:/";
    }

    @GetMapping("/shopping")
    public String getShopping(Model model, HttpSession session){
        model.addAttribute("session", session.getAttribute("userId"));

        User user = userService.findById(Integer.parseInt(session.getAttribute("userId").toString())).get();
        List<Order> orders = orderService.findByUser(user);

        model.addAttribute("orders", orders);

        return "/user/shopping";
    }

    @GetMapping("/details/{id}")
    public String shoppingDetails(@PathVariable Integer id, HttpSession session, Model model){

        LOG.info("Order id: {}", id);
        Optional<Order> order = orderService.findById(id);

        model.addAttribute("details", order.get().getDetails());
        model.addAttribute("session", session.getAttribute("userId"));

        return "user/shoppingdetails";
    }

    @GetMapping("/close")
    public String closeSession(){

        return "redirect:/";
    }


    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    private final Logger LOG= LoggerFactory.getLogger(UserController.class);
}
