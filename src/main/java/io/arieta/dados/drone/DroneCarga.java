package io.arieta.dados.drone;

public class DroneCarga extends Drone {
    double pesoMaximo;

    public DroneCarga(
        int codigo,
        double custoFixo,
        double autonomia,
        double pesoMaximo
    ) {
        super(codigo, custoFixo, autonomia);
        this.pesoMaximo = pesoMaximo;
    }

    public double getPesoMaximo() {
        return pesoMaximo;
    }
}
