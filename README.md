# 🥋 Sistema de Gestão de Academia (Artes Marciais)

Sistema completo em Java para gerenciamento de alunos de artes marciais (Jiu-Jitsu e Muay Thai), com suporte a persistência de dados em SQLite, aplicação de Orientação a Objetos (Herança e Polimorfismo) e monitoramento em tempo real via Threads.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Banco de Dados:** SQLite (via JDBC)
* **IDE:** IntelliJ IDEA
* **Versionamento:** Git & GitHub

---

## 🚀 Funcionalidades

- **CRUD Completo:** Cadastro, listagem, atualização e exclusão de alunos salvos diretamente no arquivo `academia.db`.
- **Graduação Unificada (Pattern Matching):** Identificação automática do tipo de aluno para aplicação da regra de negócio específica da modalidade:
    - **Jiu-Jitsu:** Controle de faixas e acréscimo automático de graus (0 a 4).
    - **Muay Thai:** Atualização e controle de cor das cordas de braço (*Prajied*).
- **Polimorfismo:** Apresentação diferenciada de fichas técnicas para cada modalidade.
- **Processamento Paralelo (Thread):** `MonitorStatusThread` rodando em background (Daemon) para monitorar métricas do banco em tempo real.
- **Tratamento de Exceções:** Validações customizadas (`DadosAlunoInvalidosException`) para proteção das regras de negócio e integridade do banco.

---

## 📂 Estrutura do Projeto

```text
src/
└── br/com/gestaoacademia/
    ├── database/     # Classe ConexaoBanco (JDBC e CRUD SQLite)
    ├── excecoes/     # Exceções personalizadas de validação
    ├── modelo/       # Classes Aluno (base), AlunoJiuJitsu e AlunoMuayThai
    ├── service/      # MonitorStatusThread (Execução em background)
    └── Main.java     # Interface de console, menu dinâmico e loop principal
