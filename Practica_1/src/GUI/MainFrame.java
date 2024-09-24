package GUI;

import java.awt.CardLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import Controlador.Controller;
import Modelo.Model;
import Vista.View;
import java.util.List;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout;
	private Controller controller;
	private List<String> mano;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);

		cardLayout = new CardLayout();
		getContentPane().setLayout(cardLayout);

		Mesa menuPrincipal = new Mesa(controller, this);
		getContentPane().add(menuPrincipal, "Mesa");

		mostrarPantalla("Mesa");
	}

	public void mostrarPantalla(String nombrePanel) {
		cardLayout.show(getContentPane(), nombrePanel);
	}

	public List<String> mostrarMenuArchivos() {
		JDialog dialog = new JDialog(this, "Seleccionar Archivos", true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		MenuArchivos menuArchivos = new MenuArchivos(controller);
		menuArchivos.setDialog(dialog);
		dialog.add(menuArchivos);
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);

		this.mano = menuArchivos.getInfoArchivo();
		return this.mano;

	}

	public void agregarPantallas() {
		switch (1) {
		case 1:
			MenuApartado1 pantalla1 = new MenuApartado1(mano, controller);
			getContentPane().add(pantalla1, "MenuApartado1");
			break;
		case 2:
			MenuApartado2 pantalla2 = new MenuApartado2(mano, controller);
			getContentPane().add(pantalla2, "MenuApartado2");
			break;
		case 3:
			MenuApartado3 pantalla3 = new MenuApartado3(mano, controller);
			getContentPane().add(pantalla3, "MenuApartado3");
			break;
		case 4:
			MenuApartado4 pantalla4 = new MenuApartado4(mano, controller);
			getContentPane().add(pantalla4, "MenuApartado4");
			break;
		default:
			JOptionPane.showMessageDialog(this, "Apartado no válido.");
			return;
		}
	}
}
