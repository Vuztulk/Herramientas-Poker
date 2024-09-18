package GUI;

import java.awt.CardLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JDialog;
import Controlador.Controller;
import Modelo.Model;
import Vista.View;

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

        MenuPrincipal menuPrincipal = new MenuPrincipal(controller, this);
        getContentPane().add(menuPrincipal, "MenuPrincipal");

        mostrarPantalla("MenuPrincipal");
    }

    public void mostrarPantalla(String nombrePanel) {
        cardLayout.show(getContentPane(), nombrePanel);
    }
    
    public void mostrarMenuArchivosDialog() {
        JDialog dialog = new JDialog(this, "Seleccionar Archivos", true); // Modal dialog
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        MenuArchivos menuArchivos = new MenuArchivos(controller);
        dialog.add(menuArchivos);

        dialog.setVisible(true);
    }
}
