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
                        saida.append("Erro ao cadastrar transporte: ").append(e.getMessage()).append("\n");
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

    public void processarTransportesPendentes() {
        if (transportes.isEmpty()) {
            System.out.println("Erro: Não há transportes pendentes.");
            return;
        }
    
        Queue<Transporte> filaRestante = new LinkedList<>();
    
        while (!transportes.isEmpty()) {
            Transporte transporte = transportes.poll();
            Optional<Drone> droneDisponivel = encontrarDroneDisponivel(transporte);
    
            if (droneDisponivel.isPresent()) {
                Drone drone = droneDisponivel.get();
                try{
                transporte.setSituacao(TransporteStatus.ALOCADO);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
                System.out.println("Transporte " + transporte.getNumero() + " alocado ao drone " + drone.getCodigo());

            } else {
                System.out.println("Transporte " + transporte.getNumero() + " não pode ser alocado e retornará à fila.");
                filaRestante.add(transporte);
            }
        }
    
        
        transportes.addAll(filaRestante);
    }
    

    private Optional<Drone> encontrarDroneDisponivel(Transporte transporte) {
        for (Drone drone : drones) {
            if (isDroneCompativel(drone, transporte)) {
                return Optional.of(drone);
            }
        }
        return Optional.empty();
    }

    private boolean isDroneCompativel(Drone drone, Transporte transporte) {
        double distancia = calcularDistancia(
            transporte.getLatitudeOrigem(),
            transporte.getLongitudeOrigem(),
            transporte.getLatitudeDestino(),
            transporte.getLongitudeDestino()
        );
    
        if (drone.getAutonomia() < distancia) {
            return false;
        }

        switch (drone) {
            case DronePessoal dronePessoal when transporte instanceof TransportePessoal transportePessoal -> {
                return dronePessoal.getQtdMaxPessoas() >= transportePessoal.getQtdPessoas();
            }
            case DroneCargaInanimada droneCarga when transporte instanceof TransporteCargaInanimada -> {
                TransporteCargaInanimada transporteCarga = (TransporteCargaInanimada) transporte;
                return droneCarga.getPesoMaximo() >= transporteCarga.getPeso();
            }
            case DroneCargaViva droneVivo when transporte instanceof TransporteCargaViva transporteVivo -> {
                return droneVivo.getPesoMaximo() >= transporteVivo.getPeso();
            }
            default -> {
            }
        }


        return false;
    }

    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }



}
