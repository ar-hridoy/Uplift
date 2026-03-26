package com.uplift.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyLoginOtpDto {

    @NotBlank
    private String email;

    @NotBlank
    private String code;
}
