package Vista;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import Controlador.Controller;
import Modelo.Model;

import java.util.ArrayList;
import java.util.List;

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
					Controller control = new Controller(model);

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

		setTitle("Evaluacion equity");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);

		cardLayout = new CardLayout();
		getContentPane().setLayout(cardLayout);

		Mesa menuPrincipal = new Mesa(controller, this);
		getContentPane().add(menuPrincipal, "Mesa");

		cardLayout.show(getContentPane(), "Mesa");
	}

	public List<List<String>> mostrarMenuArchivos(String opcion) {
	    JDialog dialog = new JDialog(this, "Seleccionar Archivos", true);
	    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	    MenuArchivos menuArchivos = new MenuArchivos(controller);
	    menuArchivos.setDialog(dialog);
	    dialog.add(menuArchivos);
	    dialog.pack();
	    dialog.setLocationRelativeTo(this);
	    dialog.setVisible(true);

	    List<List<String>> resultado = new ArrayList<>();
	    String filePath = menuArchivos.getSelectedFilePath();

	    if (filePath != null) {
	        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                if (opcion.equals("Board")) {
	                    resultado.add(procesarBoard(line));
	                } else if (opcion.equals("Jugadores")) {
	                    resultado.addAll(procesarJugadores(line));
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }

	    return resultado;
	}

	private List<String> procesarBoard(String linea) {
	    List<String> cartas = new ArrayList<>();
	    for (int i = 0; i < linea.length(); i += 2) {
	        if (i + 2 <= linea.length()) {
	            cartas.add(linea.substring(i, i + 2));
	        }
	    }
	    return cartas;
	}

	private List<List<String>> procesarJugadores(String linea) {
	    List<List<String>> jugadores = new ArrayList<>();
	    String[] partes = linea.split(";");

	    for (int i = 1; i < partes.length; i++) {
	        String mano = partes[i].substring(2);

	        if (mano.length() == 4 || mano.length() == 8) {
	            List<String> manoJugador = new ArrayList<>();

	            for (int j = 0; j < mano.length(); j += 2) {
	                manoJugador.add(mano.substring(j, j + 2));
	            }

	            jugadores.add(manoJugador);
	        } else {
	            System.out.println("Error: longitud de mano no válida para jugador " + i);
	        }
	    }

	    return jugadores;
	}


}
