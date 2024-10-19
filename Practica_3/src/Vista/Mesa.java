package Vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
	private JLabel labelBoardInicial;
	private JLabel labelBoardCartas;
	
	private List<String> listaCartasBoard;
	private List<List<String>> listaCartasJugadores;

	private boolean boardGenerado = false;
	private boolean playersGenerado = false;

	private List<List<String>> equity;

	private JLabel[] labelCartasBoard = new JLabel[5];
	private JLabel[][] labelCartasJugadores = new JLabel[6][4];
	private JLabel[] labelPorcentajes = new JLabel[6];
	
	private int fase = 0;

	private int[][] posicionesIniciales = { 
			{ 380, 340 }, //(Izquierda)
			{ 650, 100 }, //(Izquierda arriba)
			{ 650, 600 }, //(Izquierda abajo)
			{ 1360, 340 }, //(Derecha)
			{ 1050, 100 }, //(Derecha arriba)
			{ 1050, 600 } //(Derecha abajo)
	};

	private int[] desplazamientoX = { 30, 10, 10, 10 };
	private int[] desplazamientoY = { 20, 0, 0, 0 };

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

		JPanel boardPanel = new JPanel(new GridLayout(2, 1));
		boardPanel.setOpaque(false);
		boardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		labelBoardInicial = new JLabel("Board Inicial: ");
		labelBoardInicial.setForeground(Color.BLACK);
		labelBoardInicial.setFont(new Font("Arial", Font.PLAIN, 20));

		labelBoardCartas = new JLabel();
		labelBoardCartas.setForeground(Color.BLACK);
		labelBoardCartas.setFont(new Font("Arial", Font.PLAIN, 20));

		boardPanel.add(labelBoardInicial);
		boardPanel.add(labelBoardCartas);

		northPanel.add(boardPanel, BorderLayout.SOUTH);

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

		funcionalidadBotones();
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

	private void funcionalidadBotones() {
		botonBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listaCartasBoard = new ArrayList<>(mainFrame.mostrarMenuArchivos("Board").get(0));
			}
		});

		botonJugadores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listaCartasJugadores = mainFrame.mostrarMenuArchivos("Jugadores");
			}
		});

		botonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				switch (fase) {

				case 0: // Pre-flop
					List<String> cartasUsadas = new ArrayList<>();

					if (listaCartasBoard == null || listaCartasBoard.isEmpty()) {
						listaCartasBoard = controller.generateRandomBoard();
						boardGenerado = true;
					} else {
						boardGenerado = false;
					}
					cartasUsadas.addAll(listaCartasBoard);

					if (listaCartasJugadores == null || listaCartasJugadores.isEmpty()) {
						listaCartasJugadores = controller.generateRandomPlayerCards(cartasUsadas, 6);
						playersGenerado = true;
					} else {
						for (List<String> playerCards : listaCartasJugadores) {
							if (!playerCards.isEmpty()) {
								cartasUsadas.addAll(playerCards);
							}
						}
						List<List<String>> newPlayerCards = controller.generateRandomPlayerCards(cartasUsadas,getEmptyPlayerSlots());
						updateEmptyPlayerSlots(newPlayerCards);
						playersGenerado = getEmptyPlayerSlots() > 0;
					}
					pintaCartasJugadores(2);
					pintaCartasBoard();

					actualizarTexto();

					fase++;
					break;

				case 1: // Flop

					fase++;
					break;

				case 2: // Turn

					fase++;
					break;

				case 3: // River

					fase++;
					break;

				default:
					break;

				}
			}

		});
	}

	private void actualizarTexto() {
		if (boardGenerado) {
			labelBoardInicial.setText("Board Inicial (generado):");
		} else {
			labelBoardInicial.setText("Board Inicial (cargado):");
		}
		labelBoardCartas.setText(String.join(", ", listaCartasBoard));

		StringBuilder playerCardsText = new StringBuilder();
		if (playersGenerado) {
			playerCardsText.append("Cartas de jugadores generadas:\n\n");
		} else {
			playerCardsText.append("Cartas de jugadores cargadas:\n\n");
		}

		for (int i = 0; i < listaCartasJugadores.size(); i++) {
			playerCardsText.append("Jugador ").append(i + 1).append(": ").append(String.join(", ", listaCartasJugadores.get(i))).append("\n");
		}
		textoSalida.setText(playerCardsText.toString());
	}

	private int getEmptyPlayerSlots() {
		int emptySlots = 0;
		for (List<String> playerCards : listaCartasJugadores) {
			if (playerCards.isEmpty()) {
				emptySlots++;
			}
		}
		return emptySlots;
	}

	private void updateEmptyPlayerSlots(List<List<String>> newPlayerCards) {
		int index = 0;
		for (int i = 0; i < listaCartasJugadores.size(); i++) {
			if (listaCartasJugadores.get(i).isEmpty()) {
				listaCartasJugadores.set(i, newPlayerCards.get(index++));
			}
		}
	}

	private void pintaCartasJugadores(int num_cartas_jugadores) {

		for (int jugador = 0; jugador < 6; jugador++) {
			List<String> cartasJugador = listaCartasJugadores.get(jugador);
			for (int i = 0; i < num_cartas_jugadores; i++) {
				if (i < cartasJugador.size()) {
					String carta = cartasJugador.get(i);
					String imagePath = UtilidadesGUI.getCartaPath(carta);

					ImageIcon icon = new ImageIcon(imagePath);
					Image image = icon.getImage().getScaledInstance(70, 95, Image.SCALE_SMOOTH);
					JLabel cartaLabel = new JLabel(new ImageIcon(image));

					int x = posicionesIniciales[jugador][0] + (desplazamientoX[i]);
					int y = posicionesIniciales[jugador][1] + (desplazamientoY[i]);

					cartaLabel.setBounds(x, y, 70, 95);
					add(cartaLabel);
					labelCartasJugadores[jugador][i] = cartaLabel;
				}
			}

			labelPorcentajes[jugador] = new JLabel(2 + "%");
			labelPorcentajes[jugador].setFont(new Font("Arial", Font.BOLD, 20));
			labelPorcentajes[jugador].setForeground(Color.RED);

	        int x = posicionesIniciales[jugador][0] - 40;
	        int y = posicionesIniciales[jugador][1] + 50;
	        labelPorcentajes[jugador].setBounds(x, y, 100, 20);

	        add(labelPorcentajes[jugador]);
		}

		revalidate();
		repaint();
	}

	private void pintaCartasBoard() {

		for (int i = 0; i < Math.min(3, listaCartasBoard.size()); i++) {
			String carta = listaCartasBoard.get(i);
			String imagePath = UtilidadesGUI.getCartaPath(carta);

			ImageIcon icon = new ImageIcon(imagePath);
			Image image = icon.getImage().getScaledInstance(70, 95, Image.SCALE_SMOOTH);
			JLabel cartaLabel = new JLabel(new ImageIcon(image));

			int x = 720 + (i * 80);

			cartaLabel.setBounds(x, 350, 70, 95);
			add(cartaLabel);
			labelCartasBoard[i] = cartaLabel;
		}

		revalidate();
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 300, 0, getWidth() - 300, getHeight(), this);
	}
}