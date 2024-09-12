package GUI;

import Controlador.Controller;
import Modelo.Model;
import Vista.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GUI extends JFrame {

    /*private Controller controller;
    private JTextArea outputArea;
    private JComboBox<String> comboBoxApartados;
    private JTextField fileInputField;
    private JTextField fileOutputField;

    public GUI() {
        setTitle("Poker Hand Evaluator");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Modelo, vista y controlador
        Model model = new Model();
        View view = new View();
        controller = new Controller(model, view);

        // Panel superior (selección de archivo y apartado)
        JPanel topPanel = new JPanel(new GridLayout(3, 1));

        // Selección de archivo de entrada
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

        // Selección de archivo de salida
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

        // Selección de apartado
        JPanel comboBoxPanel = new JPanel();
        comboBoxApartados = new JComboBox<>(new String[] {"Apartado 1", "Apartado 2", "Apartado 3"});
        comboBoxPanel.add(new JLabel("Selecciona un apartado:"));
        comboBoxPanel.add(comboBoxApartados);
        topPanel.add(comboBoxPanel);

        add(topPanel, BorderLayout.NORTH);

        // Área de resultados
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Botón para ejecutar el apartado seleccionado
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

        // Ejecutar el apartado correspondiente
        controller.procesarArchivo(String.valueOf(numeroApartado), archivoEntrada, archivoSalida);

        // Mostrar el contenido del archivo de salida en el área de texto
        outputArea.setText("Apartado " + numeroApartado + " ejecutado.\nRevisa el archivo de salida.");
    }*/
}
