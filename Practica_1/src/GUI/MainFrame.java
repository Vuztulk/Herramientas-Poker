package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controlador.Controller;
import Modelo.Model;
import Vista.View;

import javax.swing.JButton;
import java.awt.CardLayout;
import javax.swing.JLayeredPane;
import javax.swing.JTabbedPane;
import javax.swing.JSpinner;
import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JProgressBar;
import java.awt.Button;
import javax.swing.JMenuBar;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout;
    private Controller controller;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
					Model model = new Model();
		            View vista = new View();
		            Controller control = new Controller(model, vista);
		            
		            MainFrame mainFrame = new MainFrame(control);
		            mainFrame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

    public MainFrame(Controller controller) {
        this.controller = controller;

        setTitle("Evaluacion de manos de Poker");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);

        MenuPrincipal menuPrincipal = new MenuPrincipal(controller);
        MenuArchivos guiPanel = new MenuArchivos(controller);
        getContentPane().add(menuPrincipal, "MenuPrincipal");
        getContentPane().add(guiPanel, "Archivos");

        mostrarPantalla("MenuPrincipal");
    }

    public void mostrarPantalla(String nombrePanel) {
        cardLayout.show(getContentPane(), nombrePanel);
    }
}
