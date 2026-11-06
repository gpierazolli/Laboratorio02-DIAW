package com.aluguelcarros.controller;
import com.aluguelcarros.dto.*;import com.aluguelcarros.service.PedidoService;import jakarta.validation.Valid;import org.springframework.http.HttpStatus;import org.springframework.security.core.Authentication;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/pedidos") public class PedidoController {
 private final PedidoService service;public PedidoController(PedidoService s){service=s;}
 @GetMapping public List<Map<String,Object>> listar(Authentication a){return service.listar(a.getName());}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Map<String,Object> criar(@Valid @RequestBody PedidoRequest r,Authentication a){return service.criar(r,a.getName());}
 @PatchMapping("/{id}/status") public Map<String,Object> status(@PathVariable Long id,@Valid @RequestBody PedidoStatusRequest r,Authentication a){return service.atualizarStatus(id,r,a.getName());}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void cancelar(@PathVariable Long id,Authentication a){service.cancelar(id,a.getName());}
}