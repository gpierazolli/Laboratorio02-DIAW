package com.aluguelcarros.dto;
import jakarta.validation.constraints.*; import java.time.LocalDate;
public record PedidoRequest(@NotNull Long carroId,@NotNull @FutureOrPresent LocalDate dataRetirada,
 @NotNull @Future LocalDate dataDevolucao,@Size(max=1000) String observacao) {}
