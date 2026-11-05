package com.aluguelcarros.service;

import com.aluguelcarros.dto.ClienteRequest;
import com.aluguelcarros.dto.ClienteResponse;
import com.aluguelcarros.dto.RendimentoDto;
import com.aluguelcarros.model.Cliente;
import com.aluguelcarros.model.Perfil;
import com.aluguelcarros.model.Rendimento;
import com.aluguelcarros.model.Usuario;
import com.aluguelcarros.repository.ClienteRepository;
import com.aluguelcarros.repository.UsuarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Regras do CRUD de cliente:
 * - CLIENTE: cadastra, consulta, altera e exclui apenas o próprio cadastro.
 * - EMPRESA/BANCO (agentes): só consultam os clientes.
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    public ClienteService(ClienteRepository clienteRepository, UsuarioRepository usuarioRepository) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar(String login) {
        Usuario usuario = buscarUsuario(login);
        List<Cliente> clientes = usuario.getPerfil() == Perfil.CLIENTE
                ? clienteRepository.findByUsuarioId(usuario.getId()).stream().toList()
                : clienteRepository.findAll(Sort.by("nome"));
        return clientes.stream().map(this::paraResposta).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscar(Long id, String login) {
        Usuario usuario = buscarUsuario(login);
        Cliente cliente = buscarCliente(id);
        if (usuario.getPerfil() == Perfil.CLIENTE) {
            exigirDono(cliente, usuario);
        }
        return paraResposta(cliente);
    }

    @Transactional
    public ClienteResponse criar(ClienteRequest request, String login) {
        Usuario usuario = buscarUsuario(login);
        if (usuario.getPerfil() != Perfil.CLIENTE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas clientes podem cadastrar os próprios dados.");
        }
        if (clienteRepository.existsByUsuarioId(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Você já possui um cadastro de cliente.");
        }
        if (clienteRepository.existsByCpf(request.cpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um cliente com esse CPF.");
        }

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        preencher(cliente, request);
        return paraResposta(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponse atualizar(Long id, ClienteRequest request, String login) {
        Usuario usuario = buscarUsuario(login);
        Cliente cliente = buscarCliente(id);
        exigirDono(cliente, usuario);

        if (!cliente.getCpf().equals(request.cpf()) && clienteRepository.existsByCpf(request.cpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um cliente com esse CPF.");
        }

        preencher(cliente, request);
        return paraResposta(clienteRepository.save(cliente));
    }

    @Transactional
    public void excluir(Long id, String login) {
        Usuario usuario = buscarUsuario(login);
        Cliente cliente = buscarCliente(id);
        exigirDono(cliente, usuario);
        clienteRepository.delete(cliente);
    }

    // ---------- auxiliares ----------

    private Usuario buscarUsuario(String login) {
        return usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado."));
    }

    private Cliente buscarCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
    }

    private void exigirDono(Cliente cliente, Usuario usuario) {
        boolean dono = usuario.getPerfil() == Perfil.CLIENTE
                && cliente.getUsuario().getId().equals(usuario.getId());
        if (!dono) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar este cadastro.");
        }
    }

    private void preencher(Cliente cliente, ClienteRequest request) {
        cliente.setRg(request.rg().trim());
        cliente.setCpf(request.cpf());
        cliente.setNome(request.nome().trim());
        cliente.setEndereco(request.endereco().trim());
        cliente.setProfissao(request.profissao().trim());

        cliente.getRendimentos().clear();
        if (request.rendimentos() != null) {
            for (RendimentoDto r : request.rendimentos()) {
                cliente.getRendimentos().add(new Rendimento(r.entidadeEmpregadora().trim(), r.valor()));
            }
        }
    }

    private ClienteResponse paraResposta(Cliente c) {
        List<RendimentoDto> rendimentos = c.getRendimentos().stream()
                .map(r -> new RendimentoDto(r.getEntidadeEmpregadora(), r.getValor()))
                .toList();
        return new ClienteResponse(c.getId(), c.getRg(), c.getCpf(), c.getNome(),
                c.getEndereco(), c.getProfissao(), rendimentos);
    }
}
