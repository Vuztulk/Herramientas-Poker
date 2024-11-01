package Vista;

import javax.swing.*;
import Controlador.Controlador;
import java.awt.*;

public class MenuJugadores extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField[] entradasJugadores;
    private JTextField[] camposEquidad;
    private Controlador controlador;

    public MenuJugadores(Controlador controlador) {
        this.controlador = controlador;
        setTitle("Analizador de Rangos de Poker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelJugador = crearPanelJugador();
        panelJugador.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panelJugador, BorderLayout.WEST);

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
    
}
