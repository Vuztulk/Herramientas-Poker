package Vista;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Controlador.Controlador;
import Modelo.Sklansky_Chubukov;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JugadorFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel panelContenido;
	private List<JButton> botonesRango = new ArrayList<>();
	private List<String> seleccionesGuardadas = new ArrayList<>();
	private JTextArea campoSeleccionado = new JTextArea();
	private JSlider slider = new JSlider(0, 1690, 0);;
	private JTextField campoPorcentaje;
	private Controlador controlador;
	private int idJugador;
	String cartasRanking[];

	public JugadorFrame(Controlador controlador, int idJugador, String rangoInput, JTextField campoTextoJugador) {
		this.controlador = controlador;
		this.idJugador = idJugador;
		this.cartasRanking = new Sklansky_Chubukov().getCartasOrdenadas();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1200, 800);
		setTitle("Distribución de Hold'em [Jugador " + idJugador + "]");

		panelContenido = new JPanel(new BorderLayout(5, 5));
		setContentPane(panelContenido);

		JTabbedPane panelPestanas = new JTabbedPane(JTabbedPane.TOP);

		JPanel panelPreflop = new JPanel(new BorderLayout());
		panelPestanas.addTab("Preflop", null, panelPreflop, "Preflop");

		JPanel panelAnalisis = new PanelAnalisis(seleccionesGuardadas, controlador);
		panelPestanas.addTab("Analisis", null, panelAnalisis, "Analisis de Rango");

		JPanel panelCuadricula = new JPanel(new GridLayout(13, 13, 2, 2));
		JPanel bordeCuadricula = new JPanel(new BorderLayout());
		bordeCuadricula.setBorder(new EmptyBorder(10, 10, 10, 10));
		bordeCuadricula.add(panelCuadricula, BorderLayout.CENTER);
		panelPreflop.add(new JScrollPane(bordeCuadricula), BorderLayout.CENTER);
		agregarBotonesRango(panelCuadricula);

		JPanel panelDerecho = new PanelDerecho(botonesRango, this, campoSeleccionado);
		panelPreflop.add(panelDerecho, BorderLayout.EAST);

		PanelInferior panelInferior = crearPanelInferior(campoTextoJugador);
		panelPreflop.add(panelInferior, BorderLayout.SOUTH);

		procesarRango(rangoInput);
		panelContenido.add(panelPestanas, BorderLayout.CENTER);
		setVisible(true);
	}
	
	private PanelInferior crearPanelInferior(JTextField campoTextoJugador) {
        slider = new JSlider(0, 1690, 0);
        campoPorcentaje = new JTextField("0.0%");
        campoPorcentaje.setEditable(true);
        campoPorcentaje.setPreferredSize(new Dimension(55, 20));

        PanelInferior panelInferior = new PanelInferior(controlador, idJugador, campoTextoJugador, campoSeleccionado, this, botonesRango, cartasRanking, slider);
        return panelInferior;
    }
	
	private void agregarBotonesRango(JPanel panelCuadricula) {

		String[] valores = { "A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2" };

		for (int i = 0; i < valores.length; i++) {
			for (int j = 0; j < valores.length; j++) {
				String textoBoton;
				Color colorFondo;

				if (i == j)
					textoBoton = valores[i] + valores[i];
				else if (i < j)
					textoBoton = valores[i] + valores[j] + "s";
				else
					textoBoton = valores[j] + valores[i] + "o";

				if (i == j)
					colorFondo = new Color(140, 230, 140);
				else if (i < j)
					colorFondo = new Color(255, 180, 190);
				else
					colorFondo = new Color(170, 210, 230);

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
			boton.setBackground(boton.isSelected() ? new Color(180, 120, 160) : colorFondo);
			actualizarSeleccion();
		});

		return boton;
	}	

	private void procesarRango(String rangoInput) {

		controlador.setRangoJugador(idJugador, rangoInput);
		Set<String> cartas = controlador.getRangoJugador(idJugador);

		for (String carta : cartas) {
			for (JButton boton : botonesRango) {
				if (boton.getText().equals(carta)) {
					boton.setSelected(true);
					boton.setBackground(new Color(180, 120, 160));
				}
			}
		}

		actualizarSeleccion();
	}

	public void actualizarSeleccion() {
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

		if (seleccionado.length() > 0) {
			seleccionado.setLength(seleccionado.length() - 2);
		}

		campoSeleccionado.setText(seleccionado.toString());

		double porcentaje = (totalCartasSeleccionadas * 100.0) / botonesRango.size();
		campoPorcentaje.setText(String.format("%.1f%%", porcentaje));
		controlador.setPorcentajeJugador(idJugador, porcentaje);
	}

	public String getSeleccionesGuardadas() {
		return String.join(", ", seleccionesGuardadas);
	}

}