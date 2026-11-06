import { useEffect, useState } from 'react'
import { api } from '../api.js'
import { useAuth } from '../context/AuthContext.jsx'
const vazio={matricula:'',ano:new Date().getFullYear(),marca:'',modelo:'',placa:'',valorDiaria:'',disponivel:true}
export default function Carros(){
 const {ehAgente}=useAuth(); const [carros,setCarros]=useState([]);const [form,setForm]=useState(vazio);const [erro,setErro]=useState('');const [editId,setEditId]=useState(null)
 async function carregar(){try{setCarros(await api('/api/carros'))}catch(e){setErro(e.message)}}
 useEffect(()=>{carregar()},[])
 function campo(k,v){setForm(f=>({...f,[k]:v}))}
 async function salvar(e){e.preventDefault();setErro('');try{await api(editId?`/api/carros/${editId}`:'/api/carros',{method:editId?'PUT':'POST',body:{...form,ano:Number(form.ano),valorDiaria:Number(form.valorDiaria)}});setForm(vazio);setEditId(null);carregar()}catch(e){setErro(e.message)}}
 async function excluir(id){if(!confirm('Excluir este automóvel?'))return;try{await api(`/api/carros/${id}`,{method:'DELETE'});carregar()}catch(e){setErro(e.message)}}
 return <section><div className="cabecalho-pagina"><h1>Frota de automóveis</h1></div>{erro&&<p className="alerta alerta-erro">{erro}</p>}
 {ehAgente&&<form className="cartao formulario" onSubmit={salvar}><h2>{editId?'Editar automóvel':'Cadastrar automóvel'}</h2><div className="linha">{[['matricula','Matrícula'],['ano','Ano'],['marca','Marca'],['modelo','Modelo'],['placa','Placa'],['valorDiaria','Diária (R$)']].map(([k,l])=><label key={k}>{l}<input required value={form[k]} type={k==='ano'||k==='valorDiaria'?'number':'text'} onChange={e=>campo(k,e.target.value)}/></label>)}</div><label className="linha-check"><input type="checkbox" checked={form.disponivel} onChange={e=>campo('disponivel',e.target.checked)}/> Disponível para aluguel</label><div className="botoes"><button className="btn">Salvar</button><button type="button" className="btn btn-secundario" onClick={()=>{setForm(vazio);setEditId(null)}}>Limpar</button></div></form>}
 <div className="grade-carros">{carros.map(c=><article className="cartao carro" key={c.id}><span className={c.disponivel?'status status-ok':'status'}>{c.disponivel?'Disponível':'Indisponível'}</span><h2>{c.marca} {c.modelo}</h2><p>{c.ano} · Placa {c.placa} · Matrícula {c.matricula}</p><strong>{Number(c.valorDiaria).toLocaleString('pt-BR',{style:'currency',currency:'BRL'})} / diária</strong>{ehAgente&&<div className="botoes"><button className="btn btn-secundario" onClick={()=>{setForm({...c,valorDiaria:String(c.valorDiaria)});setEditId(c.id)}}>Editar</button><button className="btn btn-perigo" onClick={()=>excluir(c.id)}>Excluir</button></div>}</article>)}</div>{!carros.length&&<p className="cartao vazio">Nenhum automóvel cadastrado.</p>}</section>
}
