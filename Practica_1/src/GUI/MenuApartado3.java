package GUI;

import Controlador.Controller;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class MenuApartado3 extends JPanel {

    private static final long serialVersionUID = 1L;
    private List<String> manos;
    private Controller controller;

    public MenuApartado3(List<String> manos, Controller controller) {
        this.manos = manos;
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JPanel resultadosPanel = new JPanel();
        resultadosPanel.setLayout(new GridLayout(manos.size(), 1));

        List<String> resultados = controller.ordJugRaw(manos); 
        
        for (int m = 0; m < manos.size(); m++) {
            String resultado = resultados.get(m);
            String[] lineas = resultado.split("\n");
            String boardInicial = lineas[0];

            JPanel manoPanel = new JPanel(new GridBagLayout());
            manoPanel.setBorder(new EmptyBorder(0, 0, 80, 0));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            
            JLabel boardLabel = new JLabel("Board inicial: " + boardInicial);
            boardLabel.setFont(new Font("Arial", Font.BOLD, 20));
            boardLabel.setHorizontalAlignment(SwingConstants.CENTER);
            manoPanel.add(boardLabel, gbc);

            JPanel cartasPanel = new JPanel();
            cartasPanel.setLayout(new GridLayout(lineas.length - 1, 1));
            
            for (int i = 1; i < lineas.length; i++) {
                String linea = lineas[i];
                String[] partes = linea.split(": ");
                String jugador = partes[0];
                String mano = partes[1];

                JPanel jugadorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                
                JLabel jugadorLabel = new JLabel(jugador + ": ");
                jugadorLabel.setFont(new Font("Arial", Font.PLAIN, 18));
                jugadorPanel.add(jugadorLabel);
                
                for (int j = 0; j < mano.length(); j += 2) {
                    String carta = mano.substring(j, j + 2);
                    String imagePath = UtilidadesGUI.getCartaPath(carta);
                    if (imagePath != null) {
                        ImageIcon icon = new ImageIcon(imagePath);
                        Image img = icon.getImage().getScaledInstance(75, 100, Image.SCALE_SMOOTH);
                        ImageIcon scaledIcon = new ImageIcon(img);
                        JLabel cartaLabel = new JLabel(scaledIcon);
                        jugadorPanel.add(cartaLabel);
                    }
                }
                cartasPanel.add(jugadorPanel);
            }

            gbc.gridy = 1;
            manoPanel.add(cartasPanel, gbc);
            resultadosPanel.add(manoPanel);
        }

        JScrollPane scrollPane = new JScrollPane(resultadosPanel);
        add(scrollPane, BorderLayout.CENTER);
    }
}
