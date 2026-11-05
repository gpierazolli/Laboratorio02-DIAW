package com.aluguelcarros.dto;

import com.aluguelcarros.model.Perfil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Informe o usuário.")
        @Size(min = 3, max = 50, message = "O usuário deve ter entre 3 e 50 caracteres.")
        String login,

        @NotBlank(message = "Informe a senha.")
        @Size(min = 6, max = 72, message = "A senha deve ter entre 6 e 72 caracteres.")
        String senha,

        @NotNull(message = "Escolha o tipo de conta.")
        Perfil perfil
) { }
