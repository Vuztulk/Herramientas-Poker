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
    private JTextField campoPorcentaje;

    public JugadorFrame(int idJugador, String rangoInput, JTextField campoTextoJugador) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(0, 0, 1200, 800);
        setTitle("Distribución de Hold'em [Jugador " + idJugador + "]");
        panelContenido = new JPanel(new BorderLayout(5, 5));
        setContentPane(panelContenido);

        JTabbedPane panelPestanas = new JTabbedPane(JTabbedPane.TOP);
        JPanel panelPreflop = new JPanel(new BorderLayout());
        panelPestanas.addTab("Preflop", null, panelPreflop, "Preflop");

        JPanel panelCuadricula = new JPanel(new GridLayout(13, 13, 2, 2));
        panelPreflop.add(new JScrollPane(panelCuadricula), BorderLayout.CENTER);

        String[] valores = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};
        agregarBotonesRango(panelCuadricula, valores);

        JPanel panelDerecho = crearPanelDerecho();
        panelPreflop.add(panelDerecho, BorderLayout.EAST);

        JPanel panelInferiorCompleto = crearPanelInferior(campoTextoJugador);
        panelPreflop.add(panelInferiorCompleto, BorderLayout.SOUTH);

        procesarRango(rangoInput);
        actualizarSlider();
        
        panelContenido.add(panelPestanas, BorderLayout.CENTER);
        setVisible(true);
    }

    private void agregarBotonesRango(JPanel panelCuadricula, String[] valores) {
        for (int i = 0; i < valores.length; i++) {
            for (int j = 0; j < valores.length; j++) {
                String textoBoton = (i == j) ? valores[i] + valores[i] : (i < j) ? valores[i] + valores[j] + "s" : valores[j] + valores[i] + "o";
                Color colorFondo = (i == j) ? new Color(140, 230, 140) : (i < j) ? new Color(255, 180, 190) : new Color(170, 210, 230);

                JButton boton = crearBotonRango(textoBoton, colorFondo);
                panelCuadricula.add(boton);
                botonesRango.add(boton);
            }
        }
    }

    private JButton crearBotonRango(String textoBoton, Color colorFondo) {
        JButton boton = new JButton(textoBoton);
        boton.setBackground(colorFondo);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        
        boton.addActionListener(e -> {
            boton.setSelected(!boton.isSelected());
            boton.setBackground(boton.isSelected() ? Color.RED : colorFondo);
            actualizarSeleccion();
        });
        return boton;
    }

    private JPanel crearPanelDerecho() {
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBorder(new EmptyBorder(10, 40, 10, 40));
        panelDerecho.add(new JLabel("SHIFT, CTRL, ALT"));
        panelDerecho.add(new JLabel("Modificar selección"));
        panelDerecho.add(Box.createVerticalStrut(10));

        String[] botonesSeleccion = {"Todos", "Cualquier Suited", "Cualquier Broadway", "Cualquier Par", "Limpiar"};
        for (String texto : botonesSeleccion) {
            JButton boton = new JButton(texto);
            boton.addActionListener(e -> manejarBotonSeleccion(texto));
            panelDerecho.add(boton);
            panelDerecho.add(Box.createVerticalStrut(5));
        }
        panelDerecho.add(Box.createVerticalGlue());

        campoSeleccionado = new JTextArea();
        campoSeleccionado.setEditable(false);
        campoSeleccionado.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(campoSeleccionado);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        panelDerecho.add(new JLabel("Seleccionado:"));
        panelDerecho.add(scrollPane);

        return panelDerecho;
    }

    private JPanel crearPanelInferior(JTextField campoTextoJugador) {
        slider = new JSlider(0, 100, 0);
        slider.setMajorTickSpacing(10);
        slider.setPaintLabels(true);
        campoPorcentaje = new JTextField("0%");
        campoPorcentaje.setEditable(false);
        campoPorcentaje.setPreferredSize(new Dimension(55, 20));
        
        JPanel sliderPanel = new JPanel();
        sliderPanel.add(slider);
        sliderPanel.add(campoPorcentaje);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botonAceptar = new JButton("Aceptar");
        JButton botonAplicar = new JButton("Aplicar");
        JButton botonCancelar = new JButton("Cancelar");

        botonAceptar.addActionListener(e -> {
            campoTextoJugador.setText(getSeleccionesGuardadas());
            dispose();
        });
        botonAplicar.addActionListener(e -> campoTextoJugador.setText(getSeleccionesGuardadas()));
        botonCancelar.addActionListener(e -> dispose());

        panelInferior.add(botonAceptar);
        panelInferior.add(botonCancelar);
        panelInferior.add(botonAplicar);

        JPanel panelInferiorCompleto = new JPanel();
        panelInferiorCompleto.setLayout(new BoxLayout(panelInferiorCompleto, BoxLayout.Y_AXIS));
        panelInferiorCompleto.add(sliderPanel);
        panelInferiorCompleto.add(panelInferior);

        return panelInferiorCompleto;
    }

    private void procesarRango(String rangoInput) {
        RangoCartas rangoCartas = new RangoCartas(rangoInput);
        rangoCartas.getRangos().forEach(carta -> botonesRango.stream()
            .filter(b -> b.getText().equals(carta))
            .forEach(b -> {
                b.setSelected(true);
                b.setBackground(Color.RED);
            })
        );
        actualizarSeleccion();
    }

    private void manejarBotonSeleccion(String accion) {
        String[] broadway = {"A", "K", "Q", "J", "T"};
        
        botonesRango.forEach(b -> {
            boolean esSuited = b.getText().endsWith("s");
            boolean esPar = b.getText().charAt(0) == b.getText().charAt(1);
            String carta1 = b.getText().substring(0, 1);
            String carta2 = b.getText().substring(1, 2);

            switch (accion) {
                case "Todos":
                    b.setSelected(true);
                    b.setBackground(Color.RED);
                    break;
                case "Cualquier Suited":
                    if (esSuited) {
                        b.setSelected(true);
                        b.setBackground(Color.RED);
                    }
                    break;
                case "Cualquier Broadway":
                    if (contiene(broadway, carta1) && contiene(broadway, carta2)) {
                        b.setSelected(true);
                        b.setBackground(Color.RED);
                    }
                    break;
                case "Cualquier Par":
                    if (esPar) {
                        b.setSelected(true);
                        b.setBackground(Color.RED);
                    }
                    break;
                case "Limpiar":
                    b.setSelected(false);
                    b.setBackground(esSuited ? new Color(255, 180, 190) : esPar ? new Color(140, 230, 140) : new Color(170, 210, 230));
                    break;
            }
        });
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

    private void actualizarSlider() {
        double porcentaje = (seleccionesGuardadas.size() / 169.0) * 100;
        slider.setValue((int) porcentaje);
        campoPorcentaje.setText(String.format("%.2f%%", porcentaje));
    }

    public String getSeleccionesGuardadas() {
        return String.join(", ", seleccionesGuardadas);
    }

}
