package com.example.bcsd;

public class UserResponseDTO {

    private String name;
    private String email;

    public UserResponseDTO(String name,String email)
    {
        this.name=name;
        this.email=email;
    }

    public String getName() {return this.name;}
    public String getEmail() {return this.email;}
}
