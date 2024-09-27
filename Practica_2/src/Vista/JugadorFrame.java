package Vista;

import Modelo.RangoCartas;
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
    private JTextArea campoSeleccionado;
    private JSlider slider;
    private JTextField campoTextoJugador;
    private JTextField campoPorcentaje;

    public JugadorFrame(int idJugador, String rangoInput, JTextField campoTextoJugador) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(0, 0, 1200, 800);
        setTitle("Distribucion de Hold'em [Jugador " + idJugador + "]");
        panelContenido = new JPanel();
        panelContenido.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panelContenido);
        panelContenido.setLayout(new BorderLayout());

        JTabbedPane panelPestanas = new JTabbedPane(JTabbedPane.TOP);
        
        JPanel panelPreflop = new JPanel(new BorderLayout());
        panelPestanas.addTab("Preflop", null, panelPreflop, "Preflop");
        
        JPanel panelCartas = new JPanel();
        panelPestanas.addTab("Cartas", null, panelCartas, "Cartas");
        
        JPanel panelCuadricula = new JPanel(new GridLayout(13, 13, 2, 2));
        panelPreflop.add(panelCuadricula, BorderLayout.CENTER);
        
        JPanel panelSeparadorCuadricula = new JPanel();
        panelSeparadorCuadricula.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelSeparadorCuadricula.setLayout(new BorderLayout());
        panelSeparadorCuadricula.add(panelCuadricula, BorderLayout.CENTER);
        panelPreflop.add(panelSeparadorCuadricula, BorderLayout.CENTER);
        
        String[] valores = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};

        for (int i = 0; i < valores.length; i++) {
            for (int j = 0; j < valores.length; j++) {
                String textoBoton;
                Color colorFondo;

                if (i == j) {
                    textoBoton = valores[i] + valores[i];
                    colorFondo = new Color(140, 230, 140);
                } else if (i < j) {
                    textoBoton = valores[i] + valores[j] + "s";
                    colorFondo = new Color(255, 180, 190);
                } else {
                    textoBoton = valores[j] + valores[i] + "o";
                    colorFondo = new Color(170, 210, 230);
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

        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBorder(new EmptyBorder(10, 40, 10, 40));
        panelPreflop.add(panelDerecho, BorderLayout.EAST);
        panelDerecho.add(new JLabel("SHIFT, CTRL, ALT"));
        panelDerecho.add(new JLabel("Modificar seleccion"));
        panelDerecho.add(Box.createVerticalStrut(10));

        String[] botonesSeleccion = {"Todos", "Cualquier Suited", "Cualquier Broadway", "Cualquier Par", "Limpiar"};
        for (String texto : botonesSeleccion) {
            JButton boton = new JButton(texto);
            boton.addActionListener(e -> manejarBotonSeleccion(texto));
            panelDerecho.add(boton);
            panelDerecho.add(Box.createVerticalStrut(5));
        }
        panelDerecho.add(Box.createVerticalStrut(20));
        
//CUADRO SELECCION
        
        JLabel etiquetaSeleccionada = new JLabel("Seleccionado:");
        campoSeleccionado = new JTextArea();
        campoSeleccionado.setEditable(false);
        campoSeleccionado.setLineWrap(true);
        campoSeleccionado.setWrapStyleWord(true);
        campoSeleccionado.setPreferredSize(new Dimension(200, 100));
        JScrollPane scrollPane = new JScrollPane(campoSeleccionado);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        panelDerecho.add(etiquetaSeleccionada);
        panelDerecho.add(scrollPane);
        panelDerecho.add(Box.createVerticalGlue());
        
//SLIDER PANEL
        
        JPanel sliderPanel = new JPanel();
        slider = new JSlider(0, 100, 0);
        slider.setMajorTickSpacing(10);
        slider.setPaintLabels(true);
        campoPorcentaje = new JTextField("0%");
        campoPorcentaje.setEditable(false);
        campoPorcentaje.setColumns(5);
        sliderPanel.add(slider);
        sliderPanel.add(campoPorcentaje);
        
//PANEL INFERIOR
        
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botonAceptar = new JButton("Aceptar");
        JButton botonCancelar = new JButton("Cancelar");
        JButton botonAplicar = new JButton("Aplicar");
        
        botonAceptar.addActionListener(e -> {
            String rangoSeleccionado = getSeleccionesGuardadas();
            campoTextoJugador.setText(rangoSeleccionado);
            dispose();
        });

        botonAplicar.addActionListener(e -> {
            String rangoSeleccionado = getSeleccionesGuardadas();
            campoTextoJugador.setText(rangoSeleccionado);
        });

        botonCancelar.addActionListener(e -> dispose());

        panelInferior.add(botonAceptar);
        panelInferior.add(botonCancelar);
        panelInferior.add(botonAplicar);

        JPanel panelInferiorCompleto = new JPanel();
        panelInferiorCompleto.setLayout(new BoxLayout(panelInferiorCompleto, BoxLayout.Y_AXIS));
        panelInferiorCompleto.add(sliderPanel);
        panelInferiorCompleto.add(panelInferior);

        panelPreflop.add(panelInferiorCompleto, BorderLayout.SOUTH);
        
        procesarRango(rangoInput);//Si viene con input en texto se muestra con ese input en vez de mostrarse limpio
        double porcentaje = (seleccionesGuardadas.size() / 169.0) * 100;
        slider.setValue((int) porcentaje);
        campoPorcentaje.setText(String.format("%.2f%%", porcentaje));
        
        panelContenido.add(panelPestanas, BorderLayout.CENTER);
        setVisible(true);
    }

    private void procesarRango(String rangoInput) {
        RangoCartas rangoCartas = new RangoCartas(rangoInput);
        for (String carta : rangoCartas.getRangos()) {
            for (JButton boton : botonesRango) {
                if (boton.getText().equals(carta)) {
                    boton.setSelected(true);
                    boton.setBackground(Color.RED);
                }
            }
        }
        actualizarSeleccion(); 
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
                String[] bw = {"A", "K", "Q", "J", "T"};
                botonesRango.forEach(b -> {
                    String texto = b.getText();
                    if (texto.length() >= 2 &&
                        (contiene(bw, texto.substring(0, 1)) && contiene(bw, texto.substring(1, 2)))) {
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
        int totalCartasSeleccionadas = 0;

        for (JButton b : botonesRango) {
            if (b.isSelected()) {
                seleccionado.append(b.getText()).append(", ");
                seleccionesGuardadas.add(b.getText());
                totalCartasSeleccionadas++;
            }
        }

        if (seleccionado.length() > 2) {
            seleccionado.setLength(seleccionado.length() - 2);
        }
        campoSeleccionado.setText(seleccionado.toString());
        
        double porcentaje = (totalCartasSeleccionadas / 169.0) * 100;
        slider.setValue((int) porcentaje);
        campoPorcentaje.setText(String.format("%.2f%%", porcentaje));
    }

    public String getSeleccionesGuardadas() {
        return String.join(", ", seleccionesGuardadas);
    }
    

}
