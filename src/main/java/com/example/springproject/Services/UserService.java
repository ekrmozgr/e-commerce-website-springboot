package com.example.springproject.Services;

import com.example.springproject.Entities.User;

import java.util.List;


public interface UserService {
    List<User>      getAllUsers();
    User            getUserByUname(String userName);

    User            addUser(User user);
    User            updateUser(User user);

    void            deleteUser(User user);
    void            deleteUserById(int id);
}
