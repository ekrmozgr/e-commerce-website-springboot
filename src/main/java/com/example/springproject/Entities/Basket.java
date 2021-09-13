package com.example.springproject.Entities;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@Table(name="basket_table")
public class Basket {
    @Id
    private int id;

    @Min(value = 0)
    private float total_price = 0;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
            name = "basket_products",
            joinColumns = @JoinColumn(name = "basket_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    List<Product> products;


    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    public Basket(int total_price, List<Product> products, User user) {
        this.total_price = total_price;
        this.products = products;
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public Basket() {
    }
}
