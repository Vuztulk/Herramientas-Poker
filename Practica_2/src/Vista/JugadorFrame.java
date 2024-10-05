package Vista;

import java.awt.*;
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
	private JTextArea textoSelecciones = new JTextArea();
	PanelInferior panelInferior;
	private Controlador controlador;
	private int idJugador;
	String cartasRanking[];
	JTextField campoTextoJugador;

	public JugadorFrame(Controlador controlador, int idJugador, String rangoInput, JTextField campoTextoJugador) {
		this.controlador = controlador;
		this.idJugador = idJugador;
		this.campoTextoJugador = campoTextoJugador;
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

		PanelCuadricula panelCuadricula = new PanelCuadricula(
				new String[] { "A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2" }, this);
		JPanel bordeCuadricula = new JPanel(new BorderLayout());
		bordeCuadricula.setBorder(new EmptyBorder(10, 10, 10, 10));
		bordeCuadricula.add(panelCuadricula, BorderLayout.CENTER);
		panelPreflop.add(new JScrollPane(bordeCuadricula), BorderLayout.CENTER);

		botonesRango = panelCuadricula.getBotonesRango();

		JPanel panelDerecho = new PanelDerecho(botonesRango, this, textoSelecciones);
		panelPreflop.add(panelDerecho, BorderLayout.EAST);

		panelInferior = crearPanelInferior(campoTextoJugador);
		panelPreflop.add(panelInferior, BorderLayout.SOUTH);

		procesarRango(rangoInput);

		panelContenido.add(panelPestanas, BorderLayout.CENTER);
		setVisible(true);
	}

	private PanelInferior crearPanelInferior(JTextField campoTextoJugador) {
		return new PanelInferior(controlador, idJugador, campoTextoJugador, textoSelecciones, this, botonesRango,
				cartasRanking, seleccionesGuardadas);
	}

	private void procesarRango(String rangoInput) {
		controlador.setRangoJugador(idJugador, rangoInput);
		Set<String> rangos = controlador.getRangoJugador(idJugador);

		for (String rango : rangos) {
			for (JButton boton : botonesRango) {
				if (boton.getText().equals(rango)) {
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

		textoSelecciones.setText(seleccionado.toString());

		double porcentaje = (totalCartasSeleccionadas * 100.0) / botonesRango.size();
		panelInferior.modificarCampoPorcentaje(porcentaje);
		controlador.setPorcentajeJugador(idJugador, porcentaje);
	}

	public String getSeleccionesGuardadas() {
		return String.join(", ", seleccionesGuardadas);
	}
}
