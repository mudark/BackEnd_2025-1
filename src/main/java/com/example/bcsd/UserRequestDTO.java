package com.example.bcsd;

import jakarta.validation.constraints.NotBlank;

public class UserRequestDTO {
    @NotBlank(message = "회원 이름은 null 값이 불가능합니다.")
    private String name;
    @NotBlank(message = "회원 이메일은 null 값이 불가능합니다.")
    private String email;
    @NotBlank(message = "회원 비밀번호는 null 값이 불가능합니다.")
    private String password;

    public UserRequestDTO(
            String n,
            String e,
            String p
    ) {
        name=n;
        email=e;
        password=p;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
