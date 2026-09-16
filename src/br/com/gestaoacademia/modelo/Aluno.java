package br.com.gestaoacademia.modelo;

public class Aluno {

    // 1 - Atributos
    private Integer id;
    private String nome;
    private boolean ativo;

    // 2 - Construtores
    public Aluno() {
    }

    public Aluno(Integer id, String nome, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
    }

    public Aluno(String nome) {
        this.nome = nome;
        this.ativo = true;
    }

    // 3 - Método Polimórfico
    public String exibirFichaTecnica() {
        return String.format("[ID: %d][%s] %s - Status: %s",
                id, getClass().getSimpleName().toUpperCase(), nome, (ativo ? "ATIVO" : "INATIVO"));
    }

    // 4 - Getters e Setters
    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public String getNome() {

        return nome;
    }

    public void setNome(String nome) {

        this.nome = nome;
    }

    public boolean isAtivo() {

        return ativo;
    }

    public void setAtivo(boolean ativo) {

        this.ativo = ativo;
    }
}