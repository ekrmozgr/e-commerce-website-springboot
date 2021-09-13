package com.example.springproject.Entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "category_table")
@SequenceGenerator(name = "categoryseq")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoryseq")
    @Column(name = "category_id")
    private int id;

    @NotNull(message = "Category Name Cannot be Null")
    @Size(min = 3,max = 15,message = "Category Name Must be Min 3 - Max 15")
    @Column
    private String category_name;

    @Min(value = 0,message = "Cannot Less Than 0")
    @Max(value = 99,message = "Cannot More Than 99")
    @NotNull(message = "Discount Percent Cannot be Null")
    private int discount_percent = 0;

    @Lob
    @Column
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(int discount_percent) {
        this.discount_percent = discount_percent;
    }

}
