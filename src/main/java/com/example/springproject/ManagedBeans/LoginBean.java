package com.example.springproject.ManagedBeans;

import com.example.springproject.Entities.User;
import com.example.springproject.Services.UserService;
import com.example.springproject.SessionUtils;


import javax.faces.application.FacesMessage;
import javax.annotation.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.List;

@ManagedBean
@SessionScoped
public class LoginBean {

    @ManagedProperty(value = "userService")
    public UserService          userService;

    private List<User>          users;
    private User                _usr;
    private String              uname;
    private String              pwd;
    private boolean             loggedIn;


    public User get_usr() {
        return _usr;
    }
    public void set_usr(User _usr) {
        this._usr = _usr;
    }
    public boolean isLoggedIn() {return loggedIn;}
    public void setLoggedIn(boolean loggedIn) {this.loggedIn = loggedIn;}
    public String getUname() {
        return uname;
    }
    public void setUname(String uname) {
        this.uname = uname;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    public LoginBean(UserService userService) {
        this.userService = userService;
    }

    public String loginCheckLoggedIn()
    {
        if(loggedIn)
        {
            return "/index.xhtml?faces-redirect";
        }
        pwd = null;
        uname = null;
        return null;
    }

    public void onload()
    {
        _usr = userService.getUserByUname((String)SessionUtils.getSession().getAttribute("username"));
    }

    public String doLogin()
    {
        users = userService.getAllUsers();
        boolean valid = users.stream().anyMatch(u -> u.getUname().equals(this.uname) && u.getPassword().equals(this.pwd));
        if(valid)
        {
            loggedIn = true;
            _usr = userService.getUserByUname(uname);
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("username", _usr.getUname());
            session.setAttribute("user_id",_usr.getId());
            session.setAttribute("isAdmin",_usr.isAdmin());
            return "/index.xhtml?faces-redirect=true";
        }
        else
        {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Incorrect Username and Password",
                            "Please enter correct username and Password"));
            return null;
        }
    }

    public String doLogout() {
        loggedIn = false;
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        _usr = null;
        pwd = null;
        uname = null;
        return "/index.xhtml?faces-redirect=true";
    }

}
