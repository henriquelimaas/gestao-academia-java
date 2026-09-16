package br.com.gestaoacademia.database;

import br.com.gestaoacademia.excecoes.DadosAlunoInvalidosException;
import br.com.gestaoacademia.modelo.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConexaoBanco {

    // Banco de dados em arquivo local 'academia.db' na raiz do projeto
    private static final String URL_CONEXAO = "jdbc:sqlite:academia.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL_CONEXAO);
    }

    public static void inicializarTabela() {
        String sql = """
            CREATE TABLE IF NOT EXISTS alunos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                tipo TEXT NOT NULL,
                nome TEXT NOT NULL,
                ativo INTEGER NOT NULL,
                faixa TEXT,
                graus INTEGER,
                prajied TEXT
            );
            """;

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }

    public static void salvarAluno(Aluno aluno) throws SQLException {
        String sql = "INSERT INTO alunos (tipo, nome, ativo, faixa, graus, prajied) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (aluno instanceof AlunoJiuJitsu bjj) {
                stmt.setString(1, "BJJ");
                stmt.setString(2, bjj.getNome());
                stmt.setInt(3, bjj.isAtivo() ? 1 : 0);
                stmt.setString(4, bjj.getFaixa());
                stmt.setInt(5, bjj.getGraus());
                stmt.setNull(6, Types.VARCHAR);
            } else if (aluno instanceof AlunoMuayThai mt) {
                stmt.setString(1, "MT");
                stmt.setString(2, mt.getNome());
                stmt.setInt(3, mt.isAtivo() ? 1 : 0);
                stmt.setNull(4, Types.VARCHAR);
                stmt.setNull(5, Types.INTEGER);
                stmt.setString(6, mt.getPrajied());
            }

            stmt.executeUpdate();

            // Recupera o ID gerado pelo banco e seta no objeto Java
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    aluno.setId(rs.getInt(1));
                }
            }
        }
    }

    public static List<Aluno> listarAlunos() throws SQLException {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM alunos";

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String tipo = rs.getString("tipo");
                String nome = rs.getString("nome");
                boolean ativo = rs.getInt("ativo") == 1;

                if ("BJJ".equalsIgnoreCase(tipo)) {
                    String faixa = rs.getString("faixa");
                    int graus = rs.getInt("graus");
                    alunos.add(new AlunoJiuJitsu(id, nome, ativo, faixa, graus));
                } else if ("MT".equalsIgnoreCase(tipo)) {
                    String prajied = rs.getString("prajied");
                    alunos.add(new AlunoMuayThai(id, nome, ativo, prajied));
                }
            }
        }
        return alunos;
    }

    public static void atualizarAluno(Aluno aluno) throws SQLException {
        String sql = "UPDATE alunos SET faixa = ?, graus = ?, prajied = ?, ativo = ? WHERE id = ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (aluno instanceof AlunoJiuJitsu bjj) {
                stmt.setString(1, bjj.getFaixa());
                stmt.setInt(2, bjj.getGraus());
                stmt.setNull(3, Types.VARCHAR);
                stmt.setInt(4, bjj.isAtivo() ? 1 : 0);
                stmt.setInt(5, bjj.getId());
            } else if (aluno instanceof AlunoMuayThai mt) {
                stmt.setNull(1, Types.VARCHAR);
                stmt.setNull(2, Types.INTEGER);
                stmt.setString(3, mt.getPrajied());
                stmt.setInt(4, mt.isAtivo() ? 1 : 0);
                stmt.setInt(5, mt.getId());
            }

            stmt.executeUpdate();
        }
    }




    public static void graduarAlunoBJJ(int id, String novaFaixa, int novosGraus) throws SQLException {
        String sql = "UPDATE alunos SET faixa = ?, graus = ? WHERE id = ? AND tipo = 'BJJ'";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novaFaixa);
            stmt.setInt(2, novosGraus);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    public static void excluirAluno(int id) throws SQLException {
        String sql = "DELETE FROM alunos WHERE id = ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✅ Aluno excluído do banco com sucesso!");
            } else {
                System.out.println("Nenhum aluno encontrado com o ID informado.");
            }
        }
    }
}