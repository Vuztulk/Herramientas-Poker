package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Model {

	public Model() {

	}

	public List<Carta> parsearCartas(String mano) {
		return UtilidadesCarta.parsearCartas(mano);
	}

	public String evaluarMejorMano(String mano) {
		return new Mano(parsearCartas(mano)).obtenerCartasComoString();
	}

	public String evaluarMejorManoString(String mano) {
		return new Mano(parsearCartas(mano)).obtenerCartasComoString();
	}
	
	public String getDescripcionMano(String mano) {
		return new Mano(parsearCartas(mano)).getDescripcionMano();
	}

	public List<String> detectarDraws(String mano) {
		return new Draws().detectarDraws(parsearCartas(mano));
	}

	public String evaluarMejorManoConComunes(String cartasPropias, String cartasComunes) {
		return evaluarMejorManoGenerica(cartasPropias, cartasComunes, 3, 5);
	}
	
	public String evaluarMejorManoOmaha(String cartasPropias, String cartasComunes) {
		return evaluarMejorManoGenerica(cartasPropias, cartasComunes, 2, 3);
	}
	
	public List<String> ordenarJugadoresPorMejorMano(List<String> manosJugadores, String cartasComunes) {
		List<Jugador> jugadores = new ArrayList<>();

		for (int i = 0; i < manosJugadores.size(); i++) {
			String mejorManoCartas = evaluarMejorManoConComunes(manosJugadores.get(i), cartasComunes);
			Mano manoJugador = new Mano(UtilidadesCarta.parsearCartas(mejorManoCartas));

			Jugador jugador = new Jugador("J" + (i + 1), manoJugador);
			jugadores.add(jugador);
		}

		jugadores.sort((j1, j2) -> Integer.compare(j2.getMano().getValor(), j1.getMano().getValor()));

		List<String> resultados = new ArrayList<>();
		for (Jugador jugador : jugadores) {
			resultados.add(jugador.getIdentificador() + ": " + jugador.getMano().getDescripcionMano());
		}

		return resultados;
	}

	private String evaluarMejorManoGenerica(String cartasPropias, String cartasComunes, int minCombinacionPropia, int maxCombinacionComunes) {
		List<Carta> cartasJugador = parsearCartas(cartasPropias);
		List<Carta> cartasComunesLista = parsearCartas(cartasComunes);

		Mano mejorMano = null;

		for (int i = minCombinacionPropia; i <= maxCombinacionComunes; i++) {
			List<List<Carta>> combinacionesComunes = UtilidadesMano.generarCombinaciones(cartasComunesLista, i);

			for (List<Carta> combinacionComunes : combinacionesComunes) {
				List<Carta> combinacionTotal = new ArrayList<>(cartasJugador);
				combinacionTotal.addAll(combinacionComunes);

				Mano manoActual = new Mano(combinacionTotal);

				if (mejorMano == null || manoActual.evaluarValorMano() > mejorMano.evaluarValorMano()) {
					mejorMano = manoActual;
				}
			}
		}

		return mejorMano != null ? mejorMano.obtenerCartasComoString() : null;
	}

}
