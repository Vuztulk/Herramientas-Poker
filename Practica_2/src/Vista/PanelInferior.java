package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Controlador.Controlador;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

public class PanelInferior extends JPanel {

	private static final long serialVersionUID = 1L;
	private JSlider slider;
	private JTextField campoPorcentaje;
	private Controlador controlador;
	private int idJugador;
	private List<String> seleccionesGuardadas;
	private JugadorFrame jugadorFrame;
	private List<JButton> botonesRango;
	String[] cartasRanking;

	public PanelInferior(Controlador controlador, int idJugador, JTextField campoTextoJugador,
			JTextArea campoSeleccionado, JugadorFrame jugadorFrame, List<JButton> botonesRango,
			String[] cartasRanking, List<String> seleccionesGuardadas) {
		
		this.controlador = controlador;
		this.idJugador = idJugador;
		this.jugadorFrame = jugadorFrame;
		this.botonesRango = botonesRango;
		this.cartasRanking = cartasRanking;
		this.seleccionesGuardadas = seleccionesGuardadas;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(10, 10, 10, 10));

		crearPanelSlider(campoTextoJugador);
		crearPanelBotones(campoTextoJugador);
	}

	private void crearPanelSlider(JTextField campoTextoJugador) {
		slider = new JSlider(0, 1690, 0);
        campoPorcentaje = new JTextField("0.0%");
        campoPorcentaje.setEditable(true);
        campoPorcentaje.setPreferredSize(new Dimension(55, 20));
		slider.setMajorTickSpacing(169);
		slider.setMinorTickSpacing(84);
		slider.setPaintLabels(false);

		slider.addChangeListener(e -> {
			double porcentaje = slider.getValue() / 16.9;
			campoPorcentaje.setText(String.format("%.1f%%", porcentaje));
			controlador.setPorcentajeJugador(idJugador, porcentaje);
			aplicarSeleccion(porcentaje);
		});

		campoPorcentaje.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				actualizarDesdeTexto();
			}
		});

		campoPorcentaje.addActionListener(e -> actualizarDesdeTexto());

		JPanel sliderPanel = new JPanel();
		sliderPanel.add(slider);
		sliderPanel.add(campoPorcentaje);
		add(sliderPanel);
	}

	private void crearPanelBotones(JTextField campoTextoJugador) {
		JPanel panelInferior = new JPanel();
		panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER));

		JButton botonAceptar = new JButton("Aceptar");
		JButton botonAplicar = new JButton("Aplicar");
		JButton botonCancelar = new JButton("Cancelar");

		botonAceptar.addActionListener(e -> {
			String rangoSeleccionado = jugadorFrame.getSeleccionesGuardadas();
			campoTextoJugador.setText(rangoSeleccionado);
			controlador.setRangoJugador(idJugador, rangoSeleccionado);
			jugadorFrame.dispose();
		});
		botonAplicar.addActionListener(e -> {
			String rangoSeleccionado = jugadorFrame.getSeleccionesGuardadas();
			campoTextoJugador.setText(rangoSeleccionado);
			controlador.setRangoJugador(idJugador, rangoSeleccionado);
		});
		botonCancelar.addActionListener(e -> {
			jugadorFrame.dispose();
		});

		panelInferior.add(botonAceptar);
		panelInferior.add(botonCancelar);
		panelInferior.add(botonAplicar);
		add(panelInferior);
	}

	private void aplicarSeleccion(double porcentaje) {
		int n_valores = (int) Math.round((porcentaje / 100) * 169);

		seleccionesGuardadas.clear();
		for (JButton b : botonesRango) {
			b.setSelected(false);

			String textoBoton = b.getText();
			Color colorOriginal = textoBoton.length() == 2 ? new Color(140, 230, 140): textoBoton.endsWith("s") ? new Color(255, 180, 190) : new Color(170, 210, 230);
			b.setBackground(colorOriginal);
		}

		for (int i = 0; i < n_valores && i < cartasRanking.length; i++) {
			for (JButton b : botonesRango) {
				if (b.getText().equals(cartasRanking[i])) {
					b.setSelected(true);
					b.setBackground(new Color(180, 120, 160));
					seleccionesGuardadas.add(b.getText());
					break;
				}
			}
		}

		jugadorFrame.actualizarSeleccion();
	}

	private void actualizarDesdeTexto() {
		try {
			String texto = campoPorcentaje.getText().replace("%", "").replace(",", ".").trim();
			double porcentaje = Double.parseDouble(texto);
			if (porcentaje >= 0 && porcentaje <= 100) {
				slider.setValue((int) (porcentaje * 16.9));
				controlador.setPorcentajeJugador(idJugador, porcentaje);
				aplicarSeleccion(porcentaje);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Introduzca un porcentaje valido entre 0 y 100", "Error",JOptionPane.ERROR_MESSAGE);
			campoPorcentaje.setText(String.format("%.1f%%", slider.getValue() / 16.9));
		}
	}
	
	public void modificarCampoPorcentaje(double porcentaje){
		campoPorcentaje.setText(String.format("%.1f%%", porcentaje));
	}

}
