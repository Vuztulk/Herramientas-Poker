package Vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BoardPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
    private JLabel[] cardLabels = new JLabel[5];
    JPanel rightPanel;
    
    public BoardPanel(JPanel rightPanel) {
    	this.rightPanel = rightPanel;
        setLayout(null);
        setOpaque(false);
    }
    
    public void generateBoard() {
        // Si hay archivo leido entonces no genera aleatorio sino aleatorio
    }
    
    public void updateBoard(List<String> cards) {
        for (int i = 0; i < 5; i++) {
            if (i < cards.size()) {
                String card = cards.get(i);
                ImageIcon icon = new ImageIcon(UtilidadesGUI.getCartaPath(card));
                Image image = icon.getImage().getScaledInstance(70, 95, Image.SCALE_SMOOTH);
                if (cardLabels[i] == null) {
                    cardLabels[i] = new JLabel(new ImageIcon(image));
                    cardLabels[i].setBounds(420 + (i * 80), 350, 70, 95);
                    rightPanel.add(cardLabels[i]);
                } else {
                    cardLabels[i].setIcon(new ImageIcon(image));
                }
                cardLabels[i].setVisible(true);
            } else if (cardLabels[i] != null) {
                cardLabels[i].setVisible(false);
            }
        }
    }
}