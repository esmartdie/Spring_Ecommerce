package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.OrderStatus;
import com.ecommerce.ecommerce.repository.IOrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderStatusServiceImpl implements IOrderStatusService{

    @Override
    public OrderStatus save(OrderStatus orderStatus) {
        return orderStatusRepository.save(orderStatus);
    }

    @Override
    public Optional<OrderStatus> findById(Integer id) {
        return orderStatusRepository.findById(id);
    }

    @Autowired
    private IOrderStatusRepository orderStatusRepository;
}
