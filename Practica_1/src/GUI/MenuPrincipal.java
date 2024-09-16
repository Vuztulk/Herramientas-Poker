package GUI;

import Controlador.Controller;
import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JPanel {

    private static final long serialVersionUID = 1L;
    private Controller controller;

    public MenuPrincipal(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1400, 800));
        add(layeredPane, BorderLayout.CENTER);
        
        JLabel Titulo = new JLabel("Practica");
        Titulo.setFont(new Font("Tahoma", Font.PLAIN, 60));
        Titulo.setBounds(604, 67, 215, 62);
        layeredPane.add(Titulo);
        
        JButton btnNewButton = new JButton("New button");
        btnNewButton.setBounds(604, 407, 226, 109);
        layeredPane.add(btnNewButton);

        JLabel Fondo = new JLabel();
        Fondo.setIcon(new ImageIcon(getClass().getResource("/GUI/Imagenes/Fondo.jpg")));
        Fondo.setBounds(0, 0, 1400, 800);
        layeredPane.add(Fondo, JLayeredPane.DEFAULT_LAYER);

        setPreferredSize(new Dimension(1400, 800));
        
        revalidate();
        repaint();
    }
}
