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

    private void cadastrarDrones() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                system.cadastrarDrones(selectedFile);
                outputArea.setText("Drones cadastrados com sucesso!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar drones: " + e.getMessage());
            }
        }
    }

    private void cadastrarTransportes() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                system.cadastrarTransportes(selectedFile);
                outputArea.setText("Transportes cadastrados com sucesso!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar transportes: " + e.getMessage());
            }
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
        try {
            List<Transporte> transportes = system.getTodosTransportes();
            StringBuilder sb = new StringBuilder();
            for (Transporte t : transportes) {
                sb.append(t.toString()).append("\n");
            }
            outputArea.setText(sb.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar transportes: " + e.getMessage());
        }
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
