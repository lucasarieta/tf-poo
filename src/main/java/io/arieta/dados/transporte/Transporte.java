package io.arieta.dados.transporte;

import java.util.*;

public abstract class Transporte{
    private int numero;
    private String nomeCliente;
    private String descricao;
    private double peso;

    private double latitudeOrigem;
    private double longitudeOrigem;

    private double latitudeDestino;
    private double longitudeDestino;

    private TransporteStatus situacao;

    public Transporte(int numero, String nomeCliente, String descricao, double peso, double latOrigem, double longOrigem, double latDestino, double longDestino){
        this.numero = numero;
        this.nomeCliente = nomeCliente;
        this.descricao = descricao;
        this.peso = peso;
        this.latitudeOrigem = latOrigem;
        this.longitudeOrigem = longOrigem;
        this.latitudeDestino = latDestino;
        this.longitudeDestino = longDestino;
    }

    public double calculaCusto() {
        return 0;
    }

    public int getNumero(){
        return this.numero;
    }

    

}
