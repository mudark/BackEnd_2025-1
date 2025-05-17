package com.example.bcsd;

public class User {

    private Integer id;
    private String name;
    private String email;
    private String password;

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
