package com.aluguelcarros.repository;
import com.aluguelcarros.model.Carro; import org.springframework.data.jpa.repository.JpaRepository;
public interface CarroRepository extends JpaRepository<Carro,Long> { boolean existsByMatricula(String matricula); }
