package io.arieta.app;

import io.arieta.dados.drone.*;
import io.arieta.ui.UI;
import io.arieta.dados.transporte.*;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class ACMEAirDrones {
    private final String LOAD_DRONE_PATH = "SIMULA-DRONES.csv";

    private ArrayList<Drone> drones = new ArrayList<>();
    private Queue<Transporte> transportes = new LinkedList<>();

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

    public void cadastrarTransportes(File arquivo){
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
                                this.cadastraTransportePessoas(partes);
                                break;
                            case 2: // Carga Inanimada
                                this.cadastraTransporteCargaInanimada(partes);
                                break;
                            case 3: // Carga viva
                                this.cadastraTransporteCargaViva(partes);
                                break;
                            default:
                                saida.append("Erro: Tipo de transporte inválido").append("\n");
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

    public void cadastraTransportePessoas(String[] partes) throws Exception{
        int numero = Integer.parseInt(partes[1]);
        String nomeCliente = partes[2];
        String descricao = partes[3];
        double peso = Double.parseDouble(partes[4]);
        double latOrigem = Double.parseDouble(partes[5]);
        double longOrigem = Double.parseDouble(partes[6]);
        double latDestino = Double.parseDouble(partes[7]);
        double longDestino = Double.parseDouble(partes[8]);
        Integer qtdPessoas = Integer.parseInt(partes[9]);

        if (this.transportes.stream().anyMatch(transporte -> transporte.getNumero() == numero)) {
            throw new Exception("Erro: Numero do transporte repetido\n");
        }

        Transporte t = new TransportePessoal(numero, nomeCliente, descricao, peso, latOrigem, longOrigem, latDestino, longDestino, qtdPessoas);
        this.transportes.add(t);
    }

    public void cadastraTransporteCargaInanimada(String[] partes) throws Exception{
        int numero = Integer.parseInt(partes[1]);
        String nomeCliente = partes[2];
        String descricao = partes[3];
        double peso = Double.parseDouble(partes[4]);
        double latOrigem = Double.parseDouble(partes[5]);
        double longOrigem = Double.parseDouble(partes[6]);
        double latDestino = Double.parseDouble(partes[7]);
        double longDestino = Double.parseDouble(partes[8]);
        boolean cargaPerigosa;

        String temp = partes[9];
        if(temp.equals("true")){
            cargaPerigosa = true;
        }else if(temp.equals("false")){
            cargaPerigosa = false;
        }else{
            throw new Exception("Erro: tipo de carga inexistente\n");
        }

        if(this.transportes.stream().anyMatch(transporte -> transporte.getNumero() == numero)){
            throw new Exception("Erro: Numero do transporte repetido\n");
        }

        Transporte t = new TransporteCargaInanimada(
                    numero, 
                    nomeCliente, 
                    descricao, 
                    peso, 
                    latOrigem, 
                    longOrigem, 
                    latDestino, 
                    longDestino, 
                    cargaPerigosa
                    );
		this.transportes.add(t);

    }
    
    public void cadastraTransporteCargaViva(String[] partes) throws Exception{
        int numero = Integer.parseInt(partes[1]);
        String nomeCliente = partes[2];
        String descricao = partes[3];
        double peso = Double.parseDouble(partes[4]);
        double latOrigem = Double.parseDouble(partes[5]);
        double longOrigem = Double.parseDouble(partes[6]);
        double latDestino = Double.parseDouble(partes[7]);
        double longDestino = Double.parseDouble(partes[8]);
        double temperaturaMinima = Double.parseDouble(partes[9]);
        double temperaturaMaxima = Double.parseDouble(partes[10]);

        if (this.transportes.stream().anyMatch(transporte -> transporte.getNumero() == numero)){
            throw new Exception("Erro: Numero do transporte repetido\n");
        }

        Transporte t = new TransporteCargaViva(numero, nomeCliente, descricao, peso, latOrigem, longOrigem, latDestino, longDestino, temperaturaMinima, temperaturaMaxima);
        transportes.add(t);
    }
}
