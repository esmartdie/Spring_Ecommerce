package com.ecommerce.ecommerce.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ecommerce.ecommerce.model.OrderDetail;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.ProductInventory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ShoppingServiceTest {


    @Mock
    IProductInventoryService productInventoryService;

    @InjectMocks
    ShoppingService shoppingService;

    @Test
    void isUserNotLoggedIn_UserLoggedIn_ReturnsFalse() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", 1);

        boolean result = shoppingService.isUserNotLoggedIn(session);

        assertFalse(result);
    }

    @Test
    void isUserNotLoggedIn_UserNotLoggedIn_ReturnsTrue() {
        MockHttpSession session = new MockHttpSession();

        boolean result = shoppingService.isUserNotLoggedIn(session);

        assertTrue(result);
    }

    @Test
    void getSessionCart_SessionCartExists_ReturnsCart() {
        MockHttpSession session = new MockHttpSession();
        List<OrderDetail> expectedCart = new ArrayList<>();
        session.setAttribute("cart", expectedCart);

        List<OrderDetail> result = shoppingService.getSessionCart(session);

        assertEquals(expectedCart, result);
    }

    @Test
    void getSessionCart_SessionCartDoesNotExist_ReturnsNewCart() {
        MockHttpSession session = new MockHttpSession();

        List<OrderDetail> result = shoppingService.getSessionCart(session);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getAvailableQuantity_ProductExists_ReturnsQuantity() {
        Product product = new Product();
        product.setId(1);
        product.setName("Product 1");
        product.setPrice(10.0);
        product.setQuantity(20);

        when(productInventoryService.findLastProduct(product)).thenReturn(new ProductInventory(1, product, 20, 0));

        Integer result = shoppingService.getAvailableQuantity(product);

        assertEquals(20, result);
    }

    @Test
    void updateOrCreateOrderDetail_ExistingOrderDetailExists_UpdatesDetail() {
        // Arrange
        Product product = new Product();
        product.setId(1);
        product.setName("Product 1");
        product.setPrice(10.0);
        OrderDetail existingOrderDetail = new OrderDetail();
        existingOrderDetail.setProduct(product);
        existingOrderDetail.setQuantity(2);
        existingOrderDetail.setPrice(10.0);
        existingOrderDetail.setTotal(20.0);
        List<OrderDetail> details = new ArrayList<>(Arrays.asList(existingOrderDetail));

        // Act
        shoppingService.updateOrCreateOrderDetail(3, product, details);

        // Assert
        assertEquals(5, existingOrderDetail.getQuantity());
        assertEquals(50.0, existingOrderDetail.getTotal());
    }

    @Test
    void updateOrCreateOrderDetail_ExistingOrderDetailDoesNotExist_CreatesNewDetail() {
        // Arrange
        Product product = new Product();
        product.setId(1);
        product.setName("Product 1");
        product.setPrice(10.0);
        List<OrderDetail> details = new ArrayList<>();

        // Act
        shoppingService.updateOrCreateOrderDetail(3, product, details);

        // Assert
        assertEquals(1, details.size());
        OrderDetail orderDetail = details.get(0);
        assertEquals(product, orderDetail.getProduct());
        assertEquals(3, orderDetail.getQuantity());
        assertEquals(30.0, orderDetail.getTotal());
    }

    @Test
    void getAvailableQuantitiesForCart_CartWithMultipleProducts_ReturnsQuantitiesMap() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Product 1");
        product1.setPrice(10.0);

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Product 2");
        product2.setPrice(20.0);

        List<OrderDetail> details = new ArrayList<>();
        OrderDetail detail1 = new OrderDetail();
        detail1.setProduct(product1);
        details.add(detail1);
        OrderDetail detail2 = new OrderDetail();
        detail2.setProduct(product2);
        details.add(detail2);

        when(productInventoryService.findLastProduct(product1)).thenReturn(new ProductInventory(1, product1, 20, 0));
        when(productInventoryService.findLastProduct(product2)).thenReturn(new ProductInventory(2, product2, 15, 0));

        // Act
        Map<Integer, Integer> result = shoppingService.getAvailableQuantitiesForCart(details);

        // Assert
        assertEquals(2, result.size());
        assertEquals(20, result.get(1));
        assertEquals(15, result.get(2));
    }

    @Test
    void calculateTotalSum_CartWithMultipleProducts_ReturnsTotalSum() {
        // Arrange
        OrderDetail detail1 = new OrderDetail();
        detail1.setPrice(10.0);
        detail1.setQuantity(2);
        OrderDetail detail2 = new OrderDetail();
        detail2.setPrice(20.0);
        detail2.setQuantity(3);
        List<OrderDetail> details = Arrays.asList(detail1, detail2);

        // Act
        double result = shoppingService.calculateTotalSum(details);

        // Assert
        assertEquals(80.0, result);
    }

    @Test
    void removeProductFromCart_ProductExists_RemovesProductFromCart() {
        // Arrange
        Product product = new Product();
        product.setId(1);
        product.setName("Product 1");
        product.setPrice(10.0);
        OrderDetail detail1 = new OrderDetail();
        detail1.setProduct(product);
        OrderDetail detail2 = new OrderDetail();
        detail2.setProduct(new Product());
        List<OrderDetail> details = new ArrayList<>(Arrays.asList(detail1, detail2));

        // Act
        shoppingService.removeProductFromCart(1, details);

        // Assert
        assertEquals(1, details.size());
        assertNotEquals(product, details.get(0).getProduct());
    }

    @Test
    void redirectToCartOrHomePage_CartNotEmpty_ReturnsCartPage() {
        // Arrange
        List<OrderDetail> details = Arrays.asList(new OrderDetail(), new OrderDetail());

        // Act
        String result = shoppingService.redirectToCartOrHomePage(details);

        // Assert
        assertEquals("user/cart", result);
    }

    @Test
    void redirectToCartOrHomePage_CartEmpty_ReturnsHomePage() {
        // Arrange
        List<OrderDetail> details = new ArrayList<>();

        // Act
        String result = shoppingService.redirectToCartOrHomePage(details);

        // Assert
        assertEquals("redirect:/getCart", result);
    }

    @Test
    void updateCartQuantities_ValidQuantities_UpdatesCartQuantities() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Product 1");
        product1.setPrice(10.0);
        OrderDetail detail1 = new OrderDetail();
        detail1.setProduct(product1);
        detail1.setPrice(10.0);
        detail1.setQuantity(1);

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Product 2");
        product2.setPrice(20.0);
        OrderDetail detail2 = new OrderDetail();
        detail2.setProduct(product2);
        detail2.setPrice(20.0);
        detail2.setQuantity(1);

        List<OrderDetail> cart = Arrays.asList(detail1, detail2);

        MultiValueMap<String, String> quantitiesMap = new LinkedMultiValueMap<>();
        quantitiesMap.add("quantities-1", "3");
        quantitiesMap.add("quantities-2", "2");

        // Act
        shoppingService.updateCartQuantities(quantitiesMap, Arrays.asList(1, 2), cart);

        // Assert
        assertEquals(3, detail1.getQuantity());
        assertEquals(30.0, detail1.getTotal());
        assertEquals(2, detail2.getQuantity());
        assertEquals(40.0, detail2.getTotal());
    }

    @Test
    void updateOrderTotal_CalculatesTotalSumCorrectly() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Product 1");
        product1.setPrice(10.0);
        OrderDetail detail1 = new OrderDetail();
        detail1.setProduct(product1);
        detail1.setPrice(10.0);
        detail1.setQuantity(2);

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Product 2");
        product2.setPrice(20.0);
        OrderDetail detail2 = new OrderDetail();
        detail2.setProduct(product2);
        detail2.setPrice(20.0);
        detail2.setQuantity(3);

        List<OrderDetail> cart = Arrays.asList(detail1, detail2);

        // Act
        double result = shoppingService.updateOrderTotal(null, cart);

        // Assert
        assertEquals(100.0, result);
    }
}