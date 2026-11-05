import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api.js'
import { useAuth } from '../context/AuthContext.jsx'

function formatarCpf(cpf) {
  return cpf.replace(/^(\d{3})(\d{3})(\d{3})(\d{2})$/, '$1.$2.$3-$4')
}

const moeda = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

export default function Clientes() {
  const { ehAgente } = useAuth()
  const [clientes, setClientes] = useState([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState('')

  useEffect(() => {
    api('/api/clientes')
      .then(setClientes)
      .catch((err) => setErro(err.message))
      .finally(() => setCarregando(false))
  }, [])

  async function excluir(cliente) {
    if (!window.confirm(`Excluir o cadastro de ${cliente.nome}? Essa ação não pode ser desfeita.`)) return
    setErro('')
    try {
      await api(`/api/clientes/${cliente.id}`, { method: 'DELETE' })
      setClientes((atual) => atual.filter((c) => c.id !== cliente.id))
    } catch (err) {
      setErro(err.message)
    }
  }

  const podeCadastrar = !ehAgente && clientes.length === 0

  return (
    <section>
      <div className="cabecalho-pagina">
        <h1>Clientes</h1>
        {podeCadastrar && !carregando && (
          <Link to="/clientes/novo" className="btn">Cadastrar meus dados</Link>
        )}
      </div>

      {erro && <p className="alerta alerta-erro" role="alert">{erro}</p>}
      {carregando && <p className="texto-suave">Carregando...</p>}

      {!carregando && clientes.length === 0 && !erro && (
        <div className="cartao vazio">
          <p>
            {ehAgente
              ? 'Nenhum cliente cadastrado ainda.'
              : 'Você ainda não preencheu seus dados de cliente. Cadastre-os para poder fazer pedidos de aluguel.'}
          </p>
        </div>
      )}

      {clientes.length > 0 && (
        <div className="tabela-wrapper">
          <table>
            <thead>
              <tr>
                <th>Nome</th>
                <th>CPF</th>
                <th>RG</th>
                <th>Profissão</th>
                <th>Rendimentos</th>
                {!ehAgente && <th>Ações</th>}
              </tr>
            </thead>
            <tbody>
              {clientes.map((c) => (
                <tr key={c.id}>
                  <td>
                    {c.nome}
                    <div className="texto-suave pequeno">{c.endereco}</div>
                  </td>
                  <td>{formatarCpf(c.cpf)}</td>
                  <td>{c.rg}</td>
                  <td>{c.profissao}</td>
                  <td>
                    {c.rendimentos.length === 0 && <span className="texto-suave">Nenhum</span>}
                    {c.rendimentos.map((r, i) => (
                      <div key={i}>
                        {r.entidadeEmpregadora}: {moeda.format(r.valor)}
                      </div>
                    ))}
                  </td>
                  {!ehAgente && (
                    <td className="acoes">
                      <Link to={`/clientes/${c.id}/editar`} className="btn btn-secundario">Editar</Link>
                      <button type="button" className="btn btn-perigo" onClick={() => excluir(c)}>
                        Excluir
                      </button>
                    </td>
                  )}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  )
}
