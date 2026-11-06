import { Link, useNavigate } from 'react-router-dom'
import { ROTULOS_PERFIL, useAuth } from '../context/AuthContext.jsx'

export default function Navbar() {
  const { usuario, autenticado, sair } = useAuth()
  const navigate = useNavigate()

  function aoSair() {
    sair()
    navigate('/login')
  }

  return (
    <header className="navbar">
      <div className="navbar-interno">
        <Link to="/" className="marca">Aluguel Carros</Link>

        {autenticado && (
          <nav className="navbar-acoes">
            <Link to="/clientes">Clientes</Link>\n            <Link to="/carros">Automóveis</Link>\n            <Link to="/pedidos">Pedidos</Link>
            <span className="usuario">
              {usuario.login} · {ROTULOS_PERFIL[usuario.perfil]}
            </span>
            <button type="button" className="btn btn-secundario" onClick={aoSair}>
              Sair
            </button>
          </nav>
        )}
      </div>
    </header>
  )
}
