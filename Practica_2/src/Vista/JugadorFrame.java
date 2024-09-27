package Vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.util.List;

public class JugadorFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel panelContenido;
    private List<JButton> botonesRango = new ArrayList<>();
    private List<String> seleccionesGuardadas = new ArrayList<>();
    private JTextField campoSeleccionado;

    public JugadorFrame(int idJugador) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(0, 0, 1000, 800);
        setTitle("Distribucion de Hold'em [Jugador " + idJugador + "]");
        panelContenido = new JPanel();
        panelContenido.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panelContenido);
        panelContenido.setLayout(new BorderLayout());

        JTabbedPane panelPestanas = new JTabbedPane(JTabbedPane.TOP);

        JPanel panelCartas = new JPanel();
        panelPestanas.addTab("Cartas", null, panelCartas, "Cartas");

        JPanel panelPreflop = new JPanel(new BorderLayout());
        panelPestanas.addTab("Preflop", null, panelPreflop, "Preflop");

        JPanel panelCuadricula = new JPanel(new GridLayout(13, 13, 2, 2));
        panelPreflop.add(panelCuadricula, BorderLayout.CENTER);
        
        JPanel panelGrid = new JPanel();
        panelGrid.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelGrid.setLayout(new BorderLayout());
        panelGrid.add(panelCuadricula, BorderLayout.CENTER);
        panelPreflop.add(panelGrid, BorderLayout.CENTER);
        
        String[] valores = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};

        for (int i = 0; i < valores.length; i++) {
            for (int j = 0; j < valores.length; j++) {
                String textoBoton;
                Color colorFondo;

                if (i == j) {
                    textoBoton = valores[i] + valores[i];
                    colorFondo = new Color(144, 238, 144);
                } else if (i < j) {
                    textoBoton = valores[i] + valores[j] + "s";
                    colorFondo = new Color(255, 182, 193);
                } else {
                    textoBoton = valores[j] + valores[i] + "o";
                    colorFondo = new Color(173, 216, 230);
                }

                JButton boton = new JButton(textoBoton);
                boton.setBackground(colorFondo);
                boton.setOpaque(true);
                boton.setBorderPainted(false);
                boton.setFocusPainted(false);
                boton.addActionListener(e -> {
                    JButton botonOrigen = (JButton) e.getSource();
                    botonOrigen.setSelected(!botonOrigen.isSelected());
                    botonOrigen.setBackground(botonOrigen.isSelected() ? Color.RED : colorFondo);
                    actualizarSeleccion();
                });
                panelCuadricula.add(boton);
                botonesRango.add(boton);
            }
        }
        
        JPanel panelSeparador = new JPanel();
        panelSeparador.setPreferredSize(new Dimension(800, 10));
        
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelPreflop.add(panelDerecho, BorderLayout.EAST);

        panelDerecho.add(new JLabel("SHIFT, CTRL, ALT"));
        panelDerecho.add(new JLabel("modificar selección"));
        panelDerecho.add(Box.createVerticalStrut(10));

        
        String[] botonesSeleccion = {"Todos", "Cualquier Suited", "Cualquier Broadway", "Cualquier Par", "Limpiar"};
        for (String texto : botonesSeleccion) {
            JButton boton = new JButton(texto);
            boton.addActionListener(e -> manejarBotonSeleccion(texto));
            panelDerecho.add(boton);
            panelDerecho.add(Box.createVerticalStrut(5));
        }

        panelDerecho.add(Box.createVerticalStrut(20));
        JLabel etiquetaSeleccionada = new JLabel("Seleccionado:");
        panelDerecho.add(etiquetaSeleccionada);
        campoSeleccionado = new JTextField();
        campoSeleccionado.setEditable(false);
        panelDerecho.add(campoSeleccionado);

        panelDerecho.add(Box.createVerticalGlue());

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonAceptar = new JButton("Aceptar");
        JButton botonCancelar = new JButton("Cancelar");
        JButton botonAplicar = new JButton("Aplicar");
        
        botonAceptar.addActionListener(e -> dispose());
        botonCancelar.addActionListener(e -> dispose());
        botonAplicar.addActionListener(e -> guardarSeleccion());
        
        panelInferior.add(botonAceptar);
        panelInferior.add(botonCancelar);
        panelInferior.add(botonAplicar);
        panelPreflop.add(panelInferior, BorderLayout.SOUTH);

        panelContenido.add(panelPestanas, BorderLayout.CENTER);
        setVisible(true);
    }

    private void manejarBotonSeleccion(String accion) {
        switch (accion) {
            case "Todos":
                botonesRango.forEach(b -> {
                    b.setSelected(true);
                    b.setBackground(Color.RED);
                });
                break;
            case "Cualquier Suited":
                botonesRango.forEach(b -> {
                    if (b.getText().endsWith("s")) {
                        b.setSelected(true);
                        b.setBackground(Color.RED);
                    }
                });
                break;
            case "Cualquier Broadway":
                String[] broadway = {"A", "K", "Q", "J", "T"};
                botonesRango.forEach(b -> {
                    String texto = b.getText();
                    if (texto.length() >= 2 && 
                        (contiene(broadway, texto.substring(0, 1)) && contiene(broadway, texto.substring(1, 2)))) {
                        b.setSelected(true);
                        b.setBackground(Color.RED);
                    }
                });
                break;
            case "Cualquier Par":
                botonesRango.forEach(b -> {
                    if (b.getText().charAt(0) == b.getText().charAt(1)) {
                        b.setSelected(true);
                        b.setBackground(Color.RED);
                    }
                });
                break;
            case "Limpiar":
                botonesRango.forEach(b -> {
                    b.setSelected(false);
                    b.setBackground(b.getBackground() == Color.RED ? 
                        (b.getText().endsWith("s") ? new Color(255, 182, 193) : 
                         b.getText().charAt(0) == b.getText().charAt(1) ? new Color(144, 238, 144) : 
                         new Color(173, 216, 230)) : 
                        b.getBackground());
                });
                break;
        }
        actualizarSeleccion();
    }

    private boolean contiene(String[] arr, String objetivo) {
        for (String s : arr) {
            if (s.equals(objetivo)) return true;
        }
        return false;
    }

    private void actualizarSeleccion() {
        StringBuilder seleccionado = new StringBuilder();
        seleccionesGuardadas.clear();
        for (JButton b : botonesRango) {
            if (b.isSelected()) {
                seleccionado.append(b.getText()).append(", ");
                seleccionesGuardadas.add(b.getText());
            }
        }
        if (seleccionado.length() > 2) {
            seleccionado.setLength(seleccionado.length() - 2);
        }
        campoSeleccionado.setText(seleccionado.toString());
    }

    private void guardarSeleccion() {
        JOptionPane.showMessageDialog(this, "Selección guardada: " + String.join(", ", seleccionesGuardadas));
    }

    public List<String> getSeleccionesGuardadas() {
        return new ArrayList<>(seleccionesGuardadas);
    }
}