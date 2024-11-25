package io.arieta.app;

import io.arieta.dados.drone.*;
import io.arieta.dados.transporte.*;

public class ACMEAirDronesTest {
    public static void main(String[] args) {
        ACMEAirDrones sistema = new ACMEAirDrones();

        // Adicionar drones ao sistema com autonomia e capacidade menores
        try {
            sistema.cadastrarDronePessoal(new String[] { "1", "101", "20.0", "200.0", "2" });
            sistema.cadastrarDroneCargaInanimada(new String[] { "2", "102", "30.0", "40.0", "30.0", "true" });
            sistema.cadastrarDroneCargaViva(new String[] { "3", "103", "40.0", "60.0", "20.0", "false" });
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar drones: " + e.getMessage());
        }

        // Adicionar transportes ao sistema com valores menores de peso e distância
        try {
            sistema.cadastraTransportePessoas(new String[] { "1", "201", "Cliente1", "Passeio", "10.0", "0.0", "0.0", "1.0", "1.0", "1" });
            sistema.cadastraTransporteCargaInanimada(new String[] { "2", "202", "Cliente2", "Carga Leve", "20.0", "0.0", "0.0", "2.0", "2.0", "false" });
            sistema.cadastraTransporteCargaViva(new String[] { "3", "203", "Cliente3", "Carga Viva", "15.0", "0.0", "0.0", "3.0", "3.0", "5.0", "10.0" });
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar transportes: " + e.getMessage());
        }

        // Processar transportes pendentes
        try {
            sistema.processarTransportesPendentes();
        } catch (Exception e) {
            System.out.println("Erro ao processar transportes pendentes: " + e.getMessage());
        }

        // Exibir relatório geral
        System.out.println(sistema.gerarRelatorioGeral());
    }
}
