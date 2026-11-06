package com.aluguelcarros.repository;
import com.aluguelcarros.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface PedidoAluguelRepository extends JpaRepository<PedidoAluguel,Long> {
 List<PedidoAluguel> findByClienteIdOrderByCriadoEmDesc(Long clienteId);
}