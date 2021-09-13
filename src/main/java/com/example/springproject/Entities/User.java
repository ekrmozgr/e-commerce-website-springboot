package com.example.springproject.Entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="usr_table")
@SequenceGenerator(name = "seq")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "id")
    private int id;

    @Column(name = "firstname",nullable = false)
    @NotNull(message = "Name Cannot be Null")
    @Size(min = 3,max = 50,message = "Name Must be Min 3 - Max 50")
    private String firstName;

    @Column(name = "lastname",nullable = false)
    @NotNull(message = "Lastname Cannot be Null")
    @Size(min = 3,max = 50,message = "Lastname Must be Min 3 - Max 50")
    private String lastName;

    @Column(name = "adress",nullable = false)
    @NotNull(message = "Address Cannot be Null")
    @Size(max = 150, message = "Address Must be Max 150")
    private String adress;

    @Column(name = "phoneno",nullable = false,unique = true)
    @NotNull(message = "Phone Number Cannot be Null")
    @Pattern(regexp = "^5(0[5-7]|[3-5]\\d)\\d{3}\\d{4}$", message = "Don't Use Spaces - Number Must be 10 Digits")
    private String phoneNo;

    @Column(name = "password",nullable = false)
    @NotNull(message = "Password Cannot be Null")
    @Size(min = 5, max = 20,message = "Password Must be Min 5 - Max 20")
    private String password;

    @Column(name = "uname",nullable = false,unique = true)
    @NotNull(message = "Username Cannot be Null")
    @Size(min = 5, max = 20,message = "Username Must be Min 5 - Max 20")
    private String uname;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "usr_role", joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "user_id")
    private List<Order> orders;

    public List<Order> getOrders() {return orders;}
    public void setOrders(List<Order> orders) {this.orders = orders;}

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Basket basket;
    public Basket getBasket() {return basket;}
    public void setBasket(Basket basket) {this.basket = basket;}

    public boolean isAdmin()
    {
        return roles.contains(Role.ADMIN);
    }
    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public User(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public User(String firstName, String lastName, String adress, String phoneNo, String password, String uname, Set<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.adress = adress;
        this.phoneNo = phoneNo;
        this.password = password;
        this.uname = uname;
        this.roles = roles;
    }
}
