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

    private int currentPhase = 0;
    private List<String> boardCards;
    private List<List<String>> playerCards;
    private List<List<String>> equity;
    
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
    
    public BoardPanel getBoardPanel() { return boardPanel; }
    public PlayersPanel getPlayersPanel() { return playersPanel; }
    public ControlPanel getControlPanel() { return controlPanel; }
    public Controller getController() { return controller; }
    public MainFrame getMainFrame() { return mainFrame; }
    public JTextArea getTextoSalida() { return textoSalida; }
    
    public void next() {
        switch (currentPhase) {
            case 0:
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
        currentPhase++;
        updateGUI();
    }

    private void preFlop() {
        boardCards = controller.generateRandomBoard();
        playerCards = controller.generateRandomPlayerCards(new ArrayList<>(boardCards), 6);
        equity = controller.getEquity(playerCards, boardCards);
        
        //boardPanel.updateBoard(boardCards);
        playersPanel.updatePlayers(playerCards, equity);
    }

    private void flop() {
        List<String> flopCards = boardCards.subList(0, 3);
        boardPanel.updateBoard(flopCards);
        equity = controller.getEquity(playerCards, boardCards);
        playersPanel.updatePlayers(playerCards, equity);
    }

    private void turn() {
        List<String> turnCards = boardCards.subList(0, 4);
        boardPanel.updateBoard(turnCards);
        equity = controller.getEquity(playerCards, boardCards);
        playersPanel.updatePlayers(playerCards, equity);
    }

    private void river() {
        boardPanel.updateBoard(boardCards);
        equity = controller.getEquity(playerCards, boardCards);
        playersPanel.updatePlayers(playerCards, equity);
    }

    private void resetGame() {
        currentPhase = 0;
        boardCards = null;
        playerCards = null;
        equity = null;
        boardPanel.updateBoard(new ArrayList<>());
        playersPanel.updatePlayers(new ArrayList<>(), new ArrayList<>());
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
        description.append("Cartas del Board: ").append(String.join(", ", boardCards.subList(0, Math.min(currentPhase + 2, 5)))).append("\n\n");
        for (int i = 0; i < playerCards.size(); i++) {
            description.append("Jugador ").append(i + 1).append(": ")
                       .append(String.join(", ", playerCards.get(i)))
                       .append(" (Equity: ").append(equity.get(i)).append(")\n");
        }
        return description.toString();
    }

    private String getPhaseString() {
        switch (currentPhase) {
            case 0: return "Pre-flop";
            case 1: return "Flop";
            case 2: return "Turn";
            case 3: return "River";
            default: return "";
        }
    }
   

}