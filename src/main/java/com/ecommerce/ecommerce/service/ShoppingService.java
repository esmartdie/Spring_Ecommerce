package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.OrderDetail;
import com.ecommerce.ecommerce.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.*;

@Service
public class ShoppingService {
    private final IProductInventoryService productInventoryService;

    @Autowired
    public ShoppingService(IProductInventoryService productInventoryService) {
        this.productInventoryService = productInventoryService;
    }

    public boolean isUserNotLoggedIn(HttpSession session) {
        return session.getAttribute("userId") == null;
    }

    public List<OrderDetail> getSessionCart(HttpSession session) {
        List<OrderDetail> details = (List<OrderDetail>) session.getAttribute("cart");
        if (details == null) {
            details = new ArrayList<>();
            session.setAttribute("cart", details);
        }
        return details;
    }

    public Integer getAvailableQuantity(Product product) {
        return productInventoryService.findLastProduct(product).getFinalQuantity();
    }

    public void updateOrCreateOrderDetail(Integer quantity, Product product, List<OrderDetail> details) {
        OrderDetail existingOrderDetail = findExistingOrderDetail(product.getId(), details);
        if (existingOrderDetail != null) {
            existingOrderDetail.setQuantity(existingOrderDetail.getQuantity() + quantity);
            existingOrderDetail.setTotal(existingOrderDetail.getQuantity() * existingOrderDetail.getPrice());
        } else {
            createNewOrderDetail(quantity, product, details);
        }
    }

    private OrderDetail findExistingOrderDetail(Integer productId, List<OrderDetail> details) {
        return details.stream()
                .filter(detail -> detail.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }
    private void createNewOrderDetail(Integer quantity, Product product, List<OrderDetail> details) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(product.getPrice());
        orderDetail.setName(product.getName());
        orderDetail.setTotal(product.getPrice() * quantity);
        orderDetail.setProduct(product);
        details.add(orderDetail);
    }

    public Map<Integer, Integer> getAvailableQuantitiesForCart(List<OrderDetail> details) {
        Map<Integer, Integer> availableQuantitiesForCart = new HashMap<>();
        for (OrderDetail orderDetail : details) {
            Integer keyValueIdProduct = orderDetail.getProduct().getId();
            Integer productFinalQuantity = productInventoryService.findLastProduct(orderDetail.getProduct()).getFinalQuantity();
            availableQuantitiesForCart.put(keyValueIdProduct, productFinalQuantity);
        }
        return availableQuantitiesForCart;
    }

    public double calculateTotalSum(List<OrderDetail> details) {
        return details.stream().mapToDouble(OrderDetail::getTotal).sum();
    }

    public void removeProductFromCart(Integer id, List<OrderDetail> details) {
        details.removeIf(orderDetail -> orderDetail.getProduct().getId().equals(id));
    }

    public String redirectToCartOrHomePage(List<OrderDetail> details) {
        return details.isEmpty() ? "redirect:/getCart" : "user/cart";
    }

    public void updateCartQuantities(MultiValueMap<String, String> quantitiesMap,
                                     List<Integer> productIds, List<OrderDetail> cart) {
        for (Integer productId : productIds) {
            String inputName = "quantities-" + productId;
            List<String> quantities = quantitiesMap.get(inputName);
            if (quantities != null && !quantities.isEmpty()) {
                Integer quantity = parseQuantity(quantities.get(0));
                for (OrderDetail detail : cart) {
                    if (detail.getProduct().getId().equals(productId)) {
                        updateDetailQuantityAndTotal(detail, quantity);
                        break;
                    }
                }
            }
        }
    }

    private Integer parseQuantity(String quantityStr) {
        int dotIndex = quantityStr.indexOf('.');
        String integerPart = (dotIndex != -1) ? quantityStr.substring(0, dotIndex) : quantityStr;
        return Integer.parseInt(integerPart);
    }

    private void updateDetailQuantityAndTotal(OrderDetail detail, Integer quantity) {
        detail.setQuantity(quantity);
        detail.setTotal(detail.getPrice() * quantity);
    }

    public double updateOrderTotal(HttpSession session, List<OrderDetail> cart) {
       return cart.stream().mapToDouble(detail -> detail.getPrice() * detail.getQuantity()).sum();
    }

}
