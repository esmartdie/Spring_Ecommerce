package com.ecommerce.ecommerce.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;


import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    public Product() {
    }

    public Product(Integer id, String name, String description, String image,
                   double price, int quantity, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.user = user;;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", active=" + active +
                ", user=" + user +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<ProductInventory> getInventory() {
        return inventory;
    }

    public void setInventory(List<ProductInventory> inventory) {
        this.inventory = inventory;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String image;
    private double price;
    private int quantity;
    @Convert(converter = org.hibernate.type.YesNoConverter.class)
    private Boolean active = true;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "product")
    private List<ProductInventory> inventory;

}
