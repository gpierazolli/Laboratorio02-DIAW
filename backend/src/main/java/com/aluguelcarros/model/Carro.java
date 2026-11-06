package com.aluguelcarros.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="carros")
public class Carro {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=20,unique=true) private String matricula;
 @Column(nullable=false) private Integer ano;
 @Column(nullable=false,length=60) private String marca;
 @Column(nullable=false,length=60) private String modelo;
 @Column(nullable=false,length=10) private String placa;
 @Column(nullable=false,precision=10,scale=2) private BigDecimal valorDiaria;
 @Column(nullable=false) private boolean disponivel=true;
 public Long getId(){return id;} public String getMatricula(){return matricula;} public void setMatricula(String v){matricula=v;}
 public Integer getAno(){return ano;} public void setAno(Integer v){ano=v;} public String getMarca(){return marca;} public void setMarca(String v){marca=v;}
 public String getModelo(){return modelo;} public void setModelo(String v){modelo=v;} public String getPlaca(){return placa;} public void setPlaca(String v){placa=v;}
 public BigDecimal getValorDiaria(){return valorDiaria;} public void setValorDiaria(BigDecimal v){valorDiaria=v;}
 public boolean isDisponivel(){return disponivel;} public void setDisponivel(boolean v){disponivel=v;}
}