# Aluguel Carros

Sistema web para apoio à gestão de aluguéis de automóveis, desenvolvido no **Laboratório 02 de DIAW** (Desenvolvimento e Integração de Aplicações Web) – Engenharia de Software, PUC Minas.

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java (JDK 25) |
| Back-end | Spring Boot 4.0.x (arquitetura MVC) |
| Build | Apache Maven |
| Front-end | React + Vite |
| Banco de dados | Supabase (PostgreSQL) |
| Autenticação | Login obrigatório (usuário e senha) com token JWT |

## Estado atual do projeto

- [x] Autenticação (cadastro de usuário e login) para clientes e agentes (empresa/banco)
- [x] CRUD de cliente (RG, CPF, nome, endereço, profissão e até 3 rendimentos)
- [ ] Protótipo de pedido de aluguel (criar e visualizar status)
- [ ] Cadastro de automóveis e contratos de crédito
- [ ] Modelagem UML (casos de uso, histórias do usuário, classes e pacotes) em `docs/`

### Regras de acesso

- **Cliente**: cadastra, consulta, altera e exclui o **próprio** cadastro de cliente.
- **Empresa / Banco (agentes)**: apenas consultam os clientes cadastrados.
- Qualquer rota fora de `/api/auth/**` exige login.

## Arquitetura (MVC)

- **Model**: entidades JPA (`Usuario`, `Cliente`, `Rendimento`) e repositórios Spring Data.
- **View**: interface em React + Vite (`frontend/`).
- **Controller**: controllers REST do Spring (`AuthController`, `ClienteController`), com a regra de negócio na camada `service`.

```
Aluguel Carros/
├── backend/                  # Spring Boot (Maven)
│   └── src/main/java/com/aluguelcarros/
│       ├── controller/       # Controllers REST
│       ├── service/          # Regras de negócio
│       ├── repository/       # Acesso ao banco (Spring Data JPA)
│       ├── model/            # Entidades
│       ├── dto/              # Objetos de entrada/saída da API
│       ├── security/         # JWT (geração e filtro)
│       ├── config/           # Configuração do Spring Security e CORS
│       └── exception/        # Tratamento padronizado de erros
├── frontend/                 # React + Vite
│   └── src/
│       ├── pages/            # Login, Cadastro, Clientes, ClienteForm
│       ├── components/       # Navbar, RotaProtegida
│       └── context/          # AuthContext (sessão do usuário)
└── docs/                     # Diagramas UML
```

## Como executar

### Pré-requisitos

JDK 25, Maven, Node.js 20+ e um projeto criado no [Supabase](https://supabase.com).

### 1. Banco de dados (Supabase)

No painel do Supabase, abra **Connect** e copie os dados da conexão **Session pooler** (porta 5432). As tabelas são criadas automaticamente pelo Spring na primeira execução.

### 2. Back-end

```bash
cd backend
cp .env.example .env     # preencha com os dados do seu Supabase
mvn spring-boot:run
```

O servidor sobe em `http://localhost:8080`.

### 3. Front-end

```bash
cd frontend
npm install
npm run dev
```

Acesse `http://localhost:5173`. Crie uma conta (tipo **Cliente**) e cadastre seus dados em **Clientes**.

## Endpoints da API

| Método | Rota | Descrição | Acesso |
|---|---|---|---|
| POST | `/api/auth/register` | Cria usuário e retorna o token | Público |
| POST | `/api/auth/login` | Autentica e retorna o token | Público |
| GET | `/api/clientes` | Lista clientes (cliente vê só o seu) | Logado |
| GET | `/api/clientes/{id}` | Consulta um cliente | Logado |
| POST | `/api/clientes` | Cadastra o cliente do usuário logado | Cliente |
| PUT | `/api/clientes/{id}` | Altera o próprio cadastro | Cliente (dono) |
| DELETE | `/api/clientes/{id}` | Exclui o próprio cadastro | Cliente (dono) |

As requisições autenticadas usam o header `Authorization: Bearer <token>`.

## Equipe

- Nome(s) do(s) integrante(s)
