import { Navigate, Route, Routes } from 'react-router-dom'
import Navbar from './components/Navbar.jsx'
import RotaProtegida from './components/RotaProtegida.jsx'
import Login from './pages/Login.jsx'
import Cadastro from './pages/Cadastro.jsx'
import Clientes from './pages/Clientes.jsx'
import ClienteForm from './pages/ClienteForm.jsx'
import Carros from './pages/Carros.jsx'
import Pedidos from './pages/Pedidos.jsx'
export default function App(){return <><Navbar/><main className="conteudo"><Routes>
<Route path="/login" element={<Login/>}/><Route path="/cadastro" element={<Cadastro/>}/>
<Route element={<RotaProtegida/>}><Route path="/clientes" element={<Clientes/>}/><Route path="/clientes/novo" element={<ClienteForm/>}/><Route path="/clientes/:id/editar" element={<ClienteForm/>}/><Route path="/carros" element={<Carros/>}/><Route path="/pedidos" element={<Pedidos/>}/></Route>
<Route path="*" element={<Navigate to="/clientes" replace/>}/></Routes></main></>}
