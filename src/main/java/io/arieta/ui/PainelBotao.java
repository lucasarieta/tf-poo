package io.arieta.ui;

import io.arieta.app.ACMEAirDrones;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PainelBotao extends JPanel {
    public PainelBotao(ACMEAirDrones acmeAirDrones) {
        setLayout(new GridLayout(3, 1, 10, 10));

        JButton carregarCSV = new JButton("Carregar arquivo CSV");
        JButton cadastrarDroneBtn = new JButton("Cadastrar drone");
        JButton cadastrarTransporteBtn = new JButton("Cadastrar transporte");

        Dimension botaoDimensao = new Dimension(200, 30); // Largura 200px, Altura 30px
        carregarCSV.setPreferredSize(botaoDimensao);
        cadastrarDroneBtn.setPreferredSize(botaoDimensao);
        cadastrarTransporteBtn.setPreferredSize(botaoDimensao);

        carregarCSV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cria um JFileChooser para selecionar o arquivo CSV
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Selecionar arquivo CSV");

                // Filtra apenas arquivos CSV
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos CSV", "csv"));

                int userSelection = fileChooser.showOpenDialog(PainelBotao.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToUpload = fileChooser.getSelectedFile();
                    acmeAirDrones.cadastrarDrones(fileToUpload);

                    JOptionPane.showMessageDialog(PainelBotao.this, "Arquivo carregado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        add(carregarCSV);
        add(cadastrarDroneBtn);
        add(cadastrarTransporteBtn);
    }
}
