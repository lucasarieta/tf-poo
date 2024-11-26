package io.arieta.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.arieta.dados.drone.*;
import io.arieta.dados.transporte.*;
import io.arieta.ui.MainFrame;
import io.arieta.util.JsonHandler;
import io.arieta.util.XmlHandler;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;


public class ACMEAirDrones {
    private final String LOAD_DRONE_PATH = "SIMULA-DRONES.csv";

    private ArrayList<Drone> drones = new ArrayList<>();
    private Queue<Transporte> transportes = new LinkedList<>();

    public void executar() {
        SwingUtilities.invokeLater(() -> new MainFrame(this));
    }

    public void carregarDadosJSON(String filePath) throws IOException {
        JsonHandler<ACMEAirDrones> jsonHandler = new JsonHandler<>(ACMEAirDrones.class);
        ACMEAirDrones dados = jsonHandler.readJson(filePath);
        this.drones = dados.drones;
        this.transportes = dados.transportes;
        System.out.println("Dados carregados do JSON com sucesso!");
    }

    public void salvarDadosJSON(String filePath) throws IOException {
        JsonHandler<ACMEAirDrones> jsonHandler = new JsonHandler<>(ACMEAirDrones.class);
        jsonHandler.writeJson(filePath, this);
        System.out.println("Dados salvos em JSON no arquivo: " + filePath);
    }


    public void carregarDadosXML(String filePath) throws IOException {
        XmlHandler<ACMEAirDrones> xmlHandler = new XmlHandler<>(ACMEAirDrones.class);
        ACMEAirDrones dados = xmlHandler.readXml(filePath);
        this.drones = dados.drones;
        this.transportes = dados.transportes;
        System.out.println("Dados carregados do XML com sucesso!");
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

    public void salvarDados(String nomeArquivo) throws IOException {

        try (FileWriter droneWriter = new FileWriter(nomeArquivo + "-DRONES.csv")) {
            droneWriter.write("tipo;codigo;custofixo;autonomia;especifico\n");
            for (Drone drone : drones) {
                if (drone instanceof DronePessoal) {
                    DronePessoal dp = (DronePessoal) drone;
                    droneWriter.write(String.format("1;%d;%.2f;%.2f;%d\n",
                            dp.getCodigo(), dp.getCustoFixo(), dp.getAutonomia(), dp.getQtdMaxPessoas()));
                } else if (drone instanceof DroneCargaInanimada) {
                    DroneCargaInanimada dci = (DroneCargaInanimada) drone;
                    droneWriter.write(String.format("2;%d;%.2f;%.2f;%.2f;%b\n",
                            dci.getCodigo(), dci.getCustoFixo(), dci.getAutonomia(), dci.getPesoMaximo(), dci.isProtecao()));
                } else if (drone instanceof DroneCargaViva) {
                    DroneCargaViva dcv = (DroneCargaViva) drone;
                    droneWriter.write(String.format("3;%d;%.2f;%.2f;%.2f;%b\n",
                            dcv.getCodigo(), dcv.getCustoFixo(), dcv.getAutonomia(), dcv.getPesoMaximo(), dcv.isClimatizado()));
                }
            }
        }

        // Salvar transportes
        try (FileWriter transporteWriter = new FileWriter(nomeArquivo + "-TRANSPORTES.csv")) {
            transporteWriter.write("tipo;numero;nomecliente;descricao;peso;latorigem;longorigem;latdestino;longdestino;especifico\n");
            for (Transporte transporte : transportes) {
                if (transporte instanceof TransportePessoal) {
                    TransportePessoal tp = (TransportePessoal) transporte;
                    transporteWriter.write(String.format("1;%d;%s;%s;%.2f;%.6f;%.6f;%.6f;%.6f;%d\n",
                            tp.getNumero(), tp.getNomeCliente(), tp.getDescricao(), tp.getPeso(),
                            tp.getLatitudeOrigem(), tp.getLongitudeOrigem(), tp.getLatitudeDestino(), tp.getLongitudeDestino(),
                            tp.getQtdPessoas()));
                } else if (transporte instanceof TransporteCargaInanimada) {
                    TransporteCargaInanimada tci = (TransporteCargaInanimada) transporte;
                    transporteWriter.write(String.format("2;%d;%s;%s;%.2f;%.6f;%.6f;%.6f;%.6f;%b\n",
                            tci.getNumero(), tci.getNomeCliente(), tci.getDescricao(), tci.getPeso(),
                            tci.getLatitudeOrigem(), tci.getLongitudeOrigem(), tci.getLatitudeDestino(), tci.getLongitudeDestino(),
                            tci.isCargaPerigosa()));
                } else if (transporte instanceof TransporteCargaViva) {
                    TransporteCargaViva tcv = (TransporteCargaViva) transporte;
                    transporteWriter.write(String.format("3;%d;%s;%s;%.2f;%.6f;%.6f;%.6f;%.6f;%.2f;%.2f\n",
                            tcv.getNumero(), tcv.getNomeCliente(), tcv.getDescricao(), tcv.getPeso(),
                            tcv.getLatitudeOrigem(), tcv.getLongitudeOrigem(), tcv.getLatitudeDestino(), tcv.getLongitudeDestino(),
                            tcv.getTemperaturaMinima(), tcv.getTemperaturaMaxima()));
                }
            }
        }
    }

    public void carregarDados(String nomeArquivo) throws Exception {
        // Carregar drones
        File dronesFile = new File(nomeArquivo + "-DRONES.csv");
        if (dronesFile.exists()) {
            try (Scanner scanner = new Scanner(dronesFile)) {
                // Ignorar a primeira linha do cabeçalho
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
                while (scanner.hasNextLine()) {
                    String linha = scanner.nextLine();
                    String[] partes = linha.split(";");
                    int tipo = Integer.parseInt(partes[0]);

                    switch (tipo) {
                        case 1: // Drone pessoal
                            cadastrarDronePessoal(partes);
                            break;
                        case 2: // Drone carga inanimada
                            cadastrarDroneCargaInanimada(partes);
                            break;
                        case 3: // Drone carga viva
                            cadastrarDroneCargaViva(partes);
                            break;
                        default:
                            throw new IllegalArgumentException("Tipo de drone inválido: " + tipo);
                    }
                }
            }
        }

        // Carregar transportes
        File transportesFile = new File(nomeArquivo + "-TRANSPORTES.csv");
        if (transportesFile.exists()) {
            try (Scanner scanner = new Scanner(transportesFile)) {
                // Ignorar a primeira linha do cabeçalho
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
                while (scanner.hasNextLine()) {
                    String linha = scanner.nextLine();
                    String[] partes = linha.split(";");
                    int tipo = Integer.parseInt(partes[0]);

                    switch (tipo) {
                        case 1: // Transporte pessoal
                            cadastraTransportePessoas(partes);
                            break;
                        case 2: // Transporte carga inanimada
                            cadastraTransporteCargaInanimada(partes);
                            break;
                        case 3: // Transporte carga viva
                            cadastraTransporteCargaViva(partes);
                            break;
                        default:
                            throw new IllegalArgumentException("Tipo de transporte inválido: " + tipo);
                    }
                }
            }
        }
    }

    void cadastrarDronePessoal(String[] partes) throws Exception {
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

    void cadastrarDroneCargaInanimada(String[] partes) throws Exception{
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

    void cadastrarDroneCargaViva(String[] partes) throws Exception {
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

    public void processarTransportesPendentes() throws Exception {

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
                transporte.setSituacao(TransporteStatus.ALOCADO);
                System.out.println("Transporte " + transporte.getNumero() + " alocado ao drone " + drone.getCodigo());
                filaRestante.add(transporte);
            } else {

                System.out.println("Transporte " + transporte.getNumero() + " não pode ser alocado e retornará à fila.");
                filaRestante.add(transporte);
            }


        }

        while(!filaRestante.isEmpty()) {
            transportes.add(filaRestante.poll());
        }

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

        // Log de depuração
        System.out.println("Verificando compatibilidade:");
        System.out.println("Drone Código: " + drone.getCodigo() + ", Autonomia: " + drone.getAutonomia() + ", Distância: " + distancia);
        System.out.println("Transporte Número: " + transporte.getNumero() + ", Peso: " + transporte.getPeso());

        if (drone.getAutonomia() < distancia) {
            System.out.println("Incompatível: Autonomia insuficiente.");
            return false;
        }

        switch (drone) {
            case DronePessoal dronePessoal when transporte instanceof TransportePessoal transportePessoal -> {
                boolean compativel = dronePessoal.getQtdMaxPessoas() >= transportePessoal.getQtdPessoas();
                System.out.println(compativel ? "Compatível: Transporte Pessoal." : "Incompatível: Capacidade de pessoas insuficiente.");
                return compativel;
            }
            case DroneCargaInanimada droneCarga when transporte instanceof TransporteCargaInanimada transporteCarga -> {
                boolean compativel = droneCarga.getPesoMaximo() >= transporteCarga.getPeso();
                System.out.println(compativel ? "Compatível: Transporte Carga Inanimada." : "Incompatível: Peso excede limite.");
                return compativel;
            }
            case DroneCargaViva droneVivo when transporte instanceof TransporteCargaViva transporteVivo -> {
                boolean compativel = droneVivo.getPesoMaximo() >= transporteVivo.getPeso();
                System.out.println(compativel ? "Compatível: Transporte Carga Viva." : "Incompatível: Peso excede limite.");
                return compativel;
            }
            default -> {
                System.out.println("Incompatível: Tipo de drone não suportado.");
                return false;
            }
        }
    }


    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Raio da Terra em quilômetros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distância em quilômetros
    }

    public void alterarSituacaoTransporte(int numero, TransporteStatus novaSituacao) throws Exception {
        Transporte transporte = transportes.stream()
                .filter(t -> t.getNumero() == numero)
                .findFirst()
                .orElseThrow(() -> new Exception("Transporte com o número " + numero + " não encontrado."));

        if (transporte.getSituacao() == TransporteStatus.TERMINADO || transporte.getSituacao() == TransporteStatus.CANCELADO) {
            throw new Exception("Não é possível alterar a situação de um transporte TERMINADO ou CANCELADO.");
        }

        transporte.setSituacao(novaSituacao);
    }

    public String gerarRelatorioGeral() {
        StringBuilder relatorio = new StringBuilder();

        relatorio.append("=== Drones Cadastrados ===\n");
        for (Drone drone : drones) {
            switch (drone) {
                case DronePessoal dp ->
                        relatorio.append(String.format("Drone Pessoal - Código: %d, Custo Fixo: %.2f, Autonomia: %.2f, Capacidade Máxima de Pessoas: %d\n",
                                dp.getCodigo(), dp.getCustoFixo(), dp.getAutonomia(), dp.getQtdMaxPessoas()));
                case DroneCargaInanimada dci ->
                        relatorio.append(String.format("Drone Carga Inanimada - Código: %d, Custo Fixo: %.2f, Autonomia: %.2f, Peso Máximo: %.2f, Proteção: %b\n",
                                dci.getCodigo(), dci.getCustoFixo(), dci.getAutonomia(), dci.getPesoMaximo(), dci.isProtecao()));
                case DroneCargaViva dcv ->
                        relatorio.append(String.format("Drone Carga Viva - Código: %d, Custo Fixo: %.2f, Autonomia: %.2f, Peso Máximo: %.2f, Climatizado: %b\n",
                                dcv.getCodigo(), dcv.getCustoFixo(), dcv.getAutonomia(), dcv.getPesoMaximo(), dcv.isClimatizado()));
                default ->
                        relatorio.append("Tipo de drone não identificado\n");
            }
        }

        relatorio.append("\n=== Transportes Cadastrados ===\n");
        for (Transporte transporte : transportes) {
            switch (transporte) {
                case TransportePessoal tp ->
                        relatorio.append(String.format("Transporte Pessoal - Número: %d, Cliente: %s, Descrição: %s, Peso: %.2f, Origem: (%.6f, %.6f), Destino: (%.6f, %.6f), Pessoas: %d, Situacao: %s\n",
                                tp.getNumero(), tp.getNomeCliente(), tp.getDescricao(), tp.getPeso(), tp.getLatitudeOrigem(), tp.getLongitudeOrigem(),
                                tp.getLatitudeDestino(), tp.getLongitudeDestino(), tp.getQtdPessoas(), tp.getSituacao()));
                case TransporteCargaInanimada tci ->
                        relatorio.append(String.format("Transporte Carga Inanimada - Número: %d, Cliente: %s, Descrição: %s, Peso: %.2f, Origem: (%.6f, %.6f), Destino: (%.6f, %.6f), Carga Perigosa: %b, Situacao: %s\n",
                                tci.getNumero(), tci.getNomeCliente(), tci.getDescricao(), tci.getPeso(), tci.getLatitudeOrigem(), tci.getLongitudeOrigem(),
                                tci.getLatitudeDestino(), tci.getLongitudeDestino(), tci.isCargaPerigosa(), tci.getSituacao()));
                case TransporteCargaViva tcv ->
                        relatorio.append(String.format("Transporte Carga Viva - Número: %d, Cliente: %s, Descrição: %s, Peso: %.2f, Origem: (%.6f, %.6f), Destino: (%.6f, %.6f), Temperatura Mínima: %.2f, Temperatura Máxima: %.2f, Situacao: %s\n",
                                tcv.getNumero(), tcv.getNomeCliente(), tcv.getDescricao(), tcv.getPeso(), tcv.getLatitudeOrigem(), tcv.getLongitudeOrigem(),
                                tcv.getLatitudeDestino(), tcv.getLongitudeDestino(), tcv.getTemperaturaMinima(), tcv.getTemperaturaMaxima(), tcv.getSituacao()));
                default ->
                        relatorio.append("Tipo de transporte não identificado\n");
            }
        }

        return relatorio.toString();
    }

    private void validarTransporteUnico(int numero) throws Exception {
        if (transportes.stream().anyMatch(t -> t.getNumero() == numero)) {
            throw new Exception("Erro: Número do transporte repetido.");
        }
    }

    public ArrayList<Drone> getDrones() {
        return drones;
    }

    @JsonIgnore
    public List<Transporte> getTodosTransportes() {
        return new ArrayList<>(transportes);
    }

    @JsonProperty("todosTransportes")
    private List<Transporte> getTransportesList() {
        return new ArrayList<>(transportes);
    }

    @JsonProperty("todosTransportes")
    private void setTransportesList(List<Transporte> transportes) {
        this.transportes = new LinkedList<>(transportes);
    }

    public Queue<Transporte> getTransportes() {
        return transportes;
    }

    public void setTransportes(Queue<Transporte> transportes) {
        this.transportes = transportes;
    }

}
