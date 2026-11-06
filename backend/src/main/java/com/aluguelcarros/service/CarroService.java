package com.aluguelcarros.service;
import com.aluguelcarros.dto.CarroRequest; import com.aluguelcarros.model.*; import com.aluguelcarros.repository.CarroRepository;
import org.springframework.http.HttpStatus; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException; import java.util.List;
@Service public class CarroService {
 private final CarroRepository repo; public CarroService(CarroRepository repo){this.repo=repo;}
 public List<Carro> listar(){return repo.findAll();}
 @Transactional public Carro salvar(CarroRequest r,Long id){
  Carro c=id==null?new Carro():repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Automóvel não encontrado."));
  if(id==null && repo.existsByMatricula(r.matricula())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Matrícula já cadastrada.");
  c.setMatricula(r.matricula().trim());c.setAno(r.ano());c.setMarca(r.marca().trim());c.setModelo(r.modelo().trim());c.setPlaca(r.placa().trim());c.setValorDiaria(r.valorDiaria());c.setDisponivel(r.disponivel());return repo.save(c);
 }
 public void excluir(Long id){repo.delete(repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Automóvel não encontrado.")));}
}