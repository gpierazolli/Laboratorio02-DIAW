package com.aluguelcarros.service;

import com.aluguelcarros.dto.AuthResponse;
import com.aluguelcarros.dto.LoginRequest;
import com.aluguelcarros.dto.RegisterRequest;
import com.aluguelcarros.model.Usuario;
import com.aluguelcarros.repository.UsuarioRepository;
import com.aluguelcarros.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse registrar(RegisterRequest request) {
        String login = request.login().trim();
        if (usuarioRepository.existsByLogin(login)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esse usuário já existe. Escolha outro.");
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setPerfil(request.perfil());
        usuarioRepository.save(usuario);

        return resposta(usuario);
    }

    public AuthResponse login(LoginRequest request) {
        String login = request.login().trim();
        // Lança BadCredentialsException se usuário/senha estiverem errados
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, request.senha()));

        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos."));
        return resposta(usuario);
    }

    private AuthResponse resposta(Usuario usuario) {
        return new AuthResponse(jwtService.gerar(usuario), usuario.getLogin(), usuario.getPerfil());
    }
}
