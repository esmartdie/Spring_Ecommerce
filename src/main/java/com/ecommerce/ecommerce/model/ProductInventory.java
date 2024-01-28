package com.ecommerce.ecommerce.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class ProductInventory {

    public ProductInventory() {
    }

    public ProductInventory(Integer id, String operationName, Date date, Product product, OrderDetail details,
                            Integer initialQuantity, Integer operationQuantity, Integer finalQuantity) {
        this.id = id;
        this.operationName = operationName;
        this.date = date;
        this.product = product;
        this.details = details;
        this.initialQuantity = initialQuantity;
        this.operationQuantity = operationQuantity;
        this.finalQuantity = finalQuantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderDetail getDetails() {
        return details;
    }

    public void setDetails(OrderDetail details) {
        this.details = details;
        details.setInventory(this);
    }

    public Integer getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(Integer initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public Integer getOperationQuantity() {
        return operationQuantity;
    }

    public void setOperationQuantity(Integer operationQuantity) {
        this.operationQuantity = operationQuantity;
    }

    public Integer getFinalQuantity() {
        return finalQuantity;
    }

    public void setFinalQuantity(Integer finalQuantity) {
        this.finalQuantity = finalQuantity;
    }

    @Override
    public String toString() {
        return "ProductInventory{" +
                "id=" + id +
                ", operationName='" + operationName + '\'' +
                ", date=" + date +
                ", product=" + product +
                ", details=" + details +
                ", initialQuantity=" + initialQuantity +
                ", operationQuantity=" + operationQuantity +
                ", finalQuantity=" + finalQuantity +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String operationName;
    private Date date;
    @ManyToOne
    private Product product;
    @OneToOne
    private OrderDetail details;
    private Integer initialQuantity;
    private Integer operationQuantity;
    private Integer finalQuantity;
}
