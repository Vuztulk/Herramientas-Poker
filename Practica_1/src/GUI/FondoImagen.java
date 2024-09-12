package GUI;

import javax.swing.*;
import java.awt.*;

class FondoImagen extends JPanel {
    private Image imagen;

    public FondoImagen(String rutaImagen) {
        this.imagen = new ImageIcon(rutaImagen).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibuja la imagen en todo el tamaño del panel
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
    }
}

