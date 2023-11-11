package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Order;

import java.util.List;


public interface IOrderService {

    Order save (Order order);

    List<Order> findAll();

    String generateOrderIdentification();
}
