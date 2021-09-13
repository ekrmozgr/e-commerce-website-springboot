package com.example.springproject.Repositories;

import com.example.springproject.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.ManagedBean;

@ManagedBean
public interface CategoryRepository extends JpaRepository<Category,Integer> {

}
