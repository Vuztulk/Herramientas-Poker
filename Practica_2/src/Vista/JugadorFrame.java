package Vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class JugadorFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public JugadorFrame(int playerId) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(0, 0, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        JPanel panel_cartas = new JPanel();
        tabbedPane.addTab("Cards", null, panel_cartas, "Descripción del Panel 1");

        JPanel panel_preflop = new JPanel();
        tabbedPane.addTab("Preflop", null, panel_preflop, "Descripción del Panel 2");

        panel_preflop.setLayout(new GridLayout(13, 13, 2, 2));

        String[] valores = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};

        for (int i = 0; i < valores.length; i++) {
            for (int j = 0; j < valores.length; j++) {
                String botonTexto;
                Color backgroundColor;

                if (i == j) {
                    botonTexto = valores[i] + valores[i];
                    backgroundColor = new Color(144, 238, 144); // Verde claro para pares
                } else if (i < j) {
                    botonTexto = valores[i] + valores[j] + "s";
                    backgroundColor = new Color(255, 182, 193); // Rosa claro para suited
                } else {
                    botonTexto = valores[j] + valores[i] + "o"; // Invertimos el orden aquí
                    backgroundColor = new Color(173, 216, 230); // Azul claro para offsuit
                }

                JButton button = new JButton(botonTexto);
                button.setBackground(backgroundColor);
                button.setOpaque(true);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.addActionListener(e -> {
                    JButton sourceButton = (JButton) e.getSource();
                    sourceButton.setSelected(!sourceButton.isSelected());
                    sourceButton.setBackground(sourceButton.isSelected() ? Color.RED : backgroundColor);
                });
                panel_preflop.add(button);
            }
        }

        contentPane.add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }
}