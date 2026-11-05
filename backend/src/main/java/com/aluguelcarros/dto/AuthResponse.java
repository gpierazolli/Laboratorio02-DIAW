package com.aluguelcarros.dto;

import com.aluguelcarros.model.Perfil;

public record AuthResponse(String token, String login, Perfil perfil) { }
