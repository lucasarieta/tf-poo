package io.arieta.dados.transporte;

public class TransporteCargaInanimada extends Transporte {
    private boolean cargaPerigosa;

    public TransporteCargaInanimada(int numero, String nomeCliente, String descricao, double peso, double latOrigem, double longOrigem, double latDestino, double longDestino, boolean cargaPerigosa  ){
        super(
        numero, 
        nomeCliente, 
        descricao,  
        peso, 
        latOrigem, 
        longOrigem, 
        latDestino, 
        longDestino
        );
        this.cargaPerigosa = cargaPerigosa;
    }

    public boolean isCargaPerigosa() {
        return cargaPerigosa;
    }

    @Override
    public double calculaCusto() {
        return (getPeso() * 0.5) + (cargaPerigosa ? 500.0 : 0.0);
    }
}
