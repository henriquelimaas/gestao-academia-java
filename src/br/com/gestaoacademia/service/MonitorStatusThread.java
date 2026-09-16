package br.com.gestaoacademia.service;

import br.com.gestaoacademia.database.ConexaoBanco;
import br.com.gestaoacademia.modelo.Aluno;

import java.sql.SQLException;
import java.util.List;

public class MonitorStatusThread implements Runnable {

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Aguarda 15 segundos entre as verificações
                Thread.sleep(15000);

                // Consulta o total atualizado no arquivo academia.db
                List<Aluno> alunosDoBanco = ConexaoBanco.listarAlunos();
                int totalAlunos = alunosDoBanco.size();

                System.out.printf("%n[BACKGROUND-THREAD] Sync status: Sistema ativo | Total de alunos cadastrados: %d%n> ",
                        totalAlunos);

            } catch (InterruptedException e) {
                System.out.println("[BACKGROUND-THREAD] Monitor encerrado.");
                break;
            } catch (SQLException e) {
                System.err.println("[BACKGROUND-THREAD] Falha de sincronização com o banco: " + e.getMessage());
            }
        }
    }
}