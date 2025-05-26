package com.example.bcsd;

import jakarta.validation.constraints.NotBlank;

public class BoardDTO {

    @NotBlank
    private String name;

    public BoardDTO() {}

    public BoardDTO(String n){
        name=n;
    }
    public String getName() {
        return name;
    }
}
