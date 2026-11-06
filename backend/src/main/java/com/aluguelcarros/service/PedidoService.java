package com.aluguelcarros.service;
import com.aluguelcarros.dto.*; import com.aluguelcarros.model.*; import com.aluguelcarros.repository.*;
import org.springframework.http.HttpStatus; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import org.springframework.web.server.ResponseStatusException;
import java.util.*;
@Service public class PedidoService {
 private final PedidoAluguelRepository pedidos; private final ClienteRepository clientes; private final CarroRepository carros; private final UsuarioRepository usuarios;
 public PedidoService(PedidoAluguelRepository p,ClienteRepository c,CarroRepository a,UsuarioRepository u){pedidos=p;clientes=c;carros=a;usuarios=u;}
 public List<Map<String,Object>> listar(String login){Usuario u=usuario(login);List<PedidoAluguel> lista=u.getPerfil()==Perfil.CLIENTE?pedidos.findByClienteIdOrderByCriadoEmDesc(clientes.findByUsuarioId(u.getId()).map(Cliente::getId).orElse(-1L)):pedidos.findAll();return lista.stream().map(this::dto).toList();}
 @Transactional public Map<String,Object> criar(PedidoRequest r,String login){
  Usuario u=usuario(login);if(u.getPerfil()!=Perfil.CLIENTE) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Somente clientes podem criar pedidos.");
  Cliente c=clientes.findByUsuarioId(u.getId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cadastre seus dados de cliente antes de solicitar um aluguel."));
  if(!r.dataDevolucao().isAfter(r.dataRetirada())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"A devolução deve ocorrer depois da retirada.");
  Carro carro=carros.findById(r.carroId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Automóvel não encontrado."));
  if(!carro.isDisponivel()) throw new ResponseStatusException(HttpStatus.CONFLICT,"Este automóvel não está disponível.");
  PedidoAluguel p=new PedidoAluguel();p.setCliente(c);p.setCarro(carro);p.setDataRetirada(r.dataRetirada());p.setDataDevolucao(r.dataDevolucao());p.setObservacao(r.observacao());return dto(pedidos.save(p));
 }
 @Transactional public Map<String,Object> atualizarStatus(Long id,PedidoStatusRequest r,String login){
  Usuario u=usuario(login);if(u.getPerfil()==Perfil.CLIENTE)throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Apenas agentes podem avaliar pedidos.");
  PedidoAluguel p=pedidos.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encontrado."));
  if(p.getStatus()==StatusPedido.CANCELADO)throw new ResponseStatusException(HttpStatus.CONFLICT,"Pedido cancelado não pode ser alterado.");
  p.setStatus(r.status());p.setParecerAgente(r.parecerAgente());return dto(pedidos.save(p));
 }
 @Transactional public void cancelar(Long id,String login){
  Usuario u=usuario(login);PedidoAluguel p=pedidos.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encontrado."));
  if(u.getPerfil()!=Perfil.CLIENTE||!p.getCliente().getUsuario().getId().equals(u.getId()))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Você só pode cancelar seus próprios pedidos.");
  if(p.getStatus()==StatusPedido.APROVADO)throw new ResponseStatusException(HttpStatus.CONFLICT,"Pedidos aprovados devem ser cancelados diretamente com o agente.");
  if(p.getStatus()==StatusPedido.REJEITADO||p.getStatus()==StatusPedido.CANCELADO)throw new ResponseStatusException(HttpStatus.CONFLICT,"Este pedido não pode ser cancelado.");
  p.setStatus(StatusPedido.CANCELADO);pedidos.save(p);
 }
 private Usuario usuario(String l){return usuarios.findByLogin(l).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Usuário não encontrado."));}
 private Map<String,Object> dto(PedidoAluguel p){Map<String,Object> m=new LinkedHashMap<>();m.put("id",p.getId());m.put("cliente",p.getCliente().getNome());m.put("clienteId",p.getCliente().getId());m.put("carroId",p.getCarro().getId());m.put("carro",p.getCarro().getMarca()+" "+p.getCarro().getModelo());m.put("placa",p.getCarro().getPlaca());m.put("valorDiaria",p.getCarro().getValorDiaria());m.put("dataRetirada",p.getDataRetirada());m.put("dataDevolucao",p.getDataDevolucao());m.put("status",p.getStatus());m.put("observacao",p.getObservacao());m.put("parecerAgente",p.getParecerAgente());m.put("criadoEm",p.getCriadoEm());return m;}
}