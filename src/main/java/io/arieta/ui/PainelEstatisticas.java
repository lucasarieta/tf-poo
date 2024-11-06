package io.arieta.ui;

import javax.swing.*;
import java.awt.*;

public class PainelEstatisticas extends JPanel {
    public PainelEstatisticas() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Estatisticas"));

        // Componentes para exibir a quantidade de drones
        JLabel labelDrones = new JLabel("Quantidade de Drones:");
        JLabel labelDronesVivos = new JLabel(" - Carga Viva: 0");
        JLabel labelDronesInanimados = new JLabel(" - Carga Inanimada: 0");

        add(labelDrones);
        add(labelDronesVivos);
        add(labelDronesInanimados);

        // Componentes para exibir a quantidade de transportes
        JLabel labelTransportes = new JLabel("Quantidade de Transportes:");
        JLabel labelTransportesVivos = new JLabel(" - Carga Viva: 0");
        JLabel labelTransportesInanimados = new JLabel(" - Carga Inanimada: 0");

        add(Box.createVerticalStrut(2)); // Espaçamento entre seções
        add(labelTransportes);
        add(labelTransportesVivos);
        add(labelTransportesInanimados);
    }
}
