package com.ecommerce.ecommerce.model;

public class User {

    public User() {
    }

    public User(Integer id, String name, String username, String email, String address,
                String telephone, String userRol, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.telephone = telephone;
        this.userRol = userRol;
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getUserRol() {
        return userRol;
    }

    public void setUserRol(String userRol) {
        this.userRol = userRol;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", userRol='" + userRol + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    private Integer id;
    private String name;
    private String username;
    private String email;
    private String address;
    private String telephone;
    private String userRol;
    private String password;
}
