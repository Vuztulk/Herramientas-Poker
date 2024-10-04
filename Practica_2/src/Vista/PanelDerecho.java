package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PanelDerecho extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextArea campoSeleccionado;
    private List<JButton> botonesRango;
    private JugadorFrame jugadorFrame;

    public PanelDerecho(List<JButton> botonesRango, JugadorFrame jugadorFrame, JTextArea campoSeleccionado) {
        this.botonesRango = botonesRango;
        this.jugadorFrame = jugadorFrame;
        this.campoSeleccionado = campoSeleccionado;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 40, 10, 40));
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        add(new JLabel("SHIFT, CTRL, ALT"));
        add(new JLabel("Modificar selección"));
        add(Box.createVerticalStrut(10));

        String[] botonesSeleccion = {"Todos", "Cualquier Suited", "Cualquier Broadway", "Cualquier Par", "Limpiar"};

        for (String texto : botonesSeleccion) {
            JButton boton = new JButton(texto);
            boton.addActionListener(e -> manejarBotonSeleccion(texto));
            add(boton);
            add(Box.createVerticalStrut(5));
        }
        add(Box.createVerticalGlue());

        campoSeleccionado.setEditable(false);
        campoSeleccionado.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(campoSeleccionado);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        add(new JLabel("Seleccionado:"));
        add(scrollPane);
    }

    private void manejarBotonSeleccion(String accion) {
        String[] broadway = {"A", "K", "Q", "J", "T"};

        for (JButton b : botonesRango) {
            boolean esSuited = b.getText().endsWith("s");
            boolean esPar = b.getText().charAt(0) == b.getText().charAt(1);
            String carta1 = b.getText().substring(0, 1);
            String carta2 = b.getText().substring(1, 2);

            if (debeSeleccionarBoton(accion, esSuited, esPar, broadway, carta1, carta2)) {
                seleccionarBoton(b);
            } else if (accion.equals("Limpiar")) {
                limpiarBoton(b, esSuited, esPar);
            }
        }

        jugadorFrame.actualizarSeleccion();
    }

    private boolean debeSeleccionarBoton(String accion, boolean esSuited, boolean esPar, String[] broadway, String carta1, String carta2) {
        switch (accion) {
            case "Todos":
                return true;
            case "Cualquier Suited":
                return esSuited;
            case "Cualquier Broadway":
                return contiene(broadway, carta1) && contiene(broadway, carta2);
            case "Cualquier Par":
                return esPar;
            default:
                return false;
        }
    }

    private void seleccionarBoton(JButton b) {
        b.setSelected(true);
        b.setBackground(new Color(180, 120, 160));
    }

    private void limpiarBoton(JButton b, boolean esSuited, boolean esPar) {
        b.setSelected(false);
        Color colorFondo = esSuited ? new Color(255, 180, 190) : esPar ? new Color(140, 230, 140) : new Color(170, 210, 230);
        b.setBackground(colorFondo);
    }

    private boolean contiene(String[] arr, String objetivo) {
        for (String s : arr) {
            if (s.equals(objetivo)) return true;
        }
        return false;
    }

    public void actualizarCampoSeleccionado(String texto) {
        campoSeleccionado.setText(texto);
    }
}
