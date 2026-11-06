package com.aluguelcarros.controller;
import com.aluguelcarros.dto.CarroRequest;import com.aluguelcarros.model.*;import com.aluguelcarros.service.CarroService;
import jakarta.validation.Valid;import org.springframework.http.HttpStatus;import org.springframework.security.core.Authentication;import org.springframework.web.bind.annotation.*;import org.springframework.web.server.ResponseStatusException;import java.util.List;
@RestController @RequestMapping("/api/carros") public class CarroController {
 private final CarroService service;public CarroController(CarroService s){service=s;}
 @GetMapping public List<Carro> listar(){return service.listar();}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Carro criar(@Valid @RequestBody CarroRequest r,Authentication a){exigirAgente(a);return service.salvar(r,null);}
 @PutMapping("/{id}") public Carro atualizar(@PathVariable Long id,@Valid @RequestBody CarroRequest r,Authentication a){exigirAgente(a);return service.salvar(r,id);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id,Authentication a){exigirAgente(a);service.excluir(id);}
 private void exigirAgente(Authentication a){if(a.getAuthorities().stream().noneMatch(x->x.getAuthority().equals("ROLE_EMPRESA")||x.getAuthority().equals("ROLE_BANCO")))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Apenas agentes podem gerenciar automóveis.");}
}