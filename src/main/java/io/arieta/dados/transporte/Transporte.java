package io.arieta.dados.transporte;

import java.util.*;

import io.arieta.dados.drone.Drone;

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
        this.situacao = TransporteStatus.PENDENTE;
    }

    public abstract double calculaCusto();

    public int getNumero(){
        return this.numero;
    }

    public void setSituacao(TransporteStatus novaSituacao) throws Exception {
        
        if (this.situacao == TransporteStatus.TERMINADO || this.situacao == TransporteStatus.CANCELADO) {
            throw new Exception("Erro: Não é possível alterar a situação de um transporte TERMINADO ou CANCELADO.");
        }
        this.situacao = novaSituacao;
    }
    
    public TransporteStatus getSituacao(){
        return this.situacao;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getLongitudeOrigem() {
        return longitudeOrigem;
    }

    public double getPeso() {
        return peso;
    }

    public double getLatitudeOrigem() {
        return latitudeOrigem;
    }

    public double getLatitudeDestino() {
        return latitudeDestino;
    }

    public double getLongitudeDestino() {
        return longitudeDestino;
    }
}
