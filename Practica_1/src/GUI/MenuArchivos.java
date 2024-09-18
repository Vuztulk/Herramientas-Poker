package GUI;

import Controlador.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MenuArchivos extends JPanel {

	private static final long serialVersionUID = 1L;
	private Controller controller;
	private JTextField fileInputField;
	private File archivoEntrada;

	public MenuArchivos(Controller controller) {
		this.controller = controller;

		JPanel topPanel = new JPanel(new GridLayout(3, 1));
		topPanel.setBounds(0, 53, 733, 93);

		JPanel fileInputPanel = new JPanel();
		fileInputField = new JTextField(20);
		fileInputField.setBounds(193, 6, 166, 19);
		JButton browseInputButton = new JButton("Seleccionar archivo de entrada");
		browseInputButton.setBounds(364, 5, 175, 21);
		browseInputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seleccionarArchivoEntrada();
			}
		});
		fileInputPanel.setLayout(null);
		fileInputPanel.add(fileInputField);
		fileInputPanel.add(browseInputButton);
		topPanel.add(fileInputPanel);

		setLayout(null);

		add(topPanel);

		JButton leerArchivoButton = new JButton("Leer Archivo");
		leerArchivoButton.setBounds(292, 156, 169, 31);
		leerArchivoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				leerArchivoEntrada();
			}
		});
		add(leerArchivoButton);
	}

	private void seleccionarArchivoEntrada() {
		try {
			JFileChooser fileChooser = new JFileChooser();
			int returnValue = fileChooser.showOpenDialog(this);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				archivoEntrada = fileChooser.getSelectedFile();
				fileInputField.setText(archivoEntrada.getAbsolutePath());
			}
		} catch (Exception e) {

		}
	}

	private void leerArchivoEntrada() {
		if (archivoEntrada != null) {
			try {
				controller.leerYProcesarArchivo(archivoEntrada.getAbsolutePath());
				JOptionPane.showMessageDialog(this, "Archivo leido correctamente.");
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error al leer el archivo.");
			}
		} else {
			JOptionPane.showMessageDialog(this, "Seleccione un archivo de entrada primero.");
		}
	}
}
