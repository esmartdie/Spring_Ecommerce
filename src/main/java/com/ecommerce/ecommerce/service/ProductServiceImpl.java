package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {
    @Override
    public Product save(Product product) {
        return IProductRepository.save(product);
    }

    @Override
    public Optional<Product> getProduct(Integer id) {
        return IProductRepository.findById(id);
    }

    @Override
    public void update(Product product) {
        IProductRepository.save(product);
    }

    @Override
    public void delete(Integer id) {
        IProductRepository.deleteById(id);
    }

    @Override
    public List<Product> findAll() {
        return IProductRepository.findAll();
    }


    @Autowired
    private IProductRepository IProductRepository;

}
