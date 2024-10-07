package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Controlador.Controlador;
import Modelo.AnalizadorRangos;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PanelAnalisis extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel panelSeleccionCartas;
	private JPanel panelResultados;
	private Map<String, JButton> botonesCartas;
	private List<String> cartasSeleccionadas;
	private List<String> seleccionesGuardadas;
	private AnalizadorRangos analizadorRangos;
	private Controlador controlador;

	public PanelAnalisis(List<String> seleccionesGuardadas, Controlador controlador) {
		this.seleccionesGuardadas = seleccionesGuardadas;
		this.controlador = controlador;

		setLayout(new BorderLayout());

		panelSeleccionCartas = crearPanelCartas();
		panelResultados = new JPanel();
		panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));

		JButton botonAnalizar = new JButton("Analizar");
		botonAnalizar.addActionListener(e -> ejecutarAnalisis());

		add(panelSeleccionCartas, BorderLayout.WEST);
		add(panelResultados, BorderLayout.CENTER);
		add(botonAnalizar, BorderLayout.SOUTH);
	}

	private JPanel crearPanelCartas() {
		JPanel panel = new JPanel(new GridLayout(13, 4, 2, 2));
		botonesCartas = new HashMap<>();
		cartasSeleccionadas = new ArrayList<>();

		String[] rangos = { "A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2" };
		String[] palos = { "h", "s", "d", "c" };
		Color[] coloresPalo = { Color.decode("#D4212B"), Color.decode("#909098"), Color.decode("#D4212B"), Color.decode("#909098") };

		for (String rango : rangos) {
			for (int i = 0; i < palos.length; i++) {
				final Color colorPalo = coloresPalo[i];
				String carta = rango + palos[i];
				JButton boton = new JButton(carta);
				boton.setPreferredSize(new Dimension(60, 60));
				boton.addActionListener(e -> seleccionarCartas(carta, boton, colorPalo));
				botonesCartas.put(carta, boton);
				boton.setBackground(colorPalo);
				panel.add(boton);
			}
		}
		return panel;
	}

	private void seleccionarCartas(String carta, JButton boton, Color colorPalo) {
		if (cartasSeleccionadas.remove(carta)) {
			boton.setBackground(colorPalo);
		} else if (cartasSeleccionadas.size() < 5) {
			cartasSeleccionadas.add(carta);
			boton.setBackground(new Color(180, 120, 160));
		}
	}

	private void ejecutarAnalisis() {
		if (cartasSeleccionadas.size() < 3) {
			JOptionPane.showMessageDialog(this, "Seleccione al menos 3 cartas para el board");
			return;
		}

		Set<String> rango = new HashSet<>(seleccionesGuardadas);
		analizadorRangos = controlador.inicializarAnalizadorRangos(rango, cartasSeleccionadas);

		Map<String, Double> probabilidades = analizadorRangos.getProbabilidadesMano();
		Map<String, Integer> conteoCombos = analizadorRangos.getCombosPorTipoMano();

		actualizarPanelResultados(probabilidades, conteoCombos);
	}

	private void actualizarPanelResultados(Map<String, Double> probabilidades, Map<String, Integer> conteoCombos) {
		panelResultados.removeAll();
		panelResultados.setLayout(new BorderLayout());

		JLabel etiquetaTotalCombos = new JLabel("Numero total de combos: " + analizadorRangos.getCombosTotales());
		etiquetaTotalCombos.setFont(new Font("Arial", Font.PLAIN, 18));
		JPanel panelEncabezado = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelEncabezado.add(etiquetaTotalCombos);
		panelResultados.add(panelEncabezado, BorderLayout.NORTH);

		JPanel panelListaManos = new JPanel();
		panelListaManos.setLayout(new BoxLayout(panelListaManos, BoxLayout.Y_AXIS));
		panelListaManos.setBorder(new EmptyBorder(20, 20, 20, 20));

		int anchoMaximoEtiqueta = 0;
		for (String nombreMano : probabilidades.keySet()) {
			JLabel etiquetaTemporal = new JLabel(nombreMano);
			anchoMaximoEtiqueta = Math.max(anchoMaximoEtiqueta, etiquetaTemporal.getPreferredSize().width);
		}

		final int anchoMaximoFinal = anchoMaximoEtiqueta;

		for (Map.Entry<String, Double> entrada : probabilidades.entrySet()) {
			String nombreMano = entrada.getKey();
			Double probabilidad = entrada.getValue();
			Integer conteo = conteoCombos.get(nombreMano);

			JPanel panelMano = new JPanel();
			panelMano.setLayout(new BoxLayout(panelMano, BoxLayout.X_AXIS));

			JLabel etiquetaMano = new JLabel(nombreMano);
			etiquetaMano.setPreferredSize(new Dimension(anchoMaximoFinal + 10, etiquetaMano.getPreferredSize().height));
			panelMano.add(etiquetaMano);

			panelMano.add(Box.createHorizontalStrut(10));

			JProgressBar barraProgreso = new JProgressBar(0, 100);
			barraProgreso.setValue((int) Math.round(probabilidad));
			barraProgreso.setStringPainted(true);
			barraProgreso.setString(String.format("%.1f%%", probabilidad));
			barraProgreso.setPreferredSize(new Dimension(400, barraProgreso.getPreferredSize().height));
			panelMano.add(barraProgreso);

			panelMano.add(Box.createHorizontalStrut(10));

			JLabel etiquetaNumeroManos = new JLabel(String.valueOf(conteo != null ? conteo : 0));
			etiquetaNumeroManos.setPreferredSize(new Dimension(50, etiquetaNumeroManos.getPreferredSize().height));
			etiquetaNumeroManos.setFont(new Font("Arial", Font.PLAIN, 16));
			panelMano.add(etiquetaNumeroManos);

			panelMano.setAlignmentX(Component.LEFT_ALIGNMENT);
			panelListaManos.add(panelMano);
			panelListaManos.add(Box.createVerticalStrut(5));
		}

		JScrollPane scrollPane = new JScrollPane(panelListaManos);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		panelResultados.add(scrollPane, BorderLayout.CENTER);

		panelResultados.revalidate();
		panelResultados.repaint();
	}
}
