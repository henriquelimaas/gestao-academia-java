package br.com.gestaoacademia.modelo;

public class AlunoMuayThai extends Aluno {
    private String prajied; // Cor do Prajied (Pramah)

    public AlunoMuayThai(Integer id, String nome, boolean ativo, String prajied) {
        super(id, nome, ativo);
        this.prajied = prajied;
    }

    public AlunoMuayThai(String nome, String prajied) {
        super(nome);
        this.prajied = prajied;
    }

    @Override
    public String exibirFichaTecnica() {

        return super.exibirFichaTecnica() + String.format(" | Prajied: %s", prajied);
    }

    public String getPrajied() {

        return prajied;
    }

    public void setPrajied(String prajied) {
        this.prajied = prajied;
    }
}