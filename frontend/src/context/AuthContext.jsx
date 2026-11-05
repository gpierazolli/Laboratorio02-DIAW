import { createContext, useContext, useEffect, useState } from 'react'
import { api, definirAoExpirarSessao, lerSessao, salvarSessao } from '../api.js'

const AuthContext = createContext(null)

export const ROTULOS_PERFIL = {
  CLIENTE: 'Cliente',
  EMPRESA: 'Empresa (agente)',
  BANCO: 'Banco (agente)',
}

export function AuthProvider({ children }) {
  const [sessao, setSessao] = useState(() => lerSessao())

  function atualizar(nova) {
    salvarSessao(nova)
    setSessao(nova)
  }

  async function entrar(login, senha) {
    atualizar(await api('/api/auth/login', { method: 'POST', body: { login, senha } }))
  }

  async function cadastrar(login, senha, perfil) {
    atualizar(await api('/api/auth/register', { method: 'POST', body: { login, senha, perfil } }))
  }

  function sair() {
    atualizar(null)
  }

  useEffect(() => {
    definirAoExpirarSessao(() => atualizar(null))
  }, [])

  const valor = {
    usuario: sessao ? { login: sessao.login, perfil: sessao.perfil } : null,
    autenticado: Boolean(sessao?.token),
    ehAgente: sessao ? sessao.perfil !== 'CLIENTE' : false,
    entrar,
    cadastrar,
    sair,
  }

  return <AuthContext.Provider value={valor}>{children}</AuthContext.Provider>
}

export function useAuth() {
  return useContext(AuthContext)
}
