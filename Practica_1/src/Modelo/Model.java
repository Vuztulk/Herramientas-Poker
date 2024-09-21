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
	
	public String getCartasMejorMano(String mano) {
		return new Mano(parsearCartas(mano)).obtenerMejorManoComoString();
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

	public String evaluarMejorManoOmaha(String cartasPropias, String cartasComunes) {
	    // Parsear las cartas propias y comunes a listas de objetos Carta
	    List<Carta> cartasJugador = parsearCartas(cartasPropias);
	    List<Carta> cartasComunesLista = parsearCartas(cartasComunes);

	    Mano mejorMano = null;

	    // Generar combinaciones de exactamente 2 cartas propias
	    List<List<Carta>> combinacionesPropias = UtilidadesMano.generarCombinaciones(cartasJugador, 2);

	    // Generar combinaciones de exactamente 3 cartas comunes
	    List<List<Carta>> combinacionesComunes = UtilidadesMano.generarCombinaciones(cartasComunesLista, 3);

	    // Evaluar todas las combinaciones posibles de 2 cartas propias y 3 cartas comunes
	    for (List<Carta> combinacionPropias : combinacionesPropias) {
	        for (List<Carta> combinacionComunes : combinacionesComunes) {
	            // Crear la combinación total (2 cartas propias + 3 cartas comunes)
	            List<Carta> combinacionTotal = new ArrayList<>(combinacionPropias);
	            combinacionTotal.addAll(combinacionComunes);

	            // Crear la mano a partir de la combinación
	            Mano manoActual = new Mano(combinacionTotal);

	            // Evaluar si es la mejor mano hasta ahora
	            if (mejorMano == null || manoActual.evaluarValorMano() > mejorMano.evaluarValorMano()) {
	                mejorMano = manoActual;
	            }
	        }
	    }

	    // Devolver la mejor mano como string
	    return mejorMano != null ? mejorMano.obtenerCartasComoString() : null;
	}

	public List<String> ordJugRaw(List<String> manosJugadores, String cartasComunes) {
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
			resultados.add(jugador.getIdentificador() + ": " + jugador.getMano().obtenerMejorManoComoString());
		}

		return resultados;
	}
	
}
