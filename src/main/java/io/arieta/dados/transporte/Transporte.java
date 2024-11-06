package io.arieta.dados.transporte;

public abstract class Transporte {
    private int numero;
    private String nomeCliente;
    private String descricao;
    private double peso;

    private double latitudeOrigem;
    private double longitudeOrigem;

    private double latitudeDestino;
    private double longitudeDestino;

    private TransporteStatus situacao;

    public double calculaCusto() {
        return 0;
    }
}
