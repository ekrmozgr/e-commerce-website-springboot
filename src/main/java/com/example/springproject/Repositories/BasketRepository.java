package com.example.springproject.Repositories;

import com.example.springproject.Entities.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.ManagedBean;


@ManagedBean
public interface BasketRepository extends JpaRepository<Basket, Integer> {
    Basket getBasketByUserId(int id);
}
