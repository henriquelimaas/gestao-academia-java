# 🥋 Sistema de Gestão de Academia (Artes Marciais)

Sistema completo em Java para gerenciamento de alunos de artes marciais (Jiu-Jitsu e Muay Thai), com suporte a persistência de dados real em SQLite, aplicação de Orientação a Objetos (Herança e Polimorfismo) e monitoramento em tempo real via Threads.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 17+
* **Banco de Dados:** SQLite (via JDBC)
* **IDE:** IntelliJ IDEA
* **Versionamento:** Git & GitHub

---

## 🚀 Funcionalidades

- **CRUD Completo:** Cadastro, listagem, atualização e exclusão de alunos salvos diretamente no arquivo `academia.db`.
- **Polimorfismo:** Diferenciação de comportamentos e regras de negócio entre modalidades:
    - **Jiu-Jitsu:** Controle de faixas, graus e método de graduação.
    - **Muay Thai:** Controle de cordas de braço (*Prajied*).
- **Processamento Paralelo (Thread):** `MonitorStatusThread` rodando em background (Daemon) que verifica o total de alunos cadastrados no banco em intervalos regulares.
- **Tratamento de Exceções:** Validações customizadas para regras de negócio e integridade de dados.

---

## 📂 Estrutura do Projeto

```text
src/
└── br/com/gestaoacademia/
    ├── database/     # Classe ConexaoBanco (JDBC e CRUD SQLite)
    ├── excecao/      # Exceções personalizadas de validação
    ├── modelo/       # Classes Aluno (base), AlunoJiuJitsu e AlunoMuayThai
    ├── service/      # MonitorStatusThread (Execução em background)
    └── Main.java     # Interface via console e loop de execução