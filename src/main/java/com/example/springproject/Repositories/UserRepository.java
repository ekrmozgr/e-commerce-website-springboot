package com.example.springproject.Repositories;

import com.example.springproject.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.ManagedBean;
import java.util.Optional;


@ManagedBean
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findUserByUname(String userName);
}
