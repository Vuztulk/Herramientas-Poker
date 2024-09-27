package Vista;

import javax.swing.*;

import Modelo.RangoCartas;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class Vista extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField[] playerInputs;
    private JTextField[] equityFields;
    private JTextArea outputArea;

    public Vista() {
        setTitle("Hold'em");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel playerPanel = createPlayerPanel();
        playerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(playerPanel, BorderLayout.WEST);

        JPanel controlPanel = createControlPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(controlPanel, BorderLayout.EAST);

        outputArea = new JTextArea(10, 50);
        outputArea.setBorder(BorderFactory.createTitledBorder("Output"));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(scrollPane, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createPlayerPanel() {
        JPanel panel = new JPanel(new GridLayout(10, 3, 0, 5));

        playerInputs = new JTextField[10];
        equityFields = new JTextField[10];

        for (int i = 0; i < 10; i++) {
            JButton playerButton = new JButton("Jugador " + (i + 1));
            final int playerId = i + 1;
            playerButton.addActionListener(e -> {
                String rangoInput = playerInputs[playerId - 1].getText();
                new JugadorFrame(playerId, rangoInput);
            });
            panel.add(playerButton);

            playerInputs[i] = new JTextField(15);
            panel.add(playerInputs[i]);

            equityFields[i] = new JTextField(5);
            equityFields[i].setEditable(false);
            panel.add(equityFields[i]);
        }

        return panel;
    }


    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField boardField = new JTextField(15);
        JButton selectBoardButton = new JButton("Seleccionar");
        
        // Etiqueta Board
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Etiqueta ocupa 2 columnas
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 2, 5); // Menos espacio inferior entre la etiqueta y el cuadro de texto
        panel.add(new JLabel("Board:"), gbc);

        // Campo de texto board
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 5, 5, 5);
        panel.add(boardField, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 5, 5, 5);
        panel.add(selectBoardButton, gbc);

        // Campo de texto y boton Dead Cards
        JTextField deadCardsField = new JTextField(15);
        JButton selectDeadCardsButton = new JButton("Seleccionar");

        // Etiqueta Dead Cards
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; 
        gbc.insets = new Insets(5, 5, 2, 5);
        panel.add(new JLabel("Dead Cards:"), gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 5, 5, 5);
        gbc.gridx = 0;
        panel.add(deadCardsField, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 5, 5, 5);
        panel.add(selectDeadCardsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        JButton evaluateButton = new JButton("Evaluar");
        panel.add(evaluateButton, gbc);

        gbc.gridy = 5;
        JButton clearAllButton = new JButton("Limpiar Todos");
        panel.add(clearAllButton, gbc);

        // Radio buttons
        gbc.gridy = 6;
        JRadioButton enumerateAllButton = new JRadioButton("Enumerar Todos");
        JRadioButton monteCarloButton = new JRadioButton("Monte Carlo");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(enumerateAllButton);
        buttonGroup.add(monteCarloButton);

        panel.add(enumerateAllButton, gbc);
        gbc.gridy = 7;
        panel.add(monteCarloButton, gbc);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Vista::new);
    }
}
