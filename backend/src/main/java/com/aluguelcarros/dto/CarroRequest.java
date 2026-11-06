package com.aluguelcarros.dto;
import jakarta.validation.constraints.*; import java.math.BigDecimal;
public record CarroRequest(@NotBlank String matricula,@NotNull @Min(1950) @Max(2100) Integer ano,
 @NotBlank String marca,@NotBlank String modelo,@NotBlank String placa,
 @NotNull @DecimalMin("0.01") BigDecimal valorDiaria,@NotNull Boolean disponivel) {}
