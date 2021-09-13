package com.example.springproject.Repositories;

import com.example.springproject.Entities.Order;
import com.example.springproject.Entities.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.ManagedBean;
import java.util.List;

@ManagedBean
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {
    List<OrderProduct> getOrderProductsByOrders(Order order);
}
