package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.OrderDetail;
import com.ecommerce.ecommerce.repository.IOrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl implements IOrderDetailService{

    @Override
    public OrderDetail save(OrderDetail orderDetail) {

        return orderDetailsRepository.save(orderDetail);
    }

    @Autowired
    private IOrderDetailsRepository orderDetailsRepository;
}
