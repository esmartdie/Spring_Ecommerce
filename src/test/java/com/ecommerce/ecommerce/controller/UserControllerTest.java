package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.User;
import com.ecommerce.ecommerce.service.IOrderService;
import com.ecommerce.ecommerce.service.IUserService;
import com.ecommerce.ecommerce.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private HttpSession session;
    @Mock
    private Model model;
    @Mock
    private IUserService userService;

    @Mock
    private IOrderService orderService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private SessionUtils sessionUtils = mock(SessionUtils.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testShowRegistryForm() {
        String viewName = userController.create();
        assertEquals("user/registry", viewName);
    }

    @Test
    void testSaveUser() {
        User user = new User();
        when(userService.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");

        String viewName = userController.saveUser(user);
        assertEquals("redirect:/", viewName);
        verify(userService).save(user);
    }

    @Test
    void testShowLoginForm() {
        String viewName = userController.login();
        assertEquals("user/login", viewName);
    }

    @Test
    void testAccessUser() {
        User user = new User();
        user.setUserRol("USER");
        when(userService.findById(anyInt())).thenReturn(java.util.Optional.of(user));

        String viewName = userController.access(user, session);
        assertEquals("redirect:/", viewName);
    }

    @Test
    void testAccessUserAdmin() {
        User user = new User();
        user.setUserRol("ADMIN");
        when(userService.findById(anyInt())).thenReturn(java.util.Optional.of(user));

        String viewName = userController.access(user, session);
        assertEquals("redirect:/administrator", viewName);
    }

    @Test
    void testShowShoppingPage() {
        User user = new User();
        when(session.getAttribute("userId")).thenReturn(0);
        when(userService.findById(anyInt())).thenReturn(java.util.Optional.of(user));
        when(orderService.findByUser(any(User.class))).thenReturn(Collections.emptyList());

        String viewName = userController.getShopping(model, session);
        assertEquals("/user/shopping", viewName);
        verify(model).addAttribute("session", 0);
        verify(model).addAttribute("orders", Collections.emptyList());
    }

    @Test
    void testShowShoppingDetails() {
        Order order = new Order();
        when(orderService.findById(anyInt())).thenReturn(java.util.Optional.of(order));
        when(session.getAttribute("userId")).thenReturn(1);

        String viewName = userController.shoppingDetails(1, session, model);
        assertEquals("user/shoppingdetails", viewName);
        verify(model).addAttribute("details", order.getDetails());
        verify(model).addAttribute("session", 1);
    }

    @Test
    void testCloseSession() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(get("/user/close").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(sessionUtils).removeAllAttributes(session);
    }

}