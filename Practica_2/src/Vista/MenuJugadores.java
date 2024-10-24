package Vista;

import javax.swing.*;
import Controlador.Controlador;
import java.awt.*;

public class MenuJugadores extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField[] entradasJugadores;
    private JTextField[] camposEquidad;
    private JTextArea areaSalida;
    private Controlador controlador;

    public MenuJugadores(Controlador controlador) {
        this.controlador = controlador;
        setTitle("Analizador de Rangos de Poker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelJugador = crearPanelJugador();
        panelJugador.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panelJugador, BorderLayout.WEST);

        /*JPanel panelControl = crearPanelControl();
        panelControl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panelControl, BorderLayout.EAST);

        areaSalida = new JTextArea(10, 50);
        areaSalida.setBorder(BorderFactory.createTitledBorder("Salida"));
        JScrollPane scrollPane = new JScrollPane(areaSalida);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(scrollPane, BorderLayout.SOUTH);*/

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel crearPanelJugador() {
        JPanel panel = new JPanel(new GridLayout(10, 3, 0, 5));

        entradasJugadores = new JTextField[10];
        camposEquidad = new JTextField[10];

        for (int i = 0; i < 10; i++) {
            JButton botonJugador = new JButton("Jugador " + (i + 1));
            final int idJugador = i + 1;
            botonJugador.addActionListener(e -> {
                String rangoInput = entradasJugadores[idJugador - 1].getText();
                new JugadorFrame(controlador, idJugador, rangoInput, entradasJugadores[idJugador - 1]); // Le pasamos el input del jugador para que cuando se elija gráficamente se ponga aqui
            });
            panel.add(botonJugador);

            entradasJugadores[i] = new JTextField(15);
            panel.add(entradasJugadores[i]);

            camposEquidad[i] = new JTextField(5);
            camposEquidad[i].setEditable(false);
            panel.add(camposEquidad[i]);
        }

        return panel;
    }

    private JPanel crearPanelControl() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField campoBoard = new JTextField(15);
        JButton botonSeleccionarBoard = new JButton("Seleccionar");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 2, 5);
        panel.add(new JLabel("Board:"), gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 5, 5, 5);
        panel.add(campoBoard, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 5, 5, 5);
        panel.add(botonSeleccionarBoard, gbc);

        JTextField campoCartasMuertas = new JTextField(15);
        JButton botonSeleccionarCartasMuertas = new JButton("Seleccionar");

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; 
        gbc.insets = new Insets(5, 5, 2, 5);
        panel.add(new JLabel("Cartas Muertas:"), gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 5, 5, 5);
        gbc.gridx = 0;
        panel.add(campoCartasMuertas, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 5, 5, 5);
        panel.add(botonSeleccionarCartasMuertas, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        JButton botonEvaluar = new JButton("Evaluar");
        panel.add(botonEvaluar, gbc);

        gbc.gridy = 5;
        JButton botonLimpiarTodos = new JButton("Limpiar Todos");
        botonLimpiarTodos.addActionListener(e -> limpiarInputs());
        panel.add(botonLimpiarTodos, gbc);

        gbc.gridy = 6;
        JRadioButton botonEnumerarTodos = new JRadioButton("Enumerar Todos");
        JRadioButton botonMonteCarlo = new JRadioButton("Monte Carlo");

        ButtonGroup grupoBotones = new ButtonGroup();
        grupoBotones.add(botonEnumerarTodos);
        grupoBotones.add(botonMonteCarlo);

        panel.add(botonEnumerarTodos, gbc);
        gbc.gridy = 7;
        panel.add(botonMonteCarlo, gbc);

        return panel;
    }

    private void limpiarInputs() {
        for (int i = 0; i < entradasJugadores.length; i++) {
            entradasJugadores[i].setText("");
            camposEquidad[i].setText("");
            
            controlador.setRangoJugador(i + 1, "");
            controlador.setPorcentajeJugador(i + 1, 0.0);
        }

        areaSalida.setText("");
    }
}
