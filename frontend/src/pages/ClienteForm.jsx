import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { api } from '../api.js'
import { useAuth } from '../context/AuthContext.jsx'

const MAX_RENDIMENTOS = 3

const VAZIO = { rg: '', cpf: '', nome: '', endereco: '', profissao: '' }

export default function ClienteForm() {
  const { id } = useParams()
  const editando = Boolean(id)
  const { ehAgente } = useAuth()
  const navigate = useNavigate()

  const [dados, setDados] = useState(VAZIO)
  const [rendimentos, setRendimentos] = useState([])
  const [carregando, setCarregando] = useState(editando)
  const [enviando, setEnviando] = useState(false)
  const [erro, setErro] = useState('')

  useEffect(() => {
    if (!editando) return
    api(`/api/clientes/${id}`)
      .then((c) => {
        setDados({ rg: c.rg, cpf: c.cpf, nome: c.nome, endereco: c.endereco, profissao: c.profissao })
        setRendimentos(c.rendimentos.map((r) => ({ entidadeEmpregadora: r.entidadeEmpregadora, valor: String(r.valor) })))
      })
      .catch((err) => setErro(err.message))
      .finally(() => setCarregando(false))
  }, [editando, id])

  if (ehAgente) {
    return (
      <section className="cartao">
        <p>Somente clientes podem cadastrar ou alterar os próprios dados.</p>
        <Link to="/clientes" className="btn btn-secundario">Voltar</Link>
      </section>
    )
  }

  function alterar(campo, valor) {
    setDados((atual) => ({ ...atual, [campo]: valor }))
  }

  function alterarRendimento(indice, campo, valor) {
    setRendimentos((atual) => atual.map((r, i) => (i === indice ? { ...r, [campo]: valor } : r)))
  }

  function adicionarRendimento() {
    setRendimentos((atual) => [...atual, { entidadeEmpregadora: '', valor: '' }])
  }

  function removerRendimento(indice) {
    setRendimentos((atual) => atual.filter((_, i) => i !== indice))
  }

  async function aoEnviar(e) {
    e.preventDefault()
    setErro('')
    setEnviando(true)

    const corpo = {
      ...dados,
      rendimentos: rendimentos.map((r) => ({
        entidadeEmpregadora: r.entidadeEmpregadora,
        valor: r.valor === '' ? null : Number(r.valor),
      })),
    }

    try {
      if (editando) await api(`/api/clientes/${id}`, { method: 'PUT', body: corpo })
      else await api('/api/clientes', { method: 'POST', body: corpo })
      navigate('/clientes')
    } catch (err) {
      setErro(err.message)
    } finally {
      setEnviando(false)
    }
  }

  if (carregando) return <p className="texto-suave">Carregando...</p>

  return (
    <section className="cartao">
      <h1>{editando ? 'Editar cliente' : 'Cadastrar cliente'}</h1>

      <form onSubmit={aoEnviar} className="formulario">
        <label>
          Nome completo
          <input value={dados.nome} onChange={(e) => alterar('nome', e.target.value)} required />
        </label>

        <div className="linha">
          <label>
            CPF
            <input
              value={dados.cpf}
              onChange={(e) => alterar('cpf', e.target.value.replace(/\D/g, '').slice(0, 11))}
              inputMode="numeric"
              placeholder="Somente números"
              pattern="\d{11}"
              title="Informe os 11 dígitos do CPF"
              required
            />
          </label>
          <label>
            RG
            <input value={dados.rg} onChange={(e) => alterar('rg', e.target.value)} maxLength={20} required />
          </label>
        </div>

        <label>
          Endereço
          <input value={dados.endereco} onChange={(e) => alterar('endereco', e.target.value)} required />
        </label>

        <label>
          Profissão
          <input value={dados.profissao} onChange={(e) => alterar('profissao', e.target.value)} required />
        </label>

        <fieldset>
          <legend>Rendimentos (até {MAX_RENDIMENTOS})</legend>

          {rendimentos.length === 0 && (
            <p className="texto-suave">Nenhum rendimento informado.</p>
          )}

          {rendimentos.map((r, i) => (
            <div className="linha linha-rendimento" key={i}>
              <label>
                Entidade empregadora
                <input
                  value={r.entidadeEmpregadora}
                  onChange={(e) => alterarRendimento(i, 'entidadeEmpregadora', e.target.value)}
                  required
                />
              </label>
              <label>
                Valor (R$)
                <input
                  type="number"
                  min="0"
                  step="0.01"
                  value={r.valor}
                  onChange={(e) => alterarRendimento(i, 'valor', e.target.value)}
                  required
                />
              </label>
              <button type="button" className="btn btn-secundario" onClick={() => removerRendimento(i)}>
                Remover
              </button>
            </div>
          ))}

          <button
            type="button"
            className="btn btn-secundario"
            onClick={adicionarRendimento}
            disabled={rendimentos.length >= MAX_RENDIMENTOS}
          >
            Adicionar rendimento
          </button>
        </fieldset>

        {erro && <p className="alerta alerta-erro" role="alert">{erro}</p>}

        <div className="botoes">
          <button type="submit" className="btn" disabled={enviando}>
            {enviando ? 'Salvando...' : 'Salvar'}
          </button>
          <Link to="/clientes" className="btn btn-secundario">Cancelar</Link>
        </div>
      </form>
    </section>
  )
}
