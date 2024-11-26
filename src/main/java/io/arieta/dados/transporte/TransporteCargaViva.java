package io.arieta.dados.transporte;

public class TransporteCargaViva extends Transporte {
    private double temperaturaMinima;
    private double temperaturaMaxima;

    public void setTemperaturaMaxima(double temperaturaMaxima) {
        this.temperaturaMaxima = temperaturaMaxima;
    }

    public TransporteCargaViva(int numero, String nomeCliente, String descricao, double peso, double latOrigem, double longOrigem, double latDestino, double longDestino, double temperaturaMinima, double temperaturaMaxima){
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
        this.temperaturaMinima = temperaturaMinima;
        this.temperaturaMaxima = temperaturaMaxima;
    }

    public void setTemperaturaMinima(double temperaturaMinima) {
        this.temperaturaMinima = temperaturaMinima;
    }

    public TransporteCargaViva(){
        super(0,null,null,0.0,0.0,0.0,0.0,0.0);
    }

    public double getTemperaturaMinima() {
        return temperaturaMinima;
    }

    public double getTemperaturaMaxima() {
        return temperaturaMaxima;
    }

    @Override
    public double calculaCusto() {
        double intervalo = temperaturaMaxima - temperaturaMinima;
        return (getPeso() * 0.5) + (intervalo > 10.0 ? 1000.0 : 0.0);
    }
}
