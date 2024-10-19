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

	private int[][] posicionesIniciales = { { 80, 340 }, // (Izquierda)
			{ 350, 100 }, // (Izquierda arriba)
			{ 350, 600 }, // (Izquierda abajo)
			{ 1060, 340 }, // (Derecha)
			{ 750, 100 }, // (Derecha arriba)
			{ 750, 600 } // (Derecha abajo)
	};

	private int[] desplazamientoX = { 30, 10, 10, 10 };
	private int[] desplazamientoY = { 20, 0, 0, 0 };

	public Mesa(Controller controller, MainFrame mainFrame) {
		this.controller = controller;
		this.mainFrame = mainFrame;

		setLayout(new BorderLayout());

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setResizeWeight(0.1);

		JPanel leftPanel = createLeftPanel();
		splitPane.setLeftComponent(leftPanel);

		JPanel rightPanel = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
			}
		};
		rightPanel.setLayout(null);
		splitPane.setRightComponent(rightPanel);

		add(splitPane, BorderLayout.CENTER);

		backgroundImage = new ImageIcon("src/Vista/Imagenes/Poker_Board.jpg").getImage();

		funcionalidadBotones();
	}

	private JPanel createLeftPanel() {
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setOpaque(false);

		JPanel apartadosPanel = createApartadosPanel();
		northPanel.add(apartadosPanel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(botonBoard = new JButton("Board"));
		buttonPanel.add(botonJugadores = new JButton("Jugadores"));
		buttonPanel.add(botonNext = new JButton("Comenzar / Next"));
		northPanel.add(buttonPanel, BorderLayout.CENTER);

		JPanel boardPanel = new JPanel(new GridLayout(2, 1));
		labelBoardInicial = new JLabel("Board Inicial: ");
		labelBoardInicial.setFont(new Font("Arial", Font.PLAIN, 20));
		labelBoardCartas = new JLabel();
		boardPanel.add(labelBoardInicial);
		boardPanel.add(labelBoardCartas);
		northPanel.add(boardPanel, BorderLayout.SOUTH);

		leftPanel.add(northPanel, BorderLayout.NORTH);

		textoSalida = new JTextArea();
		textoSalida.setEditable(false);
		textoSalida.setLineWrap(true);
		textoSalida.setWrapStyleWord(true);
		textoSalida.setFont(new Font("Arial", Font.PLAIN, 14));

		JScrollPane scrollPane = new JScrollPane(textoSalida);
		scrollPane.setPreferredSize(new Dimension(280, 500));
		leftPanel.add(scrollPane, BorderLayout.CENTER);

		return leftPanel;
	}

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
					preFlop();
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
//

	private void preFlop() {
		List<String> cartasUsadas = new ArrayList<>();
		gestionarBoard();
		cartasUsadas.addAll(listaCartasBoard);
		gestionarJugadores(cartasUsadas);
		equity = controller.getEquity(listaCartasJugadores, listaCartasBoard);
		actualizarGUI();
		fase++;
	}

	private void gestionarBoard() {
		if (listaCartasBoard == null || listaCartasBoard.isEmpty()) {
			listaCartasBoard = controller.generateRandomBoard();
			boardGenerado = true;
		} else {
			boardGenerado = false;
		}
	}

	private void gestionarJugadores(List<String> cartasUsadas) {
		if (listaCartasJugadores == null || listaCartasJugadores.isEmpty()) {
			listaCartasJugadores = controller.generateRandomPlayerCards(cartasUsadas, 6);
			playersGenerado = true;
		} else {
			for (List<String> playerCards : listaCartasJugadores) {
				if (!playerCards.isEmpty()) {
					cartasUsadas.addAll(playerCards);
				}
			}
			List<List<String>> newPlayerCards = controller.generateRandomPlayerCards(cartasUsadas,
					getEmptyPlayerSlots());
			updateEmptyPlayerSlots(newPlayerCards);
			playersGenerado = getEmptyPlayerSlots() > 0;
		}
	}

	private void actualizarGUI() {
		pintaCartasJugadores(2);
		pintaCartasBoard(3);
		actualizarTexto();
	}

	private void actualizarTexto() {
		if (boardGenerado) {
			labelBoardInicial.setText("Board Inicial (generado):");
		} else {
			labelBoardInicial.setText("Board Inicial (cargado):");
		}
		labelBoardCartas.setText(String.join(", ", listaCartasBoard));
		labelBoardCartas.setFont(new Font("Arial", Font.PLAIN, 20));
		StringBuilder playerCardsText = new StringBuilder();
		if (playersGenerado) {
			playerCardsText.append("Cartas de jugadores generadas:\n\n");
		} else {
			playerCardsText.append("Cartas de jugadores cargadas:\n\n");
		}

		for (int i = 0; i < listaCartasJugadores.size(); i++) {
			playerCardsText.append("Jugador ").append(i + 1).append(": ")
					.append(String.join(", ", listaCartasJugadores.get(i))).append("\n");
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
		JPanel rightPanel = (JPanel) ((JSplitPane) getComponent(0)).getRightComponent();
		rightPanel.removeAll();

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
					rightPanel.add(cartaLabel);
					labelCartasJugadores[jugador][i] = cartaLabel;
				}
			}

			String equityText = "N/A";
			if (equity != null && jugador < equity.size() && !equity.get(jugador).isEmpty()) {
				equityText = equity.get(jugador).get(0);
			}

			if (labelPorcentajes[jugador] == null) {
				labelPorcentajes[jugador] = new JLabel(equityText);
				labelPorcentajes[jugador].setFont(new Font("Arial", Font.BOLD, 20));
				labelPorcentajes[jugador].setForeground(Color.RED);
			} else {
				labelPorcentajes[jugador].setText(equityText);
			}

			int x = posicionesIniciales[jugador][0] - 70;
			int y = posicionesIniciales[jugador][1] + 50;
			labelPorcentajes[jugador].setBounds(x, y, 100, 20);

			rightPanel.add(labelPorcentajes[jugador]);
		}

		rightPanel.revalidate();
		rightPanel.repaint();
	}

	private void pintaCartasBoard(int numCartas) {
		JPanel rightPanel = (JPanel) ((JSplitPane) getComponent(0)).getRightComponent();

		for (int i = 0; i < numCartas; i++) {
			if (i < listaCartasBoard.size()) {
				String carta = listaCartasBoard.get(i);
				String imagePath = UtilidadesGUI.getCartaPath(carta);

				ImageIcon icon = new ImageIcon(imagePath);
				Image image = icon.getImage().getScaledInstance(70, 95, Image.SCALE_SMOOTH);
				JLabel cartaLabel = new JLabel(new ImageIcon(image));

				int x = 420 + (i * 80);

				cartaLabel.setBounds(x, 350, 70, 95);
				rightPanel.add(cartaLabel);
				labelCartasBoard[i] = cartaLabel;
			}
		}

		rightPanel.revalidate();
		rightPanel.repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 300, 0, getWidth() - 300, getHeight(), this);
	}
}