package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceImpl implements IOrderService{

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Autowired
    private IOrderRepository orderRepository;
}
