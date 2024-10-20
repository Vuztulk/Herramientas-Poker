package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
    private Mesa mesa;
    private JButton botonBoard;
    private JButton botonJugadores;
    private JButton botonNext;
    private JLabel labelBoardInicial;
    private JLabel labelBoardCartas;
    
    public ControlPanel(Mesa mesa) {
        this.mesa = mesa;
        setLayout(new BorderLayout());
        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        
        JPanel apartadosPanel = createApartadosPanel();
        northPanel.add(apartadosPanel, BorderLayout.NORTH);
        
        JPanel buttonPanel = createButtonPanel();
        northPanel.add(buttonPanel, BorderLayout.CENTER);
        
        JPanel boardPanel = createBoardPanel();
        northPanel.add(boardPanel, BorderLayout.SOUTH);
        
        add(northPanel, BorderLayout.NORTH);
        
        initializeButtonListeners();
    }
    
    public JButton getBotonBoard() { return botonBoard; }
    public JButton getBotonJugadores() { return botonJugadores; }
    public JButton getBotonNext() { return botonNext; }
    public JLabel getLabelBoardInicial() { return labelBoardInicial; }
    public JLabel getLabelBoardCartas() { return labelBoardCartas; }
    
    private JPanel createApartadosPanel() {
        JPanel apartadosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        apartadosPanel.setOpaque(false);

        JLabel labelApartados = new JLabel("Apartados:");
        labelApartados.setForeground(Color.BLACK);
        labelApartados.setFont(new Font("Arial", Font.PLAIN, 20));

        JRadioButton apartado1 = new JRadioButton("Normal");
        JRadioButton apartado2 = new JRadioButton("Omaha");

        apartado1.setFont(new Font("Arial", Font.PLAIN, 15));
        apartado2.setFont(new Font("Arial", Font.PLAIN, 15));

        ButtonGroup grupoApartados = new ButtonGroup();
        grupoApartados.add(apartado1);
        grupoApartados.add(apartado2);

        apartadosPanel.add(labelApartados);
        apartadosPanel.add(Box.createHorizontalStrut(10));
        apartadosPanel.add(apartado1);
        apartadosPanel.add(apartado2);

        apartado1.setSelected(true);

        return apartadosPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));

        botonBoard = new JButton("Board");
        botonJugadores = new JButton("Jugadores");
        botonNext = new JButton("Comenzar");

        buttonPanel.add(botonBoard);
        buttonPanel.add(botonJugadores);
        buttonPanel.add(botonNext);

        return buttonPanel;
    }

    
    private JPanel createBoardPanel() {
        JPanel boardPanel = new JPanel(new GridLayout(2, 1));
        labelBoardInicial = new JLabel("Board Inicial: ");
        labelBoardInicial.setFont(new Font("Arial", Font.PLAIN, 20));
        labelBoardCartas = new JLabel();
        boardPanel.add(labelBoardInicial);
        boardPanel.add(labelBoardCartas);
        return boardPanel;
    }
    
    private void initializeButtonListeners() {
    	botonBoard.addActionListener(e -> mesa.loadBoardFromFile());
        botonJugadores.addActionListener(e -> mesa.loadPlayersFromFile());
        botonNext.addActionListener(e -> mesa.next());
    }

    
    public void resetControls() {
        botonBoard.setEnabled(true);
        botonJugadores.setEnabled(true);
        botonNext.setText("Comenzar");
    }

    public void updatePhaseLabel(int phase) {
        String phaseText;
        switch (phase) {
            case 0: phaseText = "Pre-flop"; break;
            case 1: phaseText = "Flop"; break;
            case 2: phaseText = "Turn"; break;
            case 3: phaseText = "River"; break;
            default: phaseText = "Fin de la mano";
        }
        labelBoardInicial.setText("Fase: " + phaseText);
        botonNext.setText(phase < 3 ? "Siguiente" : "Nueva mano");
    }
}