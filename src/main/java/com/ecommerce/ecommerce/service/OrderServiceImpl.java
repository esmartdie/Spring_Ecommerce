package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService{

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
    @Override
    public String generateOrderIdentification(){
        int number = 0;
        String numberConcatenated = "0000000000";

        List<Order> orders = findAll();

        List <Integer> numbers = new ArrayList<Integer>();

        orders.stream().forEach(o -> numbers.add(Integer.parseInt(o.getNumber())));

        if (orders.isEmpty()){
            number=1;
        }else{
            number=numbers.stream().max(Integer::compare).get();
            number++;
        }

        numberConcatenated += String.valueOf(number);

        int initialCount = Math.max(0, (numberConcatenated.length()-10));

        return numberConcatenated.substring(initialCount, numberConcatenated.length());
    }

    @Autowired
    private IOrderRepository orderRepository;
}
