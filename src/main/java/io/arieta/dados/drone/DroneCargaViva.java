package io.arieta.dados.drone;

public class DroneCargaViva extends DroneCarga {
    private boolean climatizado;


    public DroneCargaViva(
        int codigo,
        double custoFixo,
        double autonomia,
        double pesoMaximo,
        boolean climatizado
    ) {
        super(codigo, custoFixo, autonomia, pesoMaximo);
        this.climatizado = climatizado;
    }

    public void setClimatizado(boolean climatizado) {
        this.climatizado = climatizado;
    }

    public DroneCargaViva(){
        super(0,0.0,0,0.0);
    }

    public boolean isClimatizado() {
        return climatizado;
    }

    public double calculaCustoKm(){
        return climatizado ? getCustoFixo() + 20 : getCustoFixo() + 10;
    }
}
