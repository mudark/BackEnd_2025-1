package com.example.bcsd;

import jakarta.validation.constraints.NotBlank;

public class BoardRequestDTO {

    @NotBlank
    private String name;

    public BoardRequestDTO() {}

    public BoardRequestDTO(String n){
        name=n;
    }
    public String getName() {
        return name;
    }
}
