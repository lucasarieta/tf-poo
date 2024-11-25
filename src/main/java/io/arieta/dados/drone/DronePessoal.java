package io.arieta.dados.drone;

public class DronePessoal extends Drone {
    private int qtdMaxPessoas;

    public  DronePessoal(
        int codigo,
        double custoFixo,
        double autonomia,
        int qtdMaxPessoas
    ) {
        super(codigo, custoFixo, autonomia);
        this.qtdMaxPessoas = qtdMaxPessoas;
    }

    public int getQtdMaxPessoas() {
        return qtdMaxPessoas;
    }

    public double calculaCustoKm() {
        return getCustoFixo() + (getQtdMaxPessoas() * 2);
    }
}
