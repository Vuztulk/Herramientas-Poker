package Vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Controlador.Controller;

public class Mesa extends JPanel {

    private static final long serialVersionUID = 1L;
    private Controller controller;
    private MainFrame mainFrame;
    private JTextArea textoSalida;
    private Image backgroundImage;
    private JButton botonBoard;
    private JButton botonJugadores;
    private JButton botonNext;
    
    private List<List<String>> cartas_board;
    private List<List<String>> cartas_jugadores;
    private List<String> resultados;
    private List<List<String>> res_jugadores;
    private List<String> resultadosText;

    private JLabel labelBoardInicial;
    private JLabel labelBoard;

    private JLabel[][] cartasJugadores = new JLabel[4][4];
    private JLabel[] cartasBoard = new JLabel[5];

    private int fase = 0;

    private int[][] posicionesIniciales = { 
            { 1260, 200 }, // Jugador 1 (Derecha)
            { 750, 600 }, // Jugador 2 (Abajo)
            { 750, 100 }, // Jugador 3 (Arriba)
            { 500, 200 } // Jugador 4 (Izquierda)
    };

    private int[] desplazamientoX = { 0, 80, 80, 0 };
    private int[] desplazamientoY = { 100, 0, 0, 100 };

    public Mesa(Controller controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        backgroundImage = new ImageIcon("src/Vista/Imagenes/Poker_Board.jpg").getImage();

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JPanel apartadosPanel = createApartadosPanel();
        northPanel.add(apartadosPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);

        botonBoard = new JButton("Board");
        botonJugadores = new JButton("Jugadores ");
        botonNext = new JButton("Comenzar / Next");

        buttonPanel.add(botonBoard);
        buttonPanel.add(botonJugadores);
        buttonPanel.add(botonNext);

        
        northPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel boardLabelsPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        boardLabelsPanel.setOpaque(false);
        boardLabelsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        labelBoardInicial = new JLabel("Board Inicial: ");
        labelBoardInicial.setForeground(Color.BLACK);
        labelBoardInicial.setFont(new Font("Arial", Font.PLAIN, 20));

        labelBoard = new JLabel("");
        labelBoard.setForeground(Color.BLACK);
        labelBoard.setFont(new Font("Arial", Font.PLAIN, 20));

        boardLabelsPanel.add(labelBoardInicial);
        boardLabelsPanel.add(labelBoard);

        northPanel.add(boardLabelsPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);

        textoSalida = new JTextArea();
        textoSalida.setEditable(false);
        textoSalida.setLineWrap(true);
        textoSalida.setWrapStyleWord(true);
        textoSalida.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(textoSalida);
        scrollPane.setPreferredSize(new Dimension(280, 500));
        scrollPane.setBorder(new EmptyBorder(0, 10, 10, 10));
        add(scrollPane, BorderLayout.WEST);

        setupButtonListeners();
    }

    private JPanel createApartadosPanel() {
        JPanel apartadosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        apartadosPanel.setOpaque(false);

        JLabel labelApartados = new JLabel(" Apartados:");
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

    private void setupButtonListeners() {
    	botonBoard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	cartas_board = mainFrame.mostrarMenuArchivos("Board");              
            }
        });
    	
    	botonJugadores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	cartas_jugadores = mainFrame.mostrarMenuArchivos("Jugadores");      
            }
        });

        botonNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }



    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 300, 0, getWidth() - 300, getHeight(), this);
    }
}