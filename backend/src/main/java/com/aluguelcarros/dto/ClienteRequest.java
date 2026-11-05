package com.aluguelcarros.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ClienteRequest(
        @NotBlank(message = "Informe o RG.")
        @Size(max = 20, message = "O RG deve ter no máximo 20 caracteres.")
        String rg,

        @NotBlank(message = "Informe o CPF.")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve ter 11 dígitos (somente números).")
        String cpf,

        @NotBlank(message = "Informe o nome.")
        String nome,

        @NotBlank(message = "Informe o endereço.")
        String endereco,

        @NotBlank(message = "Informe a profissão.")
        String profissao,

        @Valid
        @Size(max = 3, message = "São permitidos no máximo 3 rendimentos.")
        List<RendimentoDto> rendimentos
) { }
