package io.arieta.ui;

import io.arieta.app.ACMEAirDrones;

import javax.swing.*;
import java.awt.*;

public class UI extends JFrame {
    public UI(ACMEAirDrones acmeAirDrones) {
        setTitle("ACMEAirDrones");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        PainelBotao painelBotao = new PainelBotao(acmeAirDrones);
        PainelEstatisticas painelEstatisticas = new PainelEstatisticas();

        setLayout(new BorderLayout());
        add(painelBotao, BorderLayout.WEST);
        add(painelEstatisticas, BorderLayout.EAST);

        setVisible(true);
    }
}
