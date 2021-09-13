package com.example.springproject.ManagedBeans;

import com.example.springproject.Entities.Basket;
import com.example.springproject.Entities.Role;
import com.example.springproject.Entities.User;
import com.example.springproject.Services.UserService;


import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.List;

@ManagedBean
@SessionScoped
public class UserBean {

    @ManagedProperty(value = "userService")
    public UserService      userService;

    private List<User>      users;

    private User            _usr;


    public UserBean(UserService userService) {
        this.userService = userService;
    }

    public User get_usr() {
        return _usr;
    }

    public void onload()
    {
        this.users = userService.getAllUsers();
    }

    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }

    public void deleteUser(User user)
    {
        userService.deleteUser(user);
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Success !",
                        "User Deleted"));
    }

    public void deleteUserById(int id)
    {
        userService.deleteUserById(id);
    }

    public String createUser()
    {
        _usr = new User();
        onload();
        return "/create_user.xhtml?faces-redirect=true";
    }
    public String save()
    {
        if(users.stream().anyMatch(u -> u.getUname().equals(_usr.getUname())))
        {
            FacesContext.getCurrentInstance().addMessage(
                    "createForm:uname",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Username already exist !",
                            "Username already exist !"));
            return null;
        }
        if(users.stream().anyMatch(u -> u.getPhoneNo().equals(_usr.getPhoneNo())))
        {
            FacesContext.getCurrentInstance().addMessage(
                    "createForm:phoneno",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Phone number already in use !",
                            "Phone number already in use !"));
            return null;
        }

        _usr.setRoles(Collections.singleton(Role.USER));
        _usr.setBasket(new Basket(0,null,_usr));
        this.userService.addUser(_usr);
        return "/user_settings.xhtml?faces-redirect=true";
    }

    public String editUser(User user)
    {
        this._usr = user; return "/edit_user.xhtml?faces-redirect=true";
    }

    public String updateUser()
    {
        if(users.stream().anyMatch(u -> u.getUname().equals(_usr.getUname())))
        {
            FacesContext.getCurrentInstance().addMessage(
                    "editUser:uname",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Username already exist !",
                            "Username already exist !"));
            return null;
        }
        if(users.stream().anyMatch(u -> u.getPhoneNo().equals(_usr.getPhoneNo())))
        {
            FacesContext.getCurrentInstance().addMessage(
                    "editUser:phoneno",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Phone number already in use !",
                            "Phone number already in use !"));
            return null;
        }
        userService.updateUser(this._usr); return "/user_settings.xhtml?faces-redirect=true";
    }

    public String updateUser(User user)
    {
        userService.updateUser(user); return "/account.xhtml?faces-redirect=true";
    }

}
