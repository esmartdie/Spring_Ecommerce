package com.ecommerce.ecommerce.model;

import java.util.Date;

public class Order {

    public Order() {
    }

    public Order(Integer id, String number, Date creationDate, Date receptionDate, double total) {
        this.id = id;
        this.number = number;
        this.creationDate = creationDate;
        this.receptionDate = receptionDate;
        this.total = total;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getReceptionDate() {
        return receptionDate;
    }

    public void setReceptionDate(Date receptionDate) {
        this.receptionDate = receptionDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", receptionDate=" + receptionDate +
                ", total=" + total +
                '}';
    }

    private Integer id;
    private String number;
    private Date creationDate;
    private Date receptionDate;
    private double total;
}
