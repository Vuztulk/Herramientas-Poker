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
    private JCheckBox[] playerCheckboxes;
    private JRadioButton boton_normal;
    private JRadioButton boton_omaha;
    
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
        
        boton_normal = new JRadioButton("Normal");
        boton_omaha = new JRadioButton("Omaha");

        boton_normal.setFont(new Font("Arial", Font.PLAIN, 15));
        boton_omaha.setFont(new Font("Arial", Font.PLAIN, 15));

        ButtonGroup grupoApartados = new ButtonGroup();
        grupoApartados.add(boton_normal);
        grupoApartados.add(boton_omaha);

        apartadosPanel.add(labelApartados);
        apartadosPanel.add(Box.createHorizontalStrut(10));
        apartadosPanel.add(boton_normal);
        apartadosPanel.add(boton_omaha);

        boton_normal.setSelected(true);

        return apartadosPanel;
    }
    
    public boolean isNormalSelected() {
        return boton_normal.isSelected();
    }
    
    public boolean isOmahaSelected() {
        return boton_omaha.isSelected();
    }
    
    public void setNormalSelected() {
        boton_normal.setSelected(true);
    }
    
    public void setOmahaSelected() {
        boton_omaha.setSelected(true);
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 5));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        botonBoard = new JButton("Board");
        botonJugadores = new JButton("Jugadores");
        botonNext = new JButton("Comenzar");

        panelBotones.add(botonBoard);
        panelBotones.add(botonJugadores);
        panelBotones.add(botonNext);

        JPanel playerCheckboxPanel = createPlayerCheckboxes();

        buttonPanel.add(panelBotones);
        buttonPanel.add(playerCheckboxPanel);

        return buttonPanel;
    }

    private JPanel createPlayerCheckboxes() {
        JPanel checkboxPanel = new JPanel(new GridLayout(2, 3, 10, 5));
        playerCheckboxes = new JCheckBox[6];

        for (int i = 0; i < 6; i++) {
            playerCheckboxes[i] = new JCheckBox("Jugador " + (i + 1));
            playerCheckboxes[i].setFont(new Font("Arial", Font.PLAIN, 14));
            playerCheckboxes[i].setSelected(true);
            checkboxPanel.add(playerCheckboxes[i]);
        }

        return checkboxPanel;
    }
    
    public boolean[] getJugadorCheckbox() {
        boolean[] checkbox = new boolean[6];
        for (int i = 0; i < 6; i++) {
        	checkbox[i] = playerCheckboxes[i].isSelected();
        }
        return checkbox;
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
        for (JCheckBox checkbox : playerCheckboxes) {
            checkbox.setSelected(true);
            checkbox.setEnabled(true);
        }
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
        
        if (phase == 0) {
            for (JCheckBox checkbox : playerCheckboxes) {
                checkbox.setSelected(true);
                checkbox.setEnabled(true);
            }
        } else {
            for (JCheckBox checkbox : playerCheckboxes) {
                checkbox.setEnabled(checkbox.isSelected());
            }
        }
    }
}