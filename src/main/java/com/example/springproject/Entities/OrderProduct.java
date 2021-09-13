package com.example.springproject.Entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orderProduct_table")
@SequenceGenerator(name = "orderProductseq")
public class OrderProduct {

    public OrderProduct(String name, float price, int quantity, String image) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public OrderProduct() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderProductseq")
    @Column(name = "orderProduct_id")
    private int id;

    @ManyToMany(mappedBy = "orderProducts",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    List<Order> orders;

    private String name;

    private float price;

    private int quantity;

    @Lob
    @Column
    private String image;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
