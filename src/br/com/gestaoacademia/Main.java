package br.com.gestaoacademia;

import br.com.gestaoacademia.database.ConexaoBanco;
import br.com.gestaoacademia.excecoes.DadosAlunoInvalidosException;
import br.com.gestaoacademia.modelo.*;
import br.com.gestaoacademia.service.MonitorStatusThread;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // CRÍTICO 1: Cria o arquivo 'academia.db' e a tabela no banco antes do menu
        ConexaoBanco.inicializarTabela();

        // Inicia Thread de Segundo Plano para monitoramento
        Thread monitorThread = new Thread(new MonitorStatusThread());
        monitorThread.setDaemon(true);
        monitorThread.start();

        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        System.out.println("==========================================");
        System.out.println("  SISTEMA DE GESTÃO DE ACADEMIA - BJJ & MT");
        System.out.println("==========================================");

        while (opcao != 0) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Cadastrar Aluno de Jiu-Jitsu");
            System.out.println("2. Cadastrar Aluno de Muay Thai");
            System.out.println("3. Alunos Cadastrados (Polimorfismo)");
            System.out.println("4. Atualizar Dados do Aluno");
            System.out.println("5. Graduar Aluno Jiu-Jitsu");
            System.out.println("6. Excluir Aluno por ID");
            System.out.println("0. Sair");
            System.out.print("> Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1 -> cadastrarAlunoJiuJitsu(scanner);
                    case 2 -> cadastrarAlunoMuayThai(scanner);
                    case 3 -> listarAlunos();
                    case 4 -> atualizarDadosAluno(scanner);
                    case 5 -> graduarAlunoJiuJitsu(scanner);
                    case 6 -> excluirAlunoPorID(scanner);
                    case 0 -> System.out.println("Encerrando a aplicação...");
                    default -> System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Digite apenas números inteiros para o menu.");
            }
        }
        scanner.close();
    }

    private static void cadastrarAlunoJiuJitsu(Scanner scanner) {
        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            if (nome.isBlank()) throw new DadosAlunoInvalidosException("O nome do aluno não pode ser vazio.");

            System.out.print("Faixa (Branca, Azul, Roxa, Marrom, Preta): ");
            String faixa = scanner.nextLine();

            System.out.print("Graus (0 a 4): ");
            int graus = Integer.parseInt(scanner.nextLine());
            if (graus < 0 || graus > 4) throw new DadosAlunoInvalidosException("O grau deve ser entre 0 e 4.");

            // Construtor sem ID (o banco gera o ID com AUTOINCREMENT)
            AlunoJiuJitsu aluno = new AlunoJiuJitsu(nome, faixa, graus);

            // PERSISTE NO BANCO SQLITE
            ConexaoBanco.salvarAluno(aluno);

            System.out.println("✅ Aluno de Jiu-Jitsu cadastrado e salvo no banco de dados!");

        } catch (DadosAlunoInvalidosException e) {
            System.err.println("Regra de Negócio: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Entrada inválida: Certifique-se de digitar números para os Graus.");
        } catch (SQLException e) {
            System.err.println("Erro de Banco de Dados: " + e.getMessage());
        }
    }

    private static void cadastrarAlunoMuayThai(Scanner scanner) {
        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            if (nome.isBlank()) throw new DadosAlunoInvalidosException("O nome do aluno não pode ser vazio.");

            System.out.print("Prajied (Cor da corda de braço): ");
            String prajied = scanner.nextLine();

            // Construtor sem ID
            AlunoMuayThai aluno = new AlunoMuayThai(nome, prajied);

            // PERSISTE NO BANCO SQLITE
            ConexaoBanco.salvarAluno(aluno);

            System.out.println("✅ Aluno de Muay Thai cadastrado e salvo no banco de dados!");

        } catch (DadosAlunoInvalidosException e) {
            System.err.println("Regra de Negócio: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro de Banco de Dados: " + e.getMessage());
        }
    }

    private static void listarAlunos() {
        System.out.println("\n=== ALUNOS CADASTRADOS (BANCO DE DADOS) ===");
        try {
            // BUSCA OS DADOS REAIS DIRETO DO ARQUIVO ACADEMIA.DB
            List<Aluno> listaAlunos = ConexaoBanco.listarAlunos();

            if (listaAlunos.isEmpty()) {
                System.out.println("Nenhum aluno cadastrado no banco de dados.");
                return;
            }

            for (Aluno a : listaAlunos) {
                System.out.println(a.exibirFichaTecnica());
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar alunos do banco: " + e.getMessage());
        }
    }

    private static void atualizarDadosAluno(Scanner scanner) {
        System.out.print("Digite o ID do Aluno que deseja atualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());

            // 1. Busca os alunos do banco para encontrar o ID
            List<Aluno> listaAlunos = ConexaoBanco.listarAlunos();
            Aluno alunoEncontrado = null;

            for (Aluno a : listaAlunos) {
                if (a.getId().equals(id)) {
                    alunoEncontrado = a;
                    break;
                }
            }

            if (alunoEncontrado == null) {
                System.err.println("Aluno não encontrado com o ID informado.");
                return;
            }

            // 2. Atualiza o nome (comum a todos os alunos)
            System.out.print("Novo Nome (ou Pressione ENTER para manter '" + alunoEncontrado.getNome() + "'): ");
            String novoNome = scanner.nextLine();
            if (!novoNome.isBlank()) {
                alunoEncontrado.setNome(novoNome);
            }

            // 3. Pergunta se deseja alterar o status (Ativo/Inativo)
            System.out.print("Aluno está ativo? (S/N) [Atual: " + (alunoEncontrado.isAtivo() ? "S" : "N") + "]: ");
            String respAtivo = scanner.nextLine();
            if (!respAtivo.isBlank()) {
                alunoEncontrado.setAtivo(respAtivo.equalsIgnoreCase("S"));
            }

            // 4. Se for Jiu-Jitsu, altera dados específicos de BJJ
            if (alunoEncontrado instanceof AlunoJiuJitsu bjj) {
                System.out.print("Nova Faixa (ou ENTER para manter '" + bjj.getFaixa() + "'): ");
                String novaFaixa = scanner.nextLine();
                if (!novaFaixa.isBlank()) {
                    bjj.setFaixa(novaFaixa);
                }

                System.out.print("Novos Graus (0 a 4) [Atual: " + bjj.getGraus() + "]: ");
                String grausStr = scanner.nextLine();
                if (!grausStr.isBlank()) {
                    int novosGraus = Integer.parseInt(grausStr);
                    if (novosGraus >= 0 && novosGraus <= 4) {
                        bjj.setGraus(novosGraus);
                    } else {
                        throw new DadosAlunoInvalidosException("Os graus devem ser entre 0 e 4.");
                    }
                }
            }
            // 5. Se for Muay Thai, altera dados específicos de MT
            else if (alunoEncontrado instanceof AlunoMuayThai mt) {
                System.out.print("Novo Prajied (ou ENTER para manter '" + mt.getPrajied() + "'): ");
                String novoPrajied = scanner.nextLine();
                if (!novoPrajied.isBlank()) {
                    mt.setPrajied(novoPrajied);
                }
            }

            // 6. PERSISTE AS ALTERAÇÕES NO BANCO DE DADOS
            ConexaoBanco.atualizarAluno(alunoEncontrado);
            System.out.println("✅ Dados do aluno atualizados com sucesso no banco de dados!");

        } catch (NumberFormatException e) {
            System.err.println("Entrada inválida: Digite números válidos para o ID ou Graus.");
        } catch (DadosAlunoInvalidosException e) {
            System.err.println("Regra de Negócio: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao salvar atualização no banco: " + e.getMessage());
        }
    }





    private static void graduarAlunoJiuJitsu(Scanner scanner) {
        System.out.print("Digite o ID do Aluno de BJJ para graduar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());

            // Busca a lista atualizada do banco
            List<Aluno> listaAlunos = ConexaoBanco.listarAlunos();
            Aluno alunoEncontrado = null;

            for (Aluno a : listaAlunos) {
                if (a.getId().equals(id)) {
                    alunoEncontrado = a;
                    break;
                }
            }

            if (alunoEncontrado instanceof AlunoJiuJitsu bjj) {
                bjj.graduar();
                // ATUALIZA AS ALTERAÇÕES NO BANCO DE DADOS
                ConexaoBanco.atualizarAluno(bjj);
                System.out.println("✅ Graduação salva no banco de dados!");
            } else if (alunoEncontrado != null) {
                System.err.println("Regra de Negócio: O aluno informado não é do Jiu-Jitsu.");
            } else {
                System.err.println("Aluno não encontrado.");
            }

        } catch (NumberFormatException e) {
            System.err.println("Entrada inválida: Digite um número inteiro para o ID.");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar graduação no banco: " + e.getMessage());
        }
    }

    private static void excluirAlunoPorID(Scanner scanner) {
        System.out.print("Digite o ID do Aluno a ser excluído: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());

            // Chamada direta para o método da ConexaoBanco
            ConexaoBanco.excluirAluno(id);

        } catch (NumberFormatException e) {
            System.err.println("Entrada inválida: Digite um número inteiro.");
        } catch (SQLException e) {
            System.err.println("Erro ao excluir do banco: " + e.getMessage());
        }
    }
}