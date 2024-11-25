package io.arieta.dados.transporte;

public class TransportePessoal extends Transporte {
    private int qtdPessoas;
    

    public TransportePessoal(
        int numero, String nomeCliente, String descricao, double peso, double latOrigem, double longOrigem, double latDestino, double longDestino, int qtdPessoas
    ) {
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
        this.qtdPessoas = qtdPessoas;
    }

    public int getQtdPessoas() {
        return qtdPessoas;
    }
}
