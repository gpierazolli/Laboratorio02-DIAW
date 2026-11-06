package com.aluguelcarros.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity @Table(name="pedidos_aluguel")
public class PedidoAluguel {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="cliente_id") private Cliente cliente;
 @ManyToOne(optional=false) @JoinColumn(name="carro_id") private Carro carro;
 @Column(nullable=false) private LocalDate dataRetirada;
 @Column(nullable=false) private LocalDate dataDevolucao;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private StatusPedido status=StatusPedido.PENDENTE;
 @Column(length=1000) private String observacao;
 @Column(length=1000) private String parecerAgente;
 @Column(nullable=false) private LocalDateTime criadoEm=LocalDateTime.now();
 public Long getId(){return id;} public Cliente getCliente(){return cliente;} public void setCliente(Cliente v){cliente=v;}
 public Carro getCarro(){return carro;} public void setCarro(Carro v){carro=v;}
 public LocalDate getDataRetirada(){return dataRetirada;} public void setDataRetirada(LocalDate v){dataRetirada=v;}
 public LocalDate getDataDevolucao(){return dataDevolucao;} public void setDataDevolucao(LocalDate v){dataDevolucao=v;}
 public StatusPedido getStatus(){return status;} public void setStatus(StatusPedido v){status=v;}
 public String getObservacao(){return observacao;} public void setObservacao(String v){observacao=v;}
 public String getParecerAgente(){return parecerAgente;} public void setParecerAgente(String v){parecerAgente=v;}
 public LocalDateTime getCriadoEm(){return criadoEm;}
}