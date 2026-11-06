package com.aluguelcarros.dto;
import com.aluguelcarros.model.StatusPedido; import jakarta.validation.constraints.NotNull; import jakarta.validation.constraints.Size;
public record PedidoStatusRequest(@NotNull StatusPedido status,@Size(max=1000) String parecerAgente) {}
