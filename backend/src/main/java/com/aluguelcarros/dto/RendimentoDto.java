package com.aluguelcarros.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RendimentoDto(
        @NotBlank(message = "Informe a entidade empregadora.")
        String entidadeEmpregadora,

        @NotNull(message = "Informe o valor do rendimento.")
        @DecimalMin(value = "0.0", message = "O rendimento não pode ser negativo.")
        BigDecimal valor
) { }
