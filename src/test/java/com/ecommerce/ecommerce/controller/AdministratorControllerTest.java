package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.OrderStatus;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.ProductInventory;
import com.ecommerce.ecommerce.service.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.*;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdministratorControllerTest {

    @Mock
    private IProductService productService;
    @Mock
    private IUserService userService;
    @Mock
    private IOrderService orderService;
    @Mock
    private IOrderStatusService orderStatusService;
    @Mock
    private IProductInventoryService productInventoryService;
    @Mock
    private Model model;
    @InjectMocks
    private AdministratorController controller;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testHome() {
        String productName = "test";
        Product product = new Product();
        product.setName("Test Product");
        List<Product> products = Collections.singletonList(product);

        when(productService.findAllActiveProducts()).thenReturn(products);

        String result = controller.home(productName, model);

        assertEquals("administrator/home", result);
        verify(model).addAttribute("products", products);
    }

    @Test
    void testProductHome() {
        Integer productId = 1;
        Product product = new Product();
        product.setId(productId);
        ProductInventory inventory = new ProductInventory();

        when(productService.getProduct(productId)).thenReturn(Optional.of(product));
        when(productInventoryService.findLastProduct(product)).thenReturn(inventory);

        String result = controller.productHome(productId, model);

        assertEquals("administrator/product_home", result);

        verify(model).addAttribute("inventory", inventory);
        verify(model).addAttribute("product", product);
        verifyNoMoreInteractions(model);
    }


    @Test
    void testOrders() {
        List<Order> orders = Collections.emptyList();

        when(orderService.findAll()).thenReturn(orders);

        String result = controller.orders(model);

        assertEquals("administrator/orders", result);
        verify(model).addAttribute("orders", orders);
    }

    @Test
    void testDetails() {
        Integer orderId = 1;
        Order order = new Order();
        order.setId(orderId);
        when(orderService.findById(orderId)).thenReturn(Optional.of(order));
        List<OrderStatus> orderStatusList = Arrays.asList(new OrderStatus(), new OrderStatus());
        when(orderStatusService.findAll()).thenReturn(orderStatusList);

        String result = controller.details(model, orderId);

        assertEquals("administrator/shoppingdetails", result);
        verify(model).addAttribute("order", order);
        verify(model).addAttribute("orderdetails", order.getDetails());
        verify(model).addAttribute("orderStatusList", orderStatusList);
        verifyNoMoreInteractions(model);
    }

    @Test
    void testUpdateOrderStatus() {
        Integer orderId = 1;
        String newStatus = "NEW_STATUS";
        Order order = new Order();
        when(orderService.findById(orderId)).thenReturn(Optional.of(order));
        OrderStatus status = new OrderStatus();
        when(orderStatusService.findByStatus(newStatus)).thenReturn(status);

        String result = controller.updateOrderStatus(orderId, newStatus);

        assertEquals("redirect:/administrator/orders", result);
        assertEquals(status, order.getStatus());
        verify(orderService).save(order);
    }

    @Test
    void testSearchProduct() {
        String productName = "test";
        Product product = new Product();
        product.setName(productName);
        List<Product> products = Collections.singletonList(product);

        when(productService.findAllActiveProducts()).thenReturn(products);

        String result = controller.searchProduct(productName, model);

        assertEquals("administrator/home", result);
        verify(model).addAttribute("products", products);
    }
}