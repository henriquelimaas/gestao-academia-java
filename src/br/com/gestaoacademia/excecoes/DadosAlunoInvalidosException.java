package br.com.gestaoacademia.excecoes;

public class DadosAlunoInvalidosException extends RuntimeException {
    public DadosAlunoInvalidosException(String message) {
        super(message);
    }
}
