package com.example.springproject.Repositories;

import com.example.springproject.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.ManagedBean;


@ManagedBean
public interface OrderRepository extends JpaRepository<Order,Integer> {
}
