package io.arieta.ui;

import io.arieta.app.ACMEAirDrones;
import io.arieta.dados.transporte.Transporte;
import io.arieta.dados.transporte.TransporteStatus;
import io.arieta.util.*;
import io.arieta.dados.drone.*;
import io.arieta.dados.transporte.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainFrame extends JFrame {
    private final ACMEAirDrones system;
    private JTextArea outputArea;

    public MainFrame(ACMEAirDrones system) {
        this.system = system;
        initComponents();
    }

    private void initComponents() {
        setTitle("ACMEAirDrones System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton cadastrarDronesBtn = new JButton("Cadastrar Drones");
        JButton cadastrarTransportesBtn = new JButton("Cadastrar Transportes");
        JButton processarTransportesBtn = new JButton("Processar Transportes Pendentes");
        JButton mostrarRelatorioBtn = new JButton("Mostrar Relatório Geral");
        JButton mostrarTransportesBtn = new JButton("Mostrar Todos os Transportes");
        JButton alterarSituacaoBtn = new JButton("Alterar Situação de Transporte");
        JButton carregarDadosBtn = new JButton("Carregar Dados");
        JButton salvarDadosBtn = new JButton("Salvar Dados");

        buttonPanel.add(cadastrarDronesBtn);
        buttonPanel.add(cadastrarTransportesBtn);
        buttonPanel.add(processarTransportesBtn);
        buttonPanel.add(mostrarRelatorioBtn);
        buttonPanel.add(mostrarTransportesBtn);
        buttonPanel.add(alterarSituacaoBtn);
        buttonPanel.add(carregarDadosBtn);
        buttonPanel.add(salvarDadosBtn);

        cadastrarDronesBtn.addActionListener(e -> cadastrarDrones());
        cadastrarTransportesBtn.addActionListener(e -> cadastrarTransportes());
        processarTransportesBtn.addActionListener(e -> processarTransportes());
        mostrarRelatorioBtn.addActionListener(e -> mostrarRelatorio());
        mostrarTransportesBtn.addActionListener(e -> mostrarTransportes());
        alterarSituacaoBtn.addActionListener(e -> alterarSituacaoTransporte());
        carregarDadosBtn.addActionListener(e -> carregarDados());
        salvarDadosBtn.addActionListener(e -> salvarDados());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JButton sairProgramaBtn = new JButton("Sair do programa");
        sairProgramaBtn.addActionListener(e -> System.exit(0));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(sairProgramaBtn);
        sairProgramaBtn.addActionListener(e -> System.exit(0));

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);


    }

    public void cadastrarDrones() {
        String[] opcoes = {"Preencher Manualmente", "Enviar Arquivo"};
        String escolha = (String) JOptionPane.showInputDialog(
                null,
                "Como deseja cadastrar o(s) drone(s)?",
                "Cadastrar Drone",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == null) return; // Cancelado

        if (escolha.equals("Preencher Manualmente")) {
            cadastrarDroneInterativo();
        } else if (escolha.equals("Enviar Arquivo")) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File arquivo = fileChooser.getSelectedFile();
                try {
                    system.cadastrarDrones(arquivo);
                    JOptionPane.showMessageDialog(null, "Drones cadastrados com sucesso a partir do arquivo!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Erro ao processar arquivo: " + e.getMessage());
                }
            }
        }
    }

    public void cadastrarTransportes() {
        String[] opcoes = {"Preencher Manualmente", "Enviar Arquivo"};
        String escolha = (String) JOptionPane.showInputDialog(
                null,
                "Como deseja cadastrar o(s) transporte(s)?",
                "Cadastrar Transporte",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == null) return; // Cancelado

        if (escolha.equals("Preencher Manualmente")) {
            cadastrarTransporteInterativo();
        } else if (escolha.equals("Enviar Arquivo")) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File arquivo = fileChooser.getSelectedFile();
                try {
                    system.cadastrarTransportes(arquivo);
                    JOptionPane.showMessageDialog(null, "Transportes cadastrados com sucesso a partir do arquivo!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Erro ao processar arquivo: " + e.getMessage());
                }
            }
        }
    }


    public void cadastrarDroneInterativo() {
        String[] opcoes = {"Pessoal", "Carga Inanimada", "Carga Viva"};
        String tipo = (String) JOptionPane.showInputDialog(null, "Selecione o tipo de drone:",
                "Cadastrar Drone", JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

        if (tipo == null) return; // Cancelado

        try {
            int codigo = Integer.parseInt(JOptionPane.showInputDialog("Digite o código do drone:"));
            double custoFixo = Double.parseDouble(JOptionPane.showInputDialog("Digite o custo fixo:"));
            double autonomia = Double.parseDouble(JOptionPane.showInputDialog("Digite a autonomia:"));

            switch (tipo) {
                case "Pessoal" -> {
                    int qtdMaxPessoas = Integer.parseInt(JOptionPane.showInputDialog("Digite a capacidade máxima de pessoas:"));
                    system.getDrones().add(new DronePessoal(codigo, custoFixo, autonomia, qtdMaxPessoas));
                }
                case "Carga Inanimada" -> {
                    double pesoMaximo = Double.parseDouble(JOptionPane.showInputDialog("Digite o peso máximo:"));
                    boolean protecao = JOptionPane.showConfirmDialog(null, "Possui proteção?",
                            "Proteção", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                    system.getDrones().add(new DroneCargaInanimada(codigo, custoFixo, autonomia, pesoMaximo, protecao));
                }
                case "Carga Viva" -> {
                    double pesoMaximo = Double.parseDouble(JOptionPane.showInputDialog("Digite o peso máximo:"));
                    boolean climatizado = JOptionPane.showConfirmDialog(null, "É climatizado?",
                            "Climatizado", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                    system.getDrones().add(new DroneCargaViva(codigo, custoFixo, autonomia, pesoMaximo, climatizado));
                }
            }
            JOptionPane.showMessageDialog(null, "Drone cadastrado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar drone: " + e.getMessage());
        }
    }

    public void cadastrarTransporteInterativo() {
        String[] opcoes = {"Pessoal", "Carga Inanimada", "Carga Viva"};
        String tipo = (String) JOptionPane.showInputDialog(null, "Selecione o tipo de transporte:",
                "Cadastrar Transporte", JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

        if (tipo == null) return; // Cancelado

        try {
            int numero = Integer.parseInt(JOptionPane.showInputDialog("Digite o número do transporte:"));
            String nomeCliente = JOptionPane.showInputDialog("Digite o nome do cliente:");
            String descricao = JOptionPane.showInputDialog("Digite a descrição:");
            double peso = Double.parseDouble(JOptionPane.showInputDialog("Digite o peso:"));
            double latOrigem = Double.parseDouble(JOptionPane.showInputDialog("Digite a latitude de origem:"));
            double longOrigem = Double.parseDouble(JOptionPane.showInputDialog("Digite a longitude de origem:"));
            double latDestino = Double.parseDouble(JOptionPane.showInputDialog("Digite a latitude de destino:"));
            double longDestino = Double.parseDouble(JOptionPane.showInputDialog("Digite a longitude de destino:"));

            switch (tipo) {
                case "Pessoal" -> {
                    int qtdPessoas = Integer.parseInt(JOptionPane.showInputDialog("Digite a quantidade de pessoas:"));
                    system.getTransportes().add(new TransportePessoal(numero, nomeCliente, descricao, peso, latOrigem,
                            longOrigem, latDestino, longDestino, qtdPessoas));
                }
                case "Carga Inanimada" -> {
                    boolean cargaPerigosa = JOptionPane.showConfirmDialog(null, "A carga é perigosa?",
                            "Carga Perigosa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                    system.getTransportes().add(new TransporteCargaInanimada(numero, nomeCliente, descricao, peso,
                            latOrigem, longOrigem, latDestino, longDestino, cargaPerigosa));
                }
                case "Carga Viva" -> {
                    double tempMin = Double.parseDouble(JOptionPane.showInputDialog("Digite a temperatura mínima:"));
                    double tempMax = Double.parseDouble(JOptionPane.showInputDialog("Digite a temperatura máxima:"));
                    system.getTransportes().add(new TransporteCargaViva(numero, nomeCliente, descricao, peso, latOrigem,
                            longOrigem, latDestino, longDestino, tempMin, tempMax));
                }
            }
            JOptionPane.showMessageDialog(null, "Transporte cadastrado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar transporte: " + e.getMessage());
        }
    }

    private void processarTransportes() {
        try {
            system.processarTransportesPendentes();
            outputArea.setText("Transportes pendentes processados!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao processar transportes: " + e.getMessage());
        }
    }

    private void mostrarRelatorio() {
        try {
            String relatorio = system.gerarRelatorioGeral();
            outputArea.setText(relatorio);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + e.getMessage());
        }
    }

    private void mostrarTransportes() {
        StringBuilder relatorio = new StringBuilder();
        try {
            List<Transporte> transportes = system.getTodosTransportes();

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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar transportes: " + e.getMessage());
        }
        String relatString = String.valueOf(relatorio);
        outputArea.setText(relatString);
    }

    private void alterarSituacaoTransporte() {
        String numeroStr = JOptionPane.showInputDialog(this, "Digite o número do transporte:");
        if (numeroStr != null && !numeroStr.isEmpty()) {
            try {
                int numero = Integer.parseInt(numeroStr);
                String[] opcoes = {"PENDENTE", "ALOCADO", "TERMINADO", "CANCELADO"};
                String novaSituacaoStr = (String) JOptionPane.showInputDialog(this,
                        "Escolha a nova situação:",
                        "Alterar Situação",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opcoes,
                        opcoes[0]);
                if (novaSituacaoStr != null) {
                    TransporteStatus novaSituacao = TransporteStatus.valueOf(novaSituacaoStr);
                    system.alterarSituacaoTransporte(numero, novaSituacao);
                    outputArea.setText(String.format("Situação do transporte %d alterada para %s.", numero, novaSituacao));
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Número de transporte inválido!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao alterar situação: " + e.getMessage());
            }
        }
    }

    private void carregarDados() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            try {
                if (filePath.endsWith(".json")) {
                    system.carregarDadosJSON(filePath);
                    outputArea.setText("Dados carregados do JSON com sucesso!");
                } else if (filePath.endsWith(".xml")) {
                    system.carregarDadosXML(filePath);
                    outputArea.setText("Dados carregados do XML com sucesso!");
                } else if (filePath.endsWith(".csv")) {
                    CsvHandler csvHandler = new CsvHandler();
                    if (filePath.contains("DRONES")) {
                        List<Drone> drones = csvHandler.readDronesFromCsv(filePath);
                        system.getDrones().addAll(drones);
                    } else if (filePath.contains("TRANSPORTES")) {
                        List<Transporte> transportes = csvHandler.readTransportesFromCsv(filePath);
                        system.getTransportes().addAll(transportes);
                    }
                    outputArea.setText("Dados carregados do CSV com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(this, "Formato de arquivo não suportado!");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage());
            }
        }
    }

    private void salvarDados() {
        String filePath = JOptionPane.showInputDialog(this, "Digite o nome do arquivo para salvar (com extensão .json):");
        if (filePath != null && !filePath.isEmpty()) {
            try {
                if (!filePath.endsWith(".json")) {
                    filePath += ".json";
                }
                system.salvarDadosJSON(filePath);
                outputArea.setText("Dados salvos em JSON com sucesso no arquivo: " + filePath);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar dados em JSON: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
