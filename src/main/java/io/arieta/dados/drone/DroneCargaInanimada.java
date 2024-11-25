package io.arieta.dados.drone;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DroneCargaInanimada extends DroneCarga {
    private boolean protecao;

    // Construtor padrão necessário para o framework de desserialização
    public DroneCargaInanimada() {
        super(0, 0.0, 0.0, 0.0);
        this.protecao = false;
    }

    // Construtor anotado para desserialização
    @JsonCreator
    public DroneCargaInanimada(
            @JsonProperty("codigo") int codigo,
            @JsonProperty("custoFixo") double custoFixo,
            @JsonProperty("autonomia") double autonomia,
            @JsonProperty("pesoMaximo") double pesoMaximo,
            @JsonProperty("protecao") boolean protecao
    ) {
        super(codigo, custoFixo, autonomia, pesoMaximo);
        this.protecao = protecao;
    }

    public boolean isProtecao() {
        return protecao;
    }

    public void setProtecao(boolean protecao) {
        this.protecao = protecao;
    }

    public double calculaCustoKm() {
        return protecao ? getCustoFixo() + 10 : getCustoFixo() + 5;
    }
}
