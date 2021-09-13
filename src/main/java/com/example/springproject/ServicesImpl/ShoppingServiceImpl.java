package com.example.springproject.ServicesImpl;

import com.example.springproject.Entities.Basket;
import com.example.springproject.Entities.Category;
import com.example.springproject.Entities.Product;
import com.example.springproject.Repositories.*;
import com.example.springproject.Services.ShoppingService;
import org.springframework.stereotype.Service;

import javax.faces.bean.ManagedProperty;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShoppingServiceImpl implements ShoppingService {

    @ManagedProperty(value="basketRepository")
    private BasketRepository basketRepository;

    @ManagedProperty(value="productRepository")
    private ProductRepository productRepository;

    @ManagedProperty(value="categoryRepository")
    private CategoryRepository categoryRepository;

    @ManagedProperty(value="orderRepository")
    private OrderRepository orderRepository;

    @ManagedProperty(value = "orderProductRepository")
    private OrderProductRepository orderProductRepository;

    public ShoppingServiceImpl(BasketRepository basketRepository, ProductRepository productRepository, CategoryRepository categoryRepository,
                               OrderProductRepository orderProductRepository, OrderRepository orderRepository) {
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderProductRepository = orderProductRepository;
        this.orderRepository = orderRepository;}

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        return productRepository.getProductsByCategory(category);
    }

    @Override
    public Basket getBasketByUserId(int id) {return basketRepository.getBasketByUserId(id);}

    @Override
    public List<Product> getProductsByBasket(Basket basket) {
        return productRepository.getProductsByBaskets(basket);
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.saveAndFlush(category);
    }

    @Override
    public void createProduct(Product product) {
        productRepository.saveAndFlush(product);
    }

    @Override
    public void deleteCategory(Category category) {
        categoryRepository.delete(category); categoryRepository.flush();
    }

    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product);productRepository.flush();
    }

    @Override
    public void deleteProductsByCategory_Id(int id) {
        productRepository.deleteProductsByCategory_Id(id);productRepository.flush();
    }

    @Override
    public void deleteProductsFromBaskets(List<Product> products) {
        List<Basket> baskets = basketRepository.findAll();
        for (Basket basket:baskets)
        {
            for (Product product:products)
            {
                product.getBaskets().removeAll(product.getBaskets());
                basket.getProducts().remove(product);
            }
            basketRepository.saveAndFlush(basket);
        }
    }

    @Override
    public void deleteProductsFromBaskets(Product product) {
        List<Basket> baskets = basketRepository.findAll();
        product.getBaskets().removeAll(product.getBaskets());
        for (Basket basket:baskets)
        {
            List<Product> basket_products = basket.getProducts();
            for (Product b_product:basket_products) {
                if(b_product.getProduct_id() == product.getProduct_id())
                {
                    basket.getProducts().remove(b_product);
                    break;
                }
            }
            basketRepository.saveAndFlush(basket);
        }
    }

    @Override
    public void updateCategory(Category category) {
        categoryRepository.saveAndFlush(category);
    }

    @Override
    public void updateProduct(Product product) {
        productRepository.saveAndFlush(product);
    }

    @Override
    public void updateAllProducts(List<Product> products) {
        productRepository.saveAllAndFlush(products);
    }

    @Override
    public void updateBasket(Basket basket) {
        basketRepository.saveAndFlush(basket);
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.getById(id);
    }
}
