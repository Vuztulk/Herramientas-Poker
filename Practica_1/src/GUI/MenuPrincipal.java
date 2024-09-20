package GUI;

import Controlador.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JPanel {

    private static final long serialVersionUID = 1L;
    private Controller controller;
    private MainFrame mainFrame;
    
    public MenuPrincipal(Controller controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        
        JLayeredPane layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);
        
        layeredPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                layeredPane.setSize(getSize());
            }
        });
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setOpaque(false);
        contentPanel.setBounds(0, 0, 1400, 800);
        layeredPane.add(contentPanel, JLayeredPane.PALETTE_LAYER);

        JLabel Titulo = new JLabel("Practica 1");
        Titulo.setForeground(new Color(255, 255, 255));
        Titulo.setFont(new Font("Tahoma", Font.PLAIN, 60));
        Titulo.setBounds(650, 67, 276, 62);
        contentPanel.add(Titulo);
        
        JButton Apartado_1 = new JButton("");
        setButtonIcon(Apartado_1, "src\\GUI\\Imagenes\\Cartas\\ace_of_diamonds.png", 102, 142);
        Apartado_1.setBounds(720, 156, 102, 142);
        Apartado_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.mostrarPantalla("MenuApartado1");
            }
        });
        contentPanel.add(Apartado_1);
        
        JButton Apartado_2 = new JButton("");
        setButtonIcon(Apartado_2, "src\\GUI\\Imagenes\\Cartas\\2_of_diamonds.png", 102, 142);
        Apartado_2.setBounds(720, 308, 102, 142);
        Apartado_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.mostrarPantalla("MenuApartado2");
            }
        });
        contentPanel.add(Apartado_2);
        
        JButton Apartado_3 = new JButton("");
        setButtonIcon(Apartado_3, "src\\GUI\\Imagenes\\Cartas\\3_of_diamonds.png", 102, 142);
        Apartado_3.setBounds(720, 460, 102, 142);
        Apartado_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.mostrarPantalla("MenuApartado3");
            }
        });
        contentPanel.add(Apartado_3);
        
        JButton Apartado_4 = new JButton("");
        setButtonIcon(Apartado_4, "src\\GUI\\Imagenes\\Cartas\\4_of_diamonds.png", 102, 142);
        Apartado_4.setBounds(720, 612, 102, 142);
        Apartado_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.mostrarPantalla("MenuApartado4");
            }
        });
        contentPanel.add(Apartado_4);
        
        JButton openMenuArchivosButton = new JButton("");
        setButtonIcon(openMenuArchivosButton, "src\\GUI\\Imagenes\\Archivos.png", 91, 68);
        openMenuArchivosButton.setBounds(1360, 624, 102, 115);
        openMenuArchivosButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.mostrarMenuArchivos();
            }
        });
        contentPanel.add(openMenuArchivosButton);

        JLabel Fondo = new JLabel();
        Fondo.setIcon(new ImageIcon(getClass().getResource("/GUI/Imagenes/Fondo.jpg")));
        Fondo.setBounds(0, 0, 1400, 800);
        layeredPane.add(Fondo, JLayeredPane.DEFAULT_LAYER);

        layeredPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                Dimension size = layeredPane.getSize();
                Fondo.setSize(size);
                contentPanel.setSize(size);
            }
        });
    }
    
    private void setButtonIcon(JButton button, String imagePath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        button.setIcon(scaledIcon);
    }
}

