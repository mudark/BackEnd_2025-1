package com.example.bcsd;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="member")
public class User {

    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    public User() {}

    public User(Integer id, String name,String email, String password)
    {
        this.id=id;
        this.name=name;
        this.email=email;
        this.password=password;
    }

    public Integer getId() {return this.id;}
    public String getName() {return this.name;}
    public String getEmail() {return this.email;}
    public String getPassword() {return this.password;}
}
