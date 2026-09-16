package br.com.gestaoacademia.modelo;

public class AlunoJiuJitsu extends Aluno {
    private String faixa;
    private int graus;

    public AlunoJiuJitsu(Integer id, String nome, boolean ativo, String faixa, int graus) {
        super(id, nome, ativo);
        this.faixa = faixa;
        this.graus = graus;
    }

    public AlunoJiuJitsu(String nome, String faixa, int graus) {
        super(nome);
        this.faixa = faixa;
        this.graus = graus;
    }

    @Override
    public String exibirFichaTecnica() {
        return super.exibirFichaTecnica() + String.format(" | Faixa: %s | Graus: %d", faixa, graus);
    }

    public void graduar() {
        if (this.graus < 4) {
            this.graus++;
            System.out.println("OSS! " + getNome() + " recebeu o " + this.graus + "º grau na faixa " + this.faixa + "!");
        } else {
            this.graus = 0;
            switch (this.faixa.toLowerCase()) {
                case "branca" -> this.faixa = "Azul";
                case "azul" -> this.faixa = "Roxa";
                case "roxa" -> this.faixa = "Marrom";
                case "marrom" -> this.faixa = "Preta";
                default -> {
                    System.out.println(getNome() + " já está na Faixa Preta!");
                    return;
                }
            }
            System.out.println("PARABÉNS! " + getNome() + " mudou de faixa e agora é FAIXA " + this.faixa.toUpperCase() + "!");
        }
    }
    public void setFaixa(String faixa) {
        this.faixa = faixa;
    }

    public void setGraus(int graus) {
        this.graus = graus;
    }

    public String getFaixa() {

        return faixa;
    }

    public int getGraus() {
        return graus;
    }
}