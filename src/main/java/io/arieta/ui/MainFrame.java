package io.arieta.ui;

import io.arieta.app.ACMEAirDrones;
import io.arieta.dados.transporte.Transporte;
import io.arieta.dados.transporte.TransporteStatus;

import javax.swing.*;
import java.awt.*;
import java.io.File;
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

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void cadastrarDrones() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            system.cadastrarDrones(selectedFile);
            outputArea.setText("Drones cadastrados com sucesso!");
        }
    }

    private void cadastrarTransportes() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            system.cadastrarTransportes(selectedFile);
            outputArea.setText("Transportes cadastrados com sucesso!");
        }
    }

    private void processarTransportes() {
        system.processarTransportesPendentes();
        outputArea.setText("Transportes pendentes processados!");
    }

    private void mostrarRelatorio() {
        String relatorio = system.gerarRelatorioGeral();
        outputArea.setText(relatorio);
    }

    private void mostrarTransportes() {
        List<Transporte> transportes = system.getTodosTransportes();
        StringBuilder sb = new StringBuilder();
        for (Transporte t : transportes) {
            sb.append(t.toString()).append("\n");
        }
        outputArea.setText(sb.toString());
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
                    outputArea.setText("Situação do transporte " + numero + " alterada para " + novaSituacao);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Número de transporte inválido!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void carregarDados() {
        String nomeArquivo = JOptionPane.showInputDialog(this, "Digite o nome do arquivo para carregar (sem extensão):");
        if (nomeArquivo != null && !nomeArquivo.isEmpty()) {
            try {
                system.carregarDados(nomeArquivo);
                outputArea.setText("Dados carregados com sucesso!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage());
            }
        }
    }

    private void salvarDados() {
        String nomeArquivo = JOptionPane.showInputDialog(this, "Digite o nome do arquivo para salvar (sem extensão):");
        if (nomeArquivo != null && !nomeArquivo.isEmpty()) {
            try {
                system.salvarDados(nomeArquivo);
                outputArea.setText("Dados salvos com sucesso!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar dados: " + e.getMessage());
            }
        }
    }
}
