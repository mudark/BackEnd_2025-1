package com.example.bcsd;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO
{
    @Getter
    @NotBlank(message = "회원 id를 입력하세요.")
    private String id;

    @Getter
    @NotBlank(message = "회원 비밀번호를 입력하세요.")
    private String password;
}
