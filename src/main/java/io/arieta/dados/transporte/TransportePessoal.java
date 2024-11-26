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

    public void setQtdPessoas(int qtdPessoas) {
        this.qtdPessoas = qtdPessoas;
    }

    public TransportePessoal(){
        super(0,null,null,0.0,0.0,0.0,0.0,0.0);
        this.qtdPessoas = 0;
    }

    public int getQtdPessoas() {
        return qtdPessoas;
    }

    @Override
    public double calculaCusto() {
        return (getPeso() * 0.5) + (qtdPessoas * 10.0);
    }
}
