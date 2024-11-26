package io.arieta.dados.transporte;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TransportePessoal.class, name = "PESSOAL"),
        @JsonSubTypes.Type(value = TransporteCargaInanimada.class, name = "CARGA_INANIMADA"),
        @JsonSubTypes.Type(value = TransporteCargaViva.class, name = "CARGA_VIVA")
})

public abstract class Transporte{
    private int numero;
    private String nomeCliente;
    private String descricao;
    private double peso;

    private double latitudeOrigem;
    private double longitudeOrigem;

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setLongitudeDestino(double longitudeDestino) {
        this.longitudeDestino = longitudeDestino;
    }

    public void setLatitudeDestino(double latitudeDestino) {
        this.latitudeDestino = latitudeDestino;
    }

    public void setLongitudeOrigem(double longitudeOrigem) {
        this.longitudeOrigem = longitudeOrigem;
    }

    public void setLatitudeOrigem(double latitudeOrigem) {
        this.latitudeOrigem = latitudeOrigem;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

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
