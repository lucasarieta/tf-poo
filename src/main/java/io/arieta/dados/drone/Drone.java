package io.arieta.dados.drone;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // Usa o nome da classe para diferenciar as subclasses
        include = JsonTypeInfo.As.PROPERTY, // Adiciona uma propriedade adicional para identificar o tipo
        property = "type" // Nome da propriedade que será usada como discriminador
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DronePessoal.class, name = "PESSOAL"),
        @JsonSubTypes.Type(value = DroneCargaInanimada.class, name = "CARGA_INANIMADA"),
        @JsonSubTypes.Type(value = DroneCargaViva.class, name = "CARGA_VIVA")
})

public abstract class Drone {
    private int codigo;
    private double custoFixo;
    private double autonomia;

    public Drone(int codigo, double custoFixo, double autonomia) {
        this.codigo = codigo;
        this.custoFixo = custoFixo;
        this.autonomia = autonomia;
    }

    public int getCodigo() {
        return codigo;
    }

    public double getCustoFixo() {
        return custoFixo;
    }

    public double getAutonomia() {
        return autonomia;
    }

}
