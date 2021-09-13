package com.example.springproject.Repositories;

import com.example.springproject.Entities.Basket;
import com.example.springproject.Entities.Category;
import com.example.springproject.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.ManagedBean;
import java.util.List;

@ManagedBean
public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> getProductsByCategory(Category category);
    List<Product> getProductsByBaskets(Basket basket);
    void deleteProductsByCategory_Id(int id);
}
