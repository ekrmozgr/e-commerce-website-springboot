package com.example.springproject.Entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "product_table")
@SequenceGenerator(name = "productseq")
public class Product {
    public Product() {}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productseq")
    @Column(name = "product_id")
    private int product_id;

    @NotNull(message = "Product Name Cannot be Null")
    @Size(min = 3,max = 50,message = "Product Name Must be Min 3 - Max 50")
    private String product_name;

    @Min(value = 0,message = "Cannot Less Than 0")
    @NotNull(message = "Product Quantity Cannot be Null")
    private int product_quantity;

    @Min(value = 1,message = "Cannot Less Than 1")
    @NotNull(message = "Price Cannot be Null")
    private float price;

    @Min(value = 0,message = "Cannot Less Than 0")
    @NotNull(message = "Basket Quantity Cannot be Null")
    private int basket_quantity;

    @Min(value = 0,message = "Cannot Less Than 0")
    @Max(value = 99,message = "Cannot More Than 99")
    @NotNull(message = "Discount Percent Cannot be Null")
    private int discount_percent;

    private float discount_price;

    @Lob
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToMany(mappedBy = "products",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    List<Basket> baskets;

    public List<Basket> getBaskets() {
        return baskets;
    }

    public void setBaskets(List<Basket> baskets) {
        this.baskets = baskets;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getBasket_quantity() {
        return basket_quantity;
    }

    public void setBasket_quantity(int basket_quantity) {
        this.basket_quantity = basket_quantity;
    }

    public int getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(int discount_percent) {
        this.discount_percent = discount_percent;
    }

    public float getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(float discount_price) {
        this.discount_price = discount_price;
    }

    public String getImage() {return image;}

    public void setImage(String image) {this.image = image;}
}
