package Vista;

import javax.swing.*;
import Controlador.Controller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class MenuArchivos extends JPanel {

	private static final long serialVersionUID = 1L;
	private Controller controller;
	private File archivoEntrada;
	private JTextField textField;
	private JDialog dialog;
	private List<String> contenidoArchivo;
	private int apartadoSeleccionado = 1;

	public MenuArchivos(Controller controller) {
		this.controller = controller;
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		JButton leerArchivoButton = new JButton("Leer Archivo");
		leerArchivoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				leerArchivoEntrada();
			}
		});
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		add(leerArchivoButton, gbc);

		textField = new JTextField(20);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		add(textField, gbc);

		JButton browseInputButton = new JButton("Seleccionar archivo de entrada");
		browseInputButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seleccionarArchivo();
			}
		});
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		add(browseInputButton, gbc);
	}

	public void setDialog(JDialog dialog) {
		this.dialog = dialog;
	}

	private void leerArchivoEntrada() {
		if (archivoEntrada != null) {
			try {
				JOptionPane.showMessageDialog(this, "Archivo leido correctamente");

				if (dialog != null) {
					dialog.dispose();
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error al leer el archivo");
			}
		} else {
			JOptionPane.showMessageDialog(this, "Seleccione un archivo de entrada");
		}
	}

	private void seleccionarArchivo() {
	    try {
	        JFileChooser fileChooser = new JFileChooser();
	        int returnValue = fileChooser.showOpenDialog(this);
	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	            archivoEntrada = fileChooser.getSelectedFile();
	            textField.setText(archivoEntrada.getAbsolutePath());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error al seleccionar archivo: " + e.getMessage());
	    }
	}


	public String getSelectedFilePath() {
	    return archivoEntrada != null ? archivoEntrada.getAbsolutePath() : null;
	}

	public List<String> getInfoArchivo() {
		return contenidoArchivo;
	}

	public int getApartado() {
		return apartadoSeleccionado;
	}
}
