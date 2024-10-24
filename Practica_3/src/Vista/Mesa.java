package Vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.Controller;

import java.util.ArrayList;
import java.util.List;

public class Mesa extends JPanel {

	private static final long serialVersionUID = 1L;
	private Controller controller;
	private MainFrame mainFrame;
	private JTextArea textoSalida;
	private Image backgroundImage;
	private BoardPanel boardPanel;
	private PlayersPanel playersPanel;
	private ControlPanel controlPanel;
	private boolean type_game;
	
	private int currentPhase = 0;
	private List<String> boardCards;
	private List<List<String>> playerCards;
	private List<List<String>> equity;
	private List<Boolean> activePlayers;

	private boolean boardLoaded = false;
	private boolean playersLoaded = false;

	public Mesa(Controller controller, MainFrame mainFrame) {
		this.controller = controller;
		this.mainFrame = mainFrame;

		setLayout(new BorderLayout());
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setResizeWeight(0.1);

		JPanel leftPanel = createLeftPanel();
		splitPane.setLeftComponent(leftPanel);

		JPanel rightPanel = createRightPanel();
		splitPane.setRightComponent(rightPanel);

		add(splitPane, BorderLayout.CENTER);

		backgroundImage = new ImageIcon("src/Vista/Imagenes/Poker_Board.jpg").getImage();

		activePlayers = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			activePlayers.add(true);
		}
	}

	public BoardPanel getBoardPanel() {
		return boardPanel;
	}

	public PlayersPanel getPlayersPanel() {
		return playersPanel;
	}

	public ControlPanel getControlPanel() {
		return controlPanel;
	}

	public Controller getController() {
		return controller;
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public JTextArea getTextoSalida() {
		return textoSalida;
	}

	private JPanel createLeftPanel() {
		JPanel leftPanel = new JPanel(new BorderLayout());
		controlPanel = new ControlPanel(this);
		leftPanel.add(controlPanel, BorderLayout.NORTH);

		textoSalida = new JTextArea();
		textoSalida.setEditable(false);
		textoSalida.setLineWrap(true);
		textoSalida.setWrapStyleWord(true);
		textoSalida.setFont(new Font("Arial", Font.PLAIN, 14));

		JScrollPane scrollPane = new JScrollPane(textoSalida);
		scrollPane.setPreferredSize(new Dimension(280, 500));
		leftPanel.add(scrollPane, BorderLayout.CENTER);

		leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		return leftPanel;
	}

	private JPanel createRightPanel() {
		JPanel rightPanel = new JPanel() {
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
			}
		};
		rightPanel.setLayout(null);

		boardPanel = new BoardPanel(rightPanel);
		playersPanel = new PlayersPanel(rightPanel);

		rightPanel.add(boardPanel);
		rightPanel.add(playersPanel);

		return rightPanel;
	}

	public void next() {

		if (currentPhase > 0) {
			updateActivePlayers(controlPanel.getJugadorCheckbox());
		}
		switch (currentPhase) {
		case 0:
			type_game = controlPanel.isNormalSelected() ? true : false;
			preFlop();
			break;
		case 1:
			flop();
			break;
		case 2:
			turn();
			break;
		case 3:
			river();
			break;
		default:
			resetGame();
			return;
		}
		updateGUI();
		currentPhase++;
	}

	private void preFlop() {
		if (!boardLoaded || boardCards == null) {
			boardCards = controller.generateRandomBoard();
		}

		if (!playersLoaded || playerCards == null) {
			playerCards = new ArrayList<>();
		}

		activePlayers.clear();
		for (int i = 0; i < 6; i++) {
			activePlayers.add(true);
		}

		List<String> usedCards = new ArrayList<>(boardCards);
		for (List<String> playerHand : playerCards) {
			usedCards.addAll(playerHand);
		}

		int numCards = controlPanel.isOmahaSelected() ? 4 : 2;

		while (playerCards.size() < 6) {
			List<String> newHand = controller.generateRandomPlayerCards(usedCards, numCards).get(0);
			playerCards.add(newHand);
			usedCards.addAll(newHand);
		}

		if (playersLoaded) {
			for (int i = 0; i < playerCards.size(); i++) {
				List<String> hand = playerCards.get(i);
				if (hand.size() != numCards) {
					List<String> newHand = controller.generateRandomPlayerCards(usedCards, numCards).get(0);
					playerCards.set(i, newHand);
					usedCards.addAll(newHand);
				}
			}
		}

		equity = controller.getEquity(playerCards, new ArrayList<>(), activePlayers, type_game);
		boardPanel.updateBoard(new ArrayList<>());
		playersPanel.updatePlayers(playerCards, equity, activePlayers, type_game);
		updateCardInfo();

		boardLoaded = false;
		playersLoaded = false;
	}

	private void flop() {
		List<String> flopCards = boardCards.subList(0, 3);
		boardPanel.updateBoard(flopCards);
		equity = controller.getEquity(playerCards, flopCards, activePlayers, type_game);
		playersPanel.updatePlayers(playerCards, equity, activePlayers, type_game);
	}

	private void turn() {
		List<String> turnCards = boardCards.subList(0, 4);
		boardPanel.updateBoard(turnCards);
		equity = controller.getEquity(playerCards, turnCards, activePlayers, type_game);
		playersPanel.updatePlayers(playerCards, equity, activePlayers, type_game);
	}

	private void river() {
		boardPanel.updateBoard(boardCards);
		equity = controller.getEquity(playerCards, boardCards, activePlayers, type_game);
		playersPanel.updatePlayers(playerCards, equity, activePlayers, type_game);
	}

	public void loadBoardFromFile() {
		List<List<String>> loadedCards = mainFrame.mostrarMenuArchivos("Board");
		if (!loadedCards.isEmpty()) {
			boardCards = loadedCards.get(0);
			boardLoaded = true;
		}
	}

	public void loadPlayersFromFile() {
		playerCards = mainFrame.mostrarMenuArchivos("Jugadores");
		if (!playerCards.isEmpty()) {
			int expectedCards = controlPanel.isOmahaSelected() ? 4 : 2;
			boolean validHands = playerCards.stream().allMatch(hand -> hand.size() == expectedCards);

			if (!validHands) {
				JOptionPane.showMessageDialog(this,
						"El archivo contiene manos con un número incorrecto de cartas para la modalidad seleccionada.\n"
								+ "Se generaran manos aleatorias en su lugar",
						"Error en el formato", JOptionPane.WARNING_MESSAGE);
				playerCards.clear();
			}
			playersLoaded = !playerCards.isEmpty();
		}
	}

	private void resetGame() {
		currentPhase = 0;
		boardCards = null;
		playerCards = null;
		equity = null;
		boardPanel.updateBoard(new ArrayList<>());
		playersPanel.updatePlayers(new ArrayList<>(), new ArrayList<>(), activePlayers, type_game);

		activePlayers.clear();
		for (int i = 0; i < 6; i++) {
			activePlayers.add(true);
		}

		textoSalida.setText("");
		controlPanel.updatePhaseLabel(-1);
		controlPanel.resetControls();

	}

	private void updateGUI() {
		textoSalida.setText(generatePhaseDescription());
		controlPanel.updatePhaseLabel(currentPhase);
		revalidate();
		repaint();
	}

	private String generatePhaseDescription() {
		StringBuilder description = new StringBuilder();
		description.append("Fase actual: ").append(getPhaseString()).append("\n\n");

		if (currentPhase > 0) {
			description.append("Cartas del Board: ")
					.append(String.join(", ", boardCards.subList(0, Math.min(currentPhase + 2, 5)))).append("\n\n");
		}

		for (int i = 0; i < playerCards.size(); i++) {
			if (activePlayers.get(i)) {
				description.append("Jugador ").append(i + 1).append(": ").append(String.join(", ", playerCards.get(i)))
						.append(" (Equity: ").append(equity.get(i).get(0)).append(")\n");
			} else {
				description.append("Jugador ").append(i + 1).append(": Fold\n");
			}
		}
		return description.toString();
	}

	private void updateCardInfo() {
		StringBuilder info = new StringBuilder();

		if (currentPhase > 0) {
			info.append(String.join(", ", boardCards.subList(0, Math.min(currentPhase + 2, 5)))).append("\n");
		}

		info.append("\nJugadores:\n");
		for (int i = 0; i < playerCards.size(); i++) {
			info.append("Jugador ").append(i + 1).append(": ").append(String.join(", ", playerCards.get(i)))
					.append("\n");
		}

		textoSalida.setText(info.toString());
	}

	public void updateActivePlayers(boolean[] playerStates) {
		for (int i = 0; i < playerStates.length; i++) {
			activePlayers.set(i, playerStates[i]);
		}
	}

	private String getPhaseString() {
		switch (currentPhase) {
		case 0:
			return "Pre-flop";
		case 1:
			return "Flop";
		case 2:
			return "Turn";
		case 3:
			return "River";
		default:
			return "Inicio";
		}
	}

}