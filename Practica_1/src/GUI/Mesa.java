package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
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
	private Image backgroundImage;
	private JButton botonArchivos;
	private List<String> manos;
	List<String> resultados;
	List<List<String>> res_jugadores;
	private JButton botonNext;
	private JLabel labelBoardInicial;

	private JLabel[][] cartasJugadores = new JLabel[4][4];
	private JLabel[] cartasBoard = new JLabel[5];

	private int indiceManoActual = 0;

	private String[] rutasImagenesCartas = { "src/GUI/Imagenes/Cartas/10_of_clubs.png",
			"src/GUI/Imagenes/Cartas/9_of_clubs.png", "src/GUI/Imagenes/Cartas/8_of_clubs.png",
			"src/GUI/Imagenes/Cartas/7_of_clubs.png", "src/GUI/Imagenes/Cartas/6_of_clubs.png" };

	private int[][] posicionesIniciales = { { 1160, 200 }, // Jugador 1 (Derecha)
			{ 570, 600 }, // Jugador 2 (Abajo)
			{ 570, 100 }, // Jugador 3 (Arriba)
			{ 300, 200 } // Jugador 4 (Izquierda)
	};

	private int[] desplazamientoX = { 0, 80, 80, 0 };
	private int[] desplazamientoY = { 100, 0, 0, 100 };

	public Mesa(Controller controller, MainFrame mainFrame) {
		this.controller = controller;
		this.mainFrame = mainFrame;

		setLayout(null);

		backgroundImage = new ImageIcon("src/GUI/Imagenes/Poker_Board.jpg").getImage();

		labelBoardInicial = new JLabel("Board Inicial: ");
		labelBoardInicial.setBounds(10, 60, 600, 50);
		labelBoardInicial.setForeground(Color.BLACK);
		labelBoardInicial.setFont(new Font("Arial", Font.PLAIN, 20));
		add(labelBoardInicial);

		JPanel apartadosPanel = new JPanel();
		apartadosPanel.setBounds(0, 0, 323, 31);
		apartadosPanel.setOpaque(false);

		JRadioButton apartado1 = new JRadioButton("Apartado 1");
		JRadioButton apartado2 = new JRadioButton("Apartado 2");
		JRadioButton apartado3 = new JRadioButton("Apartado 3");
		JRadioButton apartado4 = new JRadioButton("Apartado 4");

		ButtonGroup grupoApartados = new ButtonGroup();
		grupoApartados.add(apartado1);
		grupoApartados.add(apartado2);
		grupoApartados.add(apartado3);
		grupoApartados.add(apartado4);

		apartadosPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		apartadosPanel.add(apartado1);
		apartadosPanel.add(apartado2);
		apartadosPanel.add(apartado3);
		apartadosPanel.add(apartado4);
		apartado1.setSelected(true);
		add(apartadosPanel);

		botonArchivos = new JButton("Seleccionar Archivos");
		botonArchivos.setBounds(30, 40, 130, 22);
		botonArchivos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manos = mainFrame.mostrarMenuArchivos();

				if (apartado1.isSelected()) {
					indiceManoActual = 0;
					resultados = controller.obtenerManoRaw(manos);
					ejecutarApartado1(manos);
				} else if (apartado2.isSelected()) {
					indiceManoActual = 0;
					resultados = controller.obtenerManoComunesRaw(manos);
					ejecutarApartado2(manos);
				} else if (apartado3.isSelected()) {
					indiceManoActual = 0;
					res_jugadores = controller.ordJugRaw(manos);
					ejecutarApartado3(res_jugadores);
				} else if (apartado4.isSelected()) {
					indiceManoActual = 0;
					// resultados = controller.obtenerManoRaw(manos);
					ejecutarApartado4(manos);
				}
			}

		});
		add(botonArchivos);

		botonNext = new JButton("Next");
		botonNext.setBounds(180, 40, 130, 22);
		botonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (apartado1.isSelected()) {
					if (manos != null && indiceManoActual < manos.size()) {
						if (indiceManoActual < resultados.size()) {
							labelBoardInicial.setText("Board Inicial: " + manos.get(indiceManoActual));
							pintarBoard(resultados.get(indiceManoActual), null);
							indiceManoActual++;
						}
					}
				} else if (apartado2.isSelected()) {
					if (manos != null && indiceManoActual < manos.size()) {
						if (indiceManoActual < resultados.size()) {
							String board[] = manos.get(indiceManoActual).split(";");
							labelBoardInicial.setText("Board Inicial: " + board[2]);
							ejecutarApartado2(manos);
						}
					}
				} else if (apartado3.isSelected()) {
					if (manos != null && indiceManoActual < manos.size()) {
						if (indiceManoActual < res_jugadores.size()) {
							labelBoardInicial.setText("Board Inicial: " + manos.get(indiceManoActual));
							ejecutarApartado3(res_jugadores);
						}
					}
				} else if (apartado4.isSelected()) {

				}

			}
		});
		add(botonNext);

	}

	private void ejecutarApartado1(List<String> manos) {
		borrarCartasJugadores();
		labelBoardInicial.setText("Board Inicial: " + manos.get(indiceManoActual));
		pintarBoard(resultados.get(indiceManoActual), null);
		indiceManoActual++;
	}

	private void ejecutarApartado2(List<String> manos) {
		borrarCartasJugadores();
		labelBoardInicial.setText("Board Inicial: " + manos.get(indiceManoActual));

		String mano = resultados.get(indiceManoActual);
		String board[] = manos.get(indiceManoActual).split(";");

		pintarBoard(board[2], mano);
		int numCartasJugador = mano.length() / 2;

		Set<String> cartasBoard = new HashSet<>();// Filtra las cartas para no pintarlas en la mano del jugador y en el
													// tablero tambien
		for (int i = 0; i < board[2].length(); i += 2) {
			cartasBoard.add(board[2].substring(i, i + 2));
		}

		for (int jugador = 0; jugador < 4; jugador++) {
			if (jugador == 1) { // Solo el jugador de abajo muestra sus cartas
				for (int pos_carta = 0; pos_carta < numCartasJugador; pos_carta++) {
					String carta = mano.substring(pos_carta * 2, pos_carta * 2 + 2);

					if (!cartasBoard.contains(carta)) {
						String imagePath = UtilidadesGUI.getCartaPath(carta);
						cartasJugadores[jugador][pos_carta] = new JLabel(new ImageIcon(imagePath));

						int x = posicionesIniciales[jugador][0] + (desplazamientoX[jugador] * pos_carta);
						int y = posicionesIniciales[jugador][1] + (desplazamientoY[jugador] * pos_carta);

						cartasJugadores[jugador][pos_carta].setBounds(x, y, 70, 95);
						cartasJugadores[jugador][pos_carta].setBorder(new LineBorder(Color.YELLOW, 5));
						add(cartasJugadores[jugador][pos_carta]);
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

	private void ejecutarApartado3(List<List<String>> res_jugadores) {
		borrarCartasJugadores();
		labelBoardInicial.setText("Board Inicial: " + manos.get(indiceManoActual));

		List<String> listamano = res_jugadores.get(indiceManoActual);
		String mano = String.join("", listamano);

		String board[] = manos.get(indiceManoActual).split(";");
		int numJugadores = Integer.parseInt(board[0]);

		pintarBoard(board[numJugadores + 1], mano);

		Set<String> cartasBoard = new HashSet<>();
		for (int i = 0; i < board[numJugadores + 1].length(); i += 2) {
			cartasBoard.add(board[numJugadores + 1].substring(i, i + 2));
		}

		for (int jugador = 0; jugador < 4; jugador++) {
			if (jugador < numJugadores) {
				for (int pos_carta = 0; pos_carta < listamano.get(jugador).length() / 2; pos_carta++) {
					String carta = listamano.get(jugador).substring(pos_carta * 2, pos_carta * 2 + 2);

					if (!cartasBoard.contains(carta)) {
						String imagePath = UtilidadesGUI.getCartaPath(carta);
						cartasJugadores[jugador][pos_carta] = new JLabel(new ImageIcon(imagePath));

						int x = posicionesIniciales[jugador][0] + (desplazamientoX[jugador] * pos_carta);
						int y = posicionesIniciales[jugador][1] + (desplazamientoY[jugador] * pos_carta);

						cartasJugadores[jugador][pos_carta].setBounds(x, y, 70, 95);
						cartasJugadores[jugador][pos_carta].setBorder(new LineBorder(Color.YELLOW, 5));
						add(cartasJugadores[jugador][pos_carta]);
					}
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
		indiceManoActual++; // Incrementa el índice para la siguiente mano
	}

	private void ejecutarApartado4(List<String> manos) {

	}

	private void pintarBoard(String mano, String cards) {
		for (JLabel carta : cartasBoard) { // Limpia el board antes de pintar la nueva mano
			if (carta != null) {
				remove(carta);
			}
		}

		int xInicial = 570;
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
					cartaLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 5));
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

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	}
}
