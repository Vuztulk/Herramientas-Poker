package Vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PlayersPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private List<List<String>> playerCards;
    private JLabel[][] cardLabels = new JLabel[6][4];
    private JLabel[] equityLabels = new JLabel[6];
    private JPanel rightPanel;
    
    private int[][] positions = {
        {100, 340}, {350, 100}, {350, 600},
        {1080, 340}, {750, 100}, {750, 600}
    };
    
    public PlayersPanel(JPanel rightPanel) {
    	this.rightPanel = rightPanel;
        setLayout(null);
        setOpaque(false);
    }
    
    public void generatePlayers() {
        // Lógica para generar jugadores
    }
    
    public void updatePlayers(List<List<String>> cards, List<List<String>> equities) {
        this.playerCards = cards;
        for (int player = 0; player < 6; player++) {
            updatePlayerCards(player, cards.get(player));
            List<String> playerEquities = equities.get(player);
            updatePlayerEquity(player, String.join(", ", playerEquities));
        }

    }
    
    private void updatePlayerCards(int player, List<String> cards) {
        for (int i = 0; i < 4; i++) {
            if (i < cards.size()) {
                String card = cards.get(i);
                ImageIcon icon = new ImageIcon(UtilidadesGUI.getCartaPath(card));
                Image image = icon.getImage().getScaledInstance(70, 95, Image.SCALE_SMOOTH);
                if (cardLabels[player][i] == null) {
                    cardLabels[player][i] = new JLabel(new ImageIcon(image));
                    int x = positions[player][0] + (-30 * i);
                    int y = positions[player][1] + (i == 0 ? 20 : 0);
                    cardLabels[player][i].setBounds(x, y, 70, 95);
                    rightPanel.add(cardLabels[player][i]);
                } else {
                    cardLabels[player][i].setIcon(new ImageIcon(image));
                }
                cardLabels[player][i].setVisible(true);
            } else if (cardLabels[player][i] != null) {
                cardLabels[player][i].setVisible(false);
            }
        }
    }
    
    private void updatePlayerEquity(int player, String equity) {
        if (equityLabels[player] == null) {
            equityLabels[player] = new JLabel(equity);
            equityLabels[player].setFont(new Font("Arial", Font.BOLD, 20));
            equityLabels[player].setForeground(Color.RED);
            int x = positions[player][0] - 100;
            int y = positions[player][1] + 50;
            equityLabels[player].setBounds(x, y, 100, 20);
            rightPanel.add(equityLabels[player]);
        } else {
            equityLabels[player].setText(equity);
        }
    }
}