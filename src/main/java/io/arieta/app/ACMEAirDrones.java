package io.arieta.app;

import io.arieta.dados.drone.Drone;
import io.arieta.dados.drone.DroneCargaInanimada;
import io.arieta.dados.drone.DroneCargaViva;
import io.arieta.dados.drone.DronePessoal;
import io.arieta.ui.UI;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ACMEAirDrones {
    private final String LOAD_DRONE_PATH = "SIMULA-DRONES.csv";

    private ArrayList<Drone> drones = new ArrayList<>();

    public void executar() {
        SwingUtilities.invokeLater(() -> new UI(this));
    }

    public void cadastrarDrones(File arquivo) {
        try (Scanner scanner = new Scanner(arquivo)) {
            StringBuilder saida = new StringBuilder();

            // Ignorar a primeira linha do cabeçalho CSV
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(";");
                int tipo;

                if (partes.length > 0) {
                    try {
                        tipo = Integer.parseInt(partes[0]);

                        switch (tipo) {
                            case 1: // Pessoal
                                this.cadastrarDronePessoal(partes);
                                saida.append("1:").append("Drone Pessoal cadastrado com sucesso").append("\n");
                                break;
                            case 2: // Carga Inanimada
                                this.cadastrarDroneCargaInanimada(partes);
                                saida.append("1:").append("Drone Carga Inanimada cadastrado com sucesso").append("\n");
                                break;
                            case 3: // Carga viva
                                this.cadastrarDroneCargaViva(partes);
                                saida.append("1:").append("Drone Carga Viva cadastrado com sucesso").append("\n");
                                break;
                            default:
                                saida.append("Erro: Tipo de drone inválido").append("\n");
                                break;
                        }
                    } catch (NumberFormatException e) {
                        saida.append("Erro: Tipo inválido na linha: ").append(linha).append("\n");
                    } catch (Exception e) {
                        saida.append("Erro ao cadastrar drone: ").append(e.getMessage()).append("\n");
                    }
                }
            }

            System.out.println(saida.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Arquivo não encontrado: " + e.getMessage());
        }
    }

    private void cadastrarDronePessoal(String[] partes) throws Exception {
        int codigo = Integer.parseInt(partes[1]);
        double custoFixo = Double.parseDouble(partes[2]);
        double autonomia = Double.parseDouble(partes[3]);
        int qtdMaxPessoas = Integer.parseInt(partes[4]);

        if (this.drones.stream().anyMatch(drone -> drone.getCodigo() == codigo)) {
            throw new Exception("Erro: Código de drone repetido");
        }

        Drone drone = new DronePessoal(codigo, custoFixo, autonomia, qtdMaxPessoas);
        this.drones.add(drone);
    }

    private void cadastrarDroneCargaInanimada(String[] partes) throws Exception{
        int codigo = Integer.parseInt(partes[1]);
        double custoFixo = Double.parseDouble(partes[2]);
        double autonomia = Double.parseDouble(partes[3]);
        double pesoMaximo = Double.parseDouble(partes[4]);
        boolean protecao = Boolean.parseBoolean(partes[5]);

        if (this.drones.stream().anyMatch(drone -> drone.getCodigo() == codigo)) {
            throw new Exception("Erro: Código de drone repetido");
        }

        Drone drone = new DroneCargaInanimada(codigo, custoFixo, autonomia, pesoMaximo, protecao);
        this.drones.add(drone);
    }

    private void cadastrarDroneCargaViva(String[] partes) throws Exception {
        int codigo = Integer.parseInt(partes[1]);
        double custoFixo = Double.parseDouble(partes[2]);
        double autonomia = Double.parseDouble(partes[3]);
        double pesoMaximo = Double.parseDouble(partes[4]);
        boolean climatizado = Boolean.parseBoolean(partes[5]);

        if (this.drones.stream().anyMatch(drone -> drone.getCodigo() == codigo)) {
            throw new Exception("Erro: Código de drone repetido");
        }

       Drone drone = new DroneCargaViva(codigo, custoFixo, autonomia, pesoMaximo, climatizado);
       this.drones.add(drone);
    }
}
