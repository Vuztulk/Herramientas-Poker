package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.LineBorder;

import Controlador.Controller;

public class Mesa extends JPanel {

	private static final long serialVersionUID = 1L;
	private Controller controller;
	private MainFrame mainFrame;
	private JTextArea textoSalida;
	private Image backgroundImage;
	private JButton botonArchivos;
	private List<String> manos;
	List<String> resultados;
	List<List<String>> res_jugadores;
	List<String> resultadosText;
	private JButton botonNext;
	private JLabel labelBoardInicial;
	private JLabel labelBoard;

	private JLabel[][] cartasJugadores = new JLabel[4][4];
	private JLabel[] cartasBoard = new JLabel[5];

	private int indiceManoActual = 0;

	private int[][] posicionesIniciales = { { 1260, 200 }, // Jugador 1 (Derecha)
			{ 750, 600 }, // Jugador 2 (Abajo)
			{ 750, 100 }, // Jugador 3 (Arriba)
			{ 500, 200 } // Jugador 4 (Izquierda)
	};

	private int[] desplazamientoX = { 0, 80, 80, 0 };
	private int[] desplazamientoY = { 100, 0, 0, 100 };

	public Mesa(Controller controller, MainFrame mainFrame) {
		this.controller = controller;
		this.mainFrame = mainFrame;

		setLayout(null);

		backgroundImage = new ImageIcon("src/GUI/Imagenes/Poker_Board.jpg").getImage();

		labelBoardInicial = new JLabel("Board Inicial: ");
		labelBoardInicial.setBounds(14, 60, 600, 50);
		labelBoardInicial.setForeground(Color.BLACK);
		labelBoardInicial.setFont(new Font("Arial", Font.PLAIN, 20));
		add(labelBoardInicial);

		labelBoard = new JLabel("");
		labelBoard.setBounds(14, 90, 600, 50);
		labelBoard.setForeground(Color.BLACK);
		labelBoard.setFont(new Font("Arial", Font.PLAIN, 20));
		add(labelBoard);

		JLabel labelApartados = new JLabel("Apartados:");
		labelApartados.setBounds(0, 0, 100, 20);
		labelApartados.setForeground(Color.BLACK);
		labelApartados.setFont(new Font("Arial", Font.PLAIN, 20));

		JPanel apartadosPanel = new JPanel();
		apartadosPanel.setBounds(10, 0, 320, 30);
		apartadosPanel.setOpaque(false);
		apartadosPanel.add(labelApartados);

		JRadioButton apartado1 = new JRadioButton("1");
		JRadioButton apartado2 = new JRadioButton("2");
		JRadioButton apartado3 = new JRadioButton("3");
		JRadioButton apartado4 = new JRadioButton("4");
		apartado1.setFont(new Font("Arial", Font.PLAIN, 15));
		apartado2.setFont(new Font("Arial", Font.PLAIN, 15));
		apartado3.setFont(new Font("Arial", Font.PLAIN, 15));
		apartado4.setFont(new Font("Arial", Font.PLAIN, 15));

		ButtonGroup grupoApartados = new ButtonGroup();
		grupoApartados.add(apartado1);
		grupoApartados.add(apartado2);
		grupoApartados.add(apartado3);
		grupoApartados.add(apartado4);

		apartadosPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		apartadosPanel.add(apartado1);
		apartadosPanel.add(apartado2);
		apartadosPanel.add(apartado3);
		apartadosPanel.add(apartado4);
		apartado1.setSelected(true);
		add(apartadosPanel);

		botonArchivos = new JButton("Seleccionar Archivos");
		botonArchivos.setBounds(10, 40, 130, 22);
		botonArchivos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// Comportamiento al pulsar el boton de selccionar archivos
				manos = mainFrame.mostrarMenuArchivos();

				if (apartado1.isSelected()) {
					indiceManoActual = 0;
					resultados = controller.obtenerManoRaw(manos);
					resultadosText = controller.procesarOrden("1", "src/App/entrada1.txt", "", false);
					ejecutarApartado1(manos);
				} else if (apartado2.isSelected()) {
					indiceManoActual = 0;
					resultados = controller.obtenerManoComunesRaw(manos);
					resultadosText = controller.procesarOrden("2", "src/App/entrada2.txt", "", false);
					ejecutarApartado2(manos);
				} else if (apartado3.isSelected()) {
					indiceManoActual = 0;
					res_jugadores = controller.ordJugRaw(manos);
					resultadosText = controller.procesarOrden("3", "src/App/entrada3.txt", "", false);
					ejecutarApartado3(manos);
				} else if (apartado4.isSelected()) {
					indiceManoActual = 0;
					resultados = controller.obtenerManoOmahaRaw(manos);
					resultadosText = controller.procesarOrden("4", "src/App/entrada4.txt", "", false);
					ejecutarApartado4(manos);
				}
			}

		});
		add(botonArchivos);

		botonNext = new JButton("Next");
		botonNext.setBounds(160, 40, 130, 22);
		botonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// Comportamiento al pulsar el boton de next

				if (apartado1.isSelected()) {
					if (manos != null && indiceManoActual < manos.size()) {
						if (indiceManoActual < resultados.size()) {
							labelBoard.setText(manos.get(indiceManoActual));
							pintarBoard(resultados.get(indiceManoActual), null);
							actualizarTexto(indiceManoActual);
							indiceManoActual++;
						}
					}
				} else if (apartado2.isSelected()) {
					if (manos != null && indiceManoActual < manos.size()) {
						if (indiceManoActual < resultados.size()) {
							String board[] = manos.get(indiceManoActual).split(";");
							labelBoard.setText(board[2]);
							ejecutarApartado2(manos);
						}
					}
				} else if (apartado3.isSelected()) {
					if (manos != null && indiceManoActual < manos.size()) {
						if (indiceManoActual < res_jugadores.size()) {
							labelBoard.setText(manos.get(indiceManoActual));
							ejecutarApartado3(manos);
						}
					}
				} else if (apartado4.isSelected()) {
					if (manos != null && indiceManoActual < manos.size()) {
						if (indiceManoActual < resultados.size()) {
							labelBoard.setText(manos.get(indiceManoActual));
							ejecutarApartado4(manos);
						}
					}
				}

			}
		});
		add(botonNext);

		textoSalida = new JTextArea();
		textoSalida.setEditable(false);
		textoSalida.setLineWrap(true);
		textoSalida.setWrapStyleWord(true);
		textoSalida.setFont(new Font("Arial", Font.PLAIN, 14));

		JScrollPane scrollPane = new JScrollPane(textoSalida);
		scrollPane.setBounds(10, 200, 280, 500);
		add(scrollPane);
	}

	private void ejecutarApartado1(List<String> manos) {

		borrarCartasJugadores();
		labelBoard.setText(manos.get(indiceManoActual));
		pintarBoard(resultados.get(indiceManoActual), null);
		actualizarTexto(indiceManoActual);
		indiceManoActual++;
	}

	private void ejecutarApartado2(List<String> manos) {

		borrarCartasJugadores();
		actualizarTexto(indiceManoActual);

		labelBoard.setText(manos.get(indiceManoActual));

		String mano = resultados.get(indiceManoActual);
		String board[] = manos.get(indiceManoActual).split(";");

		pintarBoard(board[2], mano);
		int numCartasJugador = mano.length() / 2;

		Set<String> cartasBoard = new HashSet<>();// Filtra las cartas para no pintarlas en la mano del jugador y en el tablero tambien

		for (int i = 0; i < board[2].length(); i += 2) {
			cartasBoard.add(board[2].substring(i, i + 2));
		}

		for (int jugador = 0; jugador < 4; jugador++) {
			if (jugador == 1) { // Solo el jugador de abajo muestra sus cartas
				int pos_carta = 0;
				for (int i = 0; i < numCartasJugador; i++) {
					String carta = mano.substring(i * 2, i * 2 + 2);

					if (!cartasBoard.contains(carta)) {
						String imagePath = UtilidadesGUI.getCartaPath(carta);
						cartasJugadores[jugador][pos_carta] = new JLabel(new ImageIcon(imagePath));

						int x = posicionesIniciales[jugador][0] + (desplazamientoX[jugador] * pos_carta);
						int y = posicionesIniciales[jugador][1] + (desplazamientoY[jugador] * pos_carta);

						cartasJugadores[jugador][pos_carta].setBounds(x, y, 70, 95);
						cartasJugadores[jugador][pos_carta].setBorder(new LineBorder(Color.YELLOW, 4));
						add(cartasJugadores[jugador][pos_carta]);
						pos_carta++;
					}
				}
			} else { // Los otros jugadores muestran 4 jokers
				for (int pos_carta = 0; pos_carta < 4; pos_carta++) {
					String imagePath = "src/GUI/Imagenes/Cartas/red_joker.png";
					cartasJugadores[jugador][pos_carta] = new JLabel(new ImageIcon(imagePath));

					int x = posicionesIniciales[jugador][0] + (desplazamientoX[jugador] * pos_carta);
					int y = posicionesIniciales[jugador][1] + (desplazamientoY[jugador] * pos_carta);

					cartasJugadores[jugador][pos_carta].setBounds(x, y, 70, 95);
					add(cartasJugadores[jugador][pos_carta]);
				}
			}
		}

		revalidate();
		repaint();
		indiceManoActual++;
	}

	private void ejecutarApartado3(List<String> manos) {

		borrarCartasJugadores();
		actualizarTexto(indiceManoActual);

		labelBoard.setText(manos.get(indiceManoActual));

		String board[] = manos.get(indiceManoActual).split(";");

		List<String> cartasLimpias = new ArrayList<>();
		for (int i = 1; i < board.length - 1; i++) {
			String carta = board[i];
			if (carta.matches("^J\\d.*")) {
				String cartaLimpia = carta.replaceFirst("^J\\d", "");
				cartasLimpias.add(cartaLimpia);
			} else {
				cartasLimpias.add(carta);
			}
		}

		int numJugadores = Integer.parseInt(board[0]);

		pintarBoard(board[numJugadores + 1], "");

		Set<String> cartasBoard = new HashSet<>();
		for (int i = 0; i < board[numJugadores + 1].length(); i += 2) {
			cartasBoard.add(board[numJugadores + 1].substring(i, i + 2));
		}

		for (int jugador = 0; jugador < 4; jugador++) {
			if (jugador < numJugadores) {
				int pos_carta = 0;
				for (int i = 0; i < cartasLimpias.get(jugador).length() / 2; i++) {
					String carta = cartasLimpias.get(jugador).substring(i * 2, i * 2 + 2);

					String imagePath = UtilidadesGUI.getCartaPath(carta);
					cartasJugadores[jugador][pos_carta] = new JLabel(new ImageIcon(imagePath));

					int x = posicionesIniciales[jugador][0] + (desplazamientoX[jugador] * pos_carta);
					int y = posicionesIniciales[jugador][1] + (desplazamientoY[jugador] * pos_carta);

					cartasJugadores[jugador][pos_carta].setBounds(x, y, 70, 95);
					add(cartasJugadores[jugador][pos_carta]);
					pos_carta++;
				}
			} else {
				for (int pos_carta = 0; pos_carta < 4; pos_carta++) {
					String imagePath = "src/GUI/Imagenes/Cartas/red_joker.png";
					cartasJugadores[jugador][pos_carta] = new JLabel(new ImageIcon(imagePath));

					int x = posicionesIniciales[jugador][0] + (desplazamientoX[jugador] * pos_carta);
					int y = posicionesIniciales[jugador][1] + (desplazamientoY[jugador] * pos_carta);

					cartasJugadores[jugador][pos_carta].setBounds(x, y, 70, 95);
					add(cartasJugadores[jugador][pos_carta]);
				}
			}
		}

		revalidate();
		repaint();
		indiceManoActual++;
	}

	private void ejecutarApartado4(List<String> manos) {

		borrarCartasJugadores();
		actualizarTexto(indiceManoActual);

		labelBoard.setText(manos.get(indiceManoActual));

		String partes[] = manos.get(indiceManoActual).split(";");
		String mano = partes[0];

		pintarBoard(partes[2], resultados.get(indiceManoActual));
		int numCartasJugador = mano.length() / 2;

		Set<String> cartasBoard = new HashSet<>();
		for (int i = 0; i < resultados.get(indiceManoActual).length(); i += 2) {
			cartasBoard.add(resultados.get(indiceManoActual).substring(i, i + 2));
		}

		for (int jugador = 0; jugador < 4; jugador++) {
			if (jugador == 1) { // Solo el jugador de abajo muestra sus cartas
				int pos_carta = 0;
				for (int i = 0; i < numCartasJugador; i++) {
					String carta = mano.substring(i * 2, i * 2 + 2);

					if (cartasBoard.contains(carta)) {
						String imagePath = UtilidadesGUI.getCartaPath(carta);
						cartasJugadores[jugador][pos_carta] = new JLabel(new ImageIcon(imagePath));

						int x = posicionesIniciales[jugador][0] + (desplazamientoX[jugador] * pos_carta);
						int y = posicionesIniciales[jugador][1] + (desplazamientoY[jugador] * pos_carta);

						cartasJugadores[jugador][pos_carta].setBounds(x, y, 70, 95);
						cartasJugadores[jugador][pos_carta].setBorder(new LineBorder(Color.YELLOW, 4));
						add(cartasJugadores[jugador][pos_carta]);
						pos_carta++;
					}
				}
			} else { // Los otros jugadores muestran 4 jokers
				for (int pos_carta = 0; pos_carta < 4; pos_carta++) {
					String imagePath = "src/GUI/Imagenes/Cartas/red_joker.png";
					cartasJugadores[jugador][pos_carta] = new JLabel(new ImageIcon(imagePath));

					int x = posicionesIniciales[jugador][0] + (desplazamientoX[jugador] * pos_carta);
					int y = posicionesIniciales[jugador][1] + (desplazamientoY[jugador] * pos_carta);

					cartasJugadores[jugador][pos_carta].setBounds(x, y, 70, 95);
					add(cartasJugadores[jugador][pos_carta]);
				}
			}
		}

		revalidate();
		repaint();
		indiceManoActual++;
	}

	private void pintarBoard(String mano, String cards) {
		for (JLabel carta : cartasBoard) { // Limpia el board antes de pintar la nueva mano
			if (carta != null) {
				remove(carta);
			}
		}

		int xInicial = 720;
		int y = 350;

		List<String> cartasResaltar = new ArrayList<>();
		if (cards != null) {
			for (int i = 0; i < cards.length(); i += 2) {
				cartasResaltar.add(cards.substring(i, i + 2));
			}
		}

		for (int c = 0; c < Math.min(5, mano.length() / 2); c++) {
			String carta = mano.substring(c * 2, c * 2 + 2);
			String imagePath = UtilidadesGUI.getCartaPath(carta);

			if (imagePath != null) {
				JLabel cartaLabel = new JLabel(new ImageIcon(imagePath));
				cartaLabel.setBounds(xInicial + (c * 80), y, 70, 95);

				if (cartasResaltar.contains(carta)) {
					cartaLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
				}

				add(cartaLabel);
				cartasBoard[c] = cartaLabel;
			}
		}

		revalidate();
		repaint();
	}

	private void borrarCartasJugadores() {
		for (int i = 0; i < cartasJugadores.length; i++) {
			for (int j = 0; j < cartasJugadores[i].length; j++) {
				if (cartasJugadores[i][j] != null) {
					remove(cartasJugadores[i][j]);
					cartasJugadores[i][j] = null;
				}
			}
		}
		revalidate();
		repaint();
	}

	public void actualizarTexto(int indice) {
		textoSalida.setText(resultadosText.get(indice));
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 300, 0, getWidth() - 300, getHeight(), this);
	}
}
