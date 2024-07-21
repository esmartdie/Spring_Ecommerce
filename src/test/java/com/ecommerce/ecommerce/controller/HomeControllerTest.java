package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.OrderDetail;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.ProductInventory;
import com.ecommerce.ecommerce.service.*;
import com.ecommerce.ecommerce.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HomeControllerTest {

    @Mock
    IUserService userService;
    @Mock
    IOrderService orderService;
    @Mock
    IOrderDetailService orderDetailService;
    @Mock
    IOrderStatusService orderStatusService;
    @Mock
    IProductInventoryService productInventoryService;
    @Mock
    IProductService productService;
    @Mock
    ShoppingService shoppingService;

    @InjectMocks
    HomeController homeController;

    private SessionUtils sessionUtils;

    @Mock
    HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void home_UserLoggedIn_ReturnsHomePage() {

        session.setAttribute("userId", 1);
        List<Product> products = new ArrayList<>();
        when(productService.findAllActiveProducts()).thenReturn(products);
        Model model = mock(Model.class);

        String result = homeController.home(model, session);
        session.removeAttribute("session");

        assertEquals("/user/home", result);
        verify(model).addAttribute("products", products);
        verify(model).addAttribute("session", session.getAttribute("userId"));
    }

    @Test
    void home_UserNotLoggedIn_ReturnsHomePage() {
        List<Product> products = new ArrayList<>();
        when(productService.findAllActiveProducts()).thenReturn(products);
        Model model = mock(Model.class);

        String result = homeController.home(model, session);

        assertEquals("/user/home", result);
        verify(model).addAttribute("products", products);
        verify(model).addAttribute("session", null);
    }

    @Test
    void searchProduct_ValidProductName_ReturnsHomePage() {
        String productName = "Test Product";
        List<Product> products = new ArrayList<>();
        when(productService.findAllActiveProducts()).thenReturn(products);
        Model model = mock(Model.class);

        String result = homeController.searchProduct(productName, model);

        assertEquals("user/home", result);
        verify(productService).findAllActiveProducts();
        verify(model).addAttribute("products", products);
    }

    @Test
    void productHome_ValidProductId_ReturnsProductHomePage() {

        int productId = 1;
        Product product = new Product();
        when(productService.getProduct(productId)).thenReturn(Optional.of(product));
        ProductInventory productInventory = new ProductInventory();
        when(productInventoryService.findLastProduct(product)).thenReturn(productInventory);
        Model model = mock(Model.class);

        String result = homeController.productHome(productId, model);

        assertEquals("/user/product_home.html", result);
        verify(productService).getProduct(productId);
        verify(productInventoryService).findLastProduct(product);
        verify(model).addAttribute("inventory", productInventory);
        verify(model).addAttribute("product", product);
    }

    @Test
    void addCart_UserNotLoggedIn_RedirectsToLoginPage() {
        when(session.getAttribute("userId")).thenReturn(null);
        when(shoppingService.isUserNotLoggedIn(session)).thenReturn(true);
        int productId = 1;
        int quantity = 1;
        Model model = mock(Model.class);

        String result = homeController.addCart(productId, quantity, model, session);

        assertEquals("redirect:/user/login", result);
    }
}

