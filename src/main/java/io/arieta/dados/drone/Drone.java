package io.arieta.dados.drone;

public abstract class Drone {
    private int codigo;
    private double custoFixo;
    private double autonomia;

    public Drone(int codigo, double custoFixo, double autonomia) {
        
    }

    public double calculaCustoKm() {
        return 0.0;
    }

    public int getCodigo() {
        return this.codigo;
    }
}
