package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.Controlador;
import Modelo.AnalizadorRangos;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PanelAnalisis extends JPanel {
    
	private static final long serialVersionUID = 1L;
	private JPanel cardSelectionPanel;
    private JPanel resultPanel;
    private Map<String, JButton> cardButtons;
    private List<String> selectedCards;
    private List<String> seleccionesGuardadas;
    private AnalizadorRangos rangeAnalyzer;
    private Controlador controlador;
    
    public PanelAnalisis(List<String> seleccionesGuardadas, Controlador controlador) {
        this.seleccionesGuardadas = seleccionesGuardadas;
        this.controlador = controlador;
        
        setLayout(new BorderLayout());
        
        cardSelectionPanel = crearPanelCartas();
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        JButton analyzeButton = new JButton("Analizar");
        analyzeButton.addActionListener(e -> ejecutarAnalisis());

        add(cardSelectionPanel, BorderLayout.WEST);
        add(resultPanel, BorderLayout.CENTER);
        add(analyzeButton, BorderLayout.SOUTH);
    }

    private JPanel crearPanelCartas() {
        JPanel panel = new JPanel(new GridLayout(13, 4, 2, 2));
        cardButtons = new HashMap<>();
        selectedCards = new ArrayList<>();

        String[] ranks = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] suits = {"h", "s", "d", "c"};
        Color[] suitColors = {Color.ORANGE, Color.LIGHT_GRAY, Color.RED, Color.GREEN};

        for (String rank : ranks) {
            for (int i = 0; i < suits.length; i++) {
                final Color suitColor = suitColors[i];
                String card = rank + suits[i];
                JButton button = new JButton(card);
                button.setPreferredSize(new Dimension(60, 60));
                button.addActionListener(e -> seleccionCartas(card, button, suitColor));
                cardButtons.put(card, button);
                button.setBackground(suitColor);	
                panel.add(button);
            }
        }
        return panel;
    }

    private void seleccionCartas(String card, JButton button, Color suitColor) {
        if (selectedCards.remove(card)) {
            button.setBackground(suitColor);
        } else if (selectedCards.size() < 5) {
            selectedCards.add(card);
            button.setBackground(new Color(180, 120, 160));
        }
    }

    private void ejecutarAnalisis() {
        if (selectedCards.size() < 3) {
            JOptionPane.showMessageDialog(this, "Seleccione al menos 3 cartas para el board");
            return;
        }

        Set<String> range = new HashSet<>(seleccionesGuardadas);
        rangeAnalyzer = controlador.inicializarAnalizadorRangos(range, selectedCards);
        
        Map<String, Double> probabilities = rangeAnalyzer.getProbabilidadesMano();
        actualizarPanelResultados(probabilities);
    }

    private void actualizarPanelResultados(Map<String, Double> probabilities) {
        resultPanel.removeAll();
        resultPanel.setLayout(new BorderLayout());
        
        JLabel totalCombosLabel = new JLabel("Numero total de combos: " + rangeAnalyzer.getCombosTotales());
        totalCombosLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(totalCombosLabel);
        resultPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel handListPanel = new JPanel();
        handListPanel.setLayout(new BoxLayout(handListPanel, BoxLayout.Y_AXIS));
        handListPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        int maxLabelWidth = 0;
        for (String handName : probabilities.keySet()) {
            JLabel tempLabel = new JLabel(handName);
            maxLabelWidth = Math.max(maxLabelWidth, tempLabel.getPreferredSize().width);
        }

        final int finalMaxLabelWidth = maxLabelWidth;

        for (Map.Entry<String, Double> entry : probabilities.entrySet()) {
            JPanel handPanel = new JPanel();
            handPanel.setLayout(new BoxLayout(handPanel, BoxLayout.X_AXIS));
            
            JLabel handLabel = new JLabel(entry.getKey());
            handLabel.setPreferredSize(new Dimension(finalMaxLabelWidth + 10, handLabel.getPreferredSize().height));
            handPanel.add(handLabel);

            handPanel.add(Box.createHorizontalStrut(10));

            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue((int) Math.round(entry.getValue()));
            progressBar.setStringPainted(true);
            progressBar.setString(String.format("%.1f%%", entry.getValue()));
            progressBar.setPreferredSize(new Dimension(400, progressBar.getPreferredSize().height));
            handPanel.add(progressBar);

            handPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            handListPanel.add(handPanel);
            handListPanel.add(Box.createVerticalStrut(5));
        }

        JScrollPane scrollPane = new JScrollPane(handListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        resultPanel.revalidate();
        resultPanel.repaint();
    }
    
}
