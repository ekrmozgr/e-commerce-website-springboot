package com.example.springproject.ServicesImpl;

import com.example.springproject.Entities.Basket;
import com.example.springproject.Entities.Role;
import com.example.springproject.Entities.User;
import com.example.springproject.Repositories.UserRepository;
import com.example.springproject.Services.UserService;
import org.springframework.stereotype.Service;

import javax.faces.bean.ManagedProperty;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @ManagedProperty(value="userRepository")
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        if(!userRepository.findAll().isEmpty())
            return;
        User user = new User("ekrem","ozgur","adres","5076275287","12345","ekrem", Collections.singleton(Role.ADMIN));
        user.setBasket(new Basket(0,null,user));
        addUser(user);
        user = new User("mesut","cty","adres","5364999191","12345","mesut",Collections.singleton(Role.ADMIN));
        user.setBasket(new Basket(0,null,user));
        addUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User addUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User getUserByUname(String userName) {
        return userRepository.findUserByUname(userName).get();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);userRepository.flush();
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);userRepository.flush();
    }

}
