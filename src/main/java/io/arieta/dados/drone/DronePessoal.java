package io.arieta.dados.drone;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DronePessoal extends Drone {
    private int qtdMaxPessoas;

    // Construtor padrão necessário para o framework de desserialização
    public DronePessoal() {
        super(0, 0.0, 0.0);
        this.qtdMaxPessoas = 0;
    }

    // Construtor anotado para desserialização
    @JsonCreator
    public DronePessoal(
            @JsonProperty("codigo") int codigo,
            @JsonProperty("custoFixo") double custoFixo,
            @JsonProperty("autonomia") double autonomia,
            @JsonProperty("qtdMaxPessoas") int qtdMaxPessoas
    ) {
        super(codigo, custoFixo, autonomia);
        this.qtdMaxPessoas = qtdMaxPessoas;
    }

    public int getQtdMaxPessoas() {
        return qtdMaxPessoas;
    }

    public void setQtdMaxPessoas(int qtdMaxPessoas) {
        this.qtdMaxPessoas = qtdMaxPessoas;
    }

    public double calculaCustoKm() {
        return getCustoFixo() + (getQtdMaxPessoas() * 2);
    }
}
