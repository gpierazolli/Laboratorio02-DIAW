// Cliente HTTP simples para falar com o back-end Spring Boot.
const BASE_URL = import.meta.env.VITE_API_URL ?? ''
const STORAGE_KEY = 'aluguel-carros:auth'

let aoExpirarSessao = null

export function definirAoExpirarSessao(fn) {
  aoExpirarSessao = fn
}

export function lerSessao() {
  try {
    return JSON.parse(localStorage.getItem(STORAGE_KEY))
  } catch {
    return null
  }
}

export function salvarSessao(sessao) {
  if (sessao) localStorage.setItem(STORAGE_KEY, JSON.stringify(sessao))
  else localStorage.removeItem(STORAGE_KEY)
}

export class ApiError extends Error {
  constructor(message, status) {
    super(message)
    this.status = status
  }
}

export async function api(caminho, { method = 'GET', body } = {}) {
  const sessao = lerSessao()
  const headers = {}
  if (body !== undefined) headers['Content-Type'] = 'application/json'
  if (sessao?.token) headers.Authorization = `Bearer ${sessao.token}`

  let resposta
  try {
    resposta = await fetch(`${BASE_URL}${caminho}`, {
      method,
      headers,
      body: body !== undefined ? JSON.stringify(body) : undefined,
    })
  } catch {
    throw new ApiError('Não foi possível conectar ao servidor. Verifique se o back-end está rodando.', 0)
  }

  if (resposta.status === 204) return null

  let dados = null
  try {
    dados = await resposta.json()
  } catch {
    // resposta sem corpo JSON
  }

  if (!resposta.ok) {
    // 401 com token salvo = sessão expirada (no login, 401 significa senha errada)
    if (resposta.status === 401 && sessao?.token && aoExpirarSessao) aoExpirarSessao()
    throw new ApiError(dados?.message || 'Algo deu errado. Tente novamente.', resposta.status)
  }

  return dados
}
