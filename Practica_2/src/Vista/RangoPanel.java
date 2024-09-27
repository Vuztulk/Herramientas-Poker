package Vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class RangoPanel extends JPanel {
    private final Map<String, List<String>> cartasGraficas;

    public RangoPanel(Map<String, List<String>> cartasGraficas) {
        this.cartasGraficas = cartasGraficas;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 20, y = 20, width = 50, height = 70; // Ajusta según necesites

        // Dibuja los cuadrados amarillos para cada carta en cartasGraficas
        for (Map.Entry<String, List<String>> entry : cartasGraficas.entrySet()) {
            for (String carta : entry.getValue()) {
                g.setColor(Color.YELLOW);
                g.fillRect(x, y, width, height);
                g.setColor(Color.BLACK);
                g.drawString(carta, x + 10, y + 35); // Coloca el texto de la carta
                x += width + 10; // Incrementar la posición x
            }
            x = 20; // Resetear x
            y += height + 10; // Incrementar la posición y
        }
    }
}
