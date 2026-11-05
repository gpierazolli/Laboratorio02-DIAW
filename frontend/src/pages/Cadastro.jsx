import { useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { ROTULOS_PERFIL, useAuth } from '../context/AuthContext.jsx'

export default function Cadastro() {
  const { autenticado, cadastrar } = useAuth()
  const navigate = useNavigate()
  const [login, setLogin] = useState('')
  const [senha, setSenha] = useState('')
  const [perfil, setPerfil] = useState('CLIENTE')
  const [erro, setErro] = useState('')
  const [enviando, setEnviando] = useState(false)

  if (autenticado) return <Navigate to="/clientes" replace />

  async function aoEnviar(e) {
    e.preventDefault()
    setErro('')
    setEnviando(true)
    try {
      await cadastrar(login.trim(), senha, perfil)
      navigate('/clientes')
    } catch (err) {
      setErro(err.message)
    } finally {
      setEnviando(false)
    }
  }

  return (
    <section className="cartao cartao-estreito">
      <h1>Criar conta</h1>
      <p className="texto-suave">O cadastro é necessário para usar o sistema.</p>

      <form onSubmit={aoEnviar} className="formulario">
        <label>
          Usuário
          <input
            value={login}
            onChange={(e) => setLogin(e.target.value)}
            minLength={3}
            maxLength={50}
            autoComplete="username"
            required
          />
        </label>
        <label>
          Senha
          <input
            type="password"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            minLength={6}
            autoComplete="new-password"
            required
          />
          <small>Mínimo de 6 caracteres.</small>
        </label>
        <label>
          Tipo de conta
          <select value={perfil} onChange={(e) => setPerfil(e.target.value)}>
            {Object.entries(ROTULOS_PERFIL).map(([valor, rotulo]) => (
              <option key={valor} value={valor}>{rotulo}</option>
            ))}
          </select>
        </label>

        {erro && <p className="alerta alerta-erro" role="alert">{erro}</p>}

        <button type="submit" className="btn" disabled={enviando}>
          {enviando ? 'Criando conta...' : 'Criar conta'}
        </button>
      </form>

      <p className="rodape-form">
        Já tem conta? <Link to="/login">Entrar</Link>
      </p>
    </section>
  )
}
