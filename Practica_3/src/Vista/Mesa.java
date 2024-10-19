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

	private List<String> cartas_board;
	private List<List<String>> cartas_jugadores;

	private boolean boardGenerado = false;
	private boolean playersGenerado = false;

	private List<String> resultados;
	private List<List<String>> res_jugadores;
	private List<String> resultadosText;

	private JLabel labelBoardInicial;
	private JLabel labelBoardCartas;

	private JLabel[][] cartasJugadores = new JLabel[6][4];
	private JLabel[] cartasBoard = new JLabel[5];

	private int fase = 0;

	private int[][] posicionesIniciales = { { 1260, 200 }, // Jugador 1
			{ 300, 200 }, // Jugador 2
			{ 700, 200 }, // Jugador 3
			{ 500, 600 }, // Jugador 4
			{ 700, 600 }, // Jugador 5
			{ 750, 100 } // Jugador 6

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
				cartas_board = new ArrayList<>(mainFrame.mostrarMenuArchivos("Board").get(0));
			}
		});

		botonJugadores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cartas_jugadores = mainFrame.mostrarMenuArchivos("Jugadores");
			}
		});

		botonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				switch (fase) {

				case 0: // Pre-flop
					List<String> usedCards = new ArrayList<>();

					if (cartas_board == null || cartas_board.isEmpty()) {
						cartas_board = controller.generateRandomBoard();
						boardGenerado = true;
					} else {
						boardGenerado = false;
					}
					usedCards.addAll(cartas_board);

					if (cartas_jugadores == null || cartas_jugadores.isEmpty()) {
						cartas_jugadores = controller.generateRandomPlayerCards(usedCards, 6);
						playersGenerado = true;
					} else {
						for (List<String> playerCards : cartas_jugadores) {
							if (!playerCards.isEmpty()) {
								usedCards.addAll(playerCards);
							}
						}
						List<List<String>> newPlayerCards = controller.generateRandomPlayerCards(usedCards,
								getEmptyPlayerSlots());
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
		labelBoardCartas.setText(String.join(", ", cartas_board));

		StringBuilder playerCardsText = new StringBuilder();
		if (playersGenerado) {
			playerCardsText.append("Cartas de jugadores generadas:\n\n");
		} else {
			playerCardsText.append("Cartas de jugadores cargadas:\n\n");
		}

		for (int i = 0; i < cartas_jugadores.size(); i++) {
			playerCardsText.append("Jugador ").append(i + 1).append(": ")
					.append(String.join(", ", cartas_jugadores.get(i))).append("\n");
		}
		textoSalida.setText(playerCardsText.toString());
	}

	private int getEmptyPlayerSlots() {
		int emptySlots = 0;
		for (List<String> playerCards : cartas_jugadores) {
			if (playerCards.isEmpty()) {
				emptySlots++;
			}
		}
		return emptySlots;
	}

	private void updateEmptyPlayerSlots(List<List<String>> newPlayerCards) {
		int index = 0;
		for (int i = 0; i < cartas_jugadores.size(); i++) {
			if (cartas_jugadores.get(i).isEmpty()) {
				cartas_jugadores.set(i, newPlayerCards.get(index++));
			}
		}
	}

	private void pintaCartasJugadores(int num_cartas_jugadores) {
		
		for (int jugador = 0; jugador < 6; jugador++) {
			List<String> cartasJugador = cartas_jugadores.get(jugador);

			for (int i = 0; i < num_cartas_jugadores; i++) {
				if (i < cartasJugador.size()) {
					String carta = cartasJugador.get(i);
					String imagePath = UtilidadesGUI.getCartaPath(carta);

					ImageIcon icon = new ImageIcon(imagePath);
					Image image = icon.getImage().getScaledInstance(70, 95, Image.SCALE_SMOOTH);
					JLabel cartaLabel = new JLabel(new ImageIcon(image));

					int x = posicionesIniciales[jugador][0] + (desplazamientoX[jugador % 2] * i);
					int y = posicionesIniciales[jugador][1] + (desplazamientoY[jugador % 2] * i);

					cartaLabel.setBounds(x, y, 70, 95);
					add(cartaLabel);
					cartasJugadores[jugador][i] = cartaLabel;
				}
			}
		}
	}

	private void pintaCartasBoard() {

		for (int i = 0; i < Math.min(3, cartas_board.size()); i++) {
			String carta = cartas_board.get(i);
			String imagePath = UtilidadesGUI.getCartaPath(carta);

			ImageIcon icon = new ImageIcon(imagePath);
			Image image = icon.getImage().getScaledInstance(70, 95, Image.SCALE_SMOOTH);
			JLabel cartaLabel = new JLabel(new ImageIcon(image));

			int x = 720 + (i * 80);
			int y = 350;

			cartaLabel.setBounds(x, y, 70, 95);
			add(cartaLabel);
			cartasBoard[i] = cartaLabel;
		}

		revalidate();
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 300, 0, getWidth() - 300, getHeight(), this);
	}
}