package com.example.springproject.Services;

import com.example.springproject.Entities.Basket;
import com.example.springproject.Entities.Category;
import com.example.springproject.Entities.Product;

import java.util.List;
import java.util.Optional;

public interface ShoppingService {
    List<Category>      getAllCategories();
    List<Product>       getAllProducts();
    List<Product>       getProductsByCategory(Category category);
    Basket              getBasketByUserId(int id);
    Optional<Category>  getCategoryById(int id);
    List<Product>       getProductsByBasket(Basket basket);
    Product             getProductById(int id);

    void                createCategory(Category category);
    void                createProduct(Product product);

    void                deleteCategory(Category category);
    void                deleteProduct(Product product);
    void                deleteProductsByCategory_Id(int id);
    void                deleteProductsFromBaskets(List<Product> product);
    void                deleteProductsFromBaskets(Product product);

    void                updateCategory(Category category);
    void                updateProduct(Product product);
    void                updateBasket(Basket basket);
    void                updateAllProducts(List<Product> products);

}
