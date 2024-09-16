package GUI;

import Controlador.Controller;
import Vista.View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MenuArchivos extends JPanel {

    private Controller controller;
    private JTextArea outputArea;
    private JComboBox<String> comboBoxApartados;
    private JTextField fileInputField;
    private JTextField fileOutputField;

    public MenuArchivos(Controller controller) {
        this.controller = controller;

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(3, 1));

        JPanel fileInputPanel = new JPanel();
        fileInputField = new JTextField(20);
        JButton browseInputButton = new JButton("Seleccionar archivo de entrada");
        browseInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarArchivoEntrada();
            }
        });
        fileInputPanel.add(fileInputField);
        fileInputPanel.add(browseInputButton);
        topPanel.add(fileInputPanel);

        JPanel fileOutputPanel = new JPanel();
        fileOutputField = new JTextField(20);
        JButton browseOutputButton = new JButton("Seleccionar archivo de salida");
        browseOutputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarArchivoSalida();
            }
        });
        fileOutputPanel.add(fileOutputField);
        fileOutputPanel.add(browseOutputButton);
        topPanel.add(fileOutputPanel);

        JPanel comboBoxPanel = new JPanel();
        comboBoxApartados = new JComboBox<>(new String[] {"Apartado 1", "Apartado 2", "Apartado 3"});
        comboBoxPanel.add(new JLabel("Selecciona un apartado:"));
        comboBoxPanel.add(comboBoxApartados);
        topPanel.add(comboBoxPanel);

        add(topPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton ejecutarButton = new JButton("Ejecutar");
        ejecutarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarApartado();
            }
        });
        bottomPanel.add(ejecutarButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void seleccionarArchivoEntrada() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            fileInputField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void seleccionarArchivoSalida() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            fileOutputField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void ejecutarApartado() {
        String archivoEntrada = fileInputField.getText();
        String archivoSalida = fileOutputField.getText();
        String apartadoSeleccionado = (String) comboBoxApartados.getSelectedItem();

        if (archivoEntrada.isEmpty() || archivoSalida.isEmpty()) {
            outputArea.setText("Por favor, selecciona los archivos de entrada y salida.");
            return;
        }

        int numeroApartado = comboBoxApartados.getSelectedIndex() + 1;

        controller.procesarOrden(String.valueOf(numeroApartado), archivoEntrada, archivoSalida);

        outputArea.setText("Apartado " + numeroApartado + " ejecutado.\nRevisa el archivo de salida.");
    }
}
