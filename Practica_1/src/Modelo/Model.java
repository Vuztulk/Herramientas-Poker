package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Model {

	private final Draws draws;

	public Model() {
		this.draws = new Draws();
	}

	public List<Carta> parsearCartas(String mano) {
		return UtilidadesCarta.parsearCartas(mano);
	}

	/////////////////////////////////////////////////////////////////// APARTADO
	/////////////////////////////////////////////////////////////////// 1//////////////////////////////////////////////////////////////////////////
	public String evaluarMejorMano(List<Carta> cartas) {
		Mano hand = new Mano(cartas);
		return hand.getDescripcionMano();
	}

	public List<String> detectarDraws(List<Carta> cartas) {
		return draws.detectarDraws(cartas);
	}

	/////////////////////////////////////////////////////////////////// APARTADO
	/////////////////////////////////////////////////////////////////// 2//////////////////////////////////////////////////////////////////////////
	public List<Carta> evaluarMejorManoConComunes(List<Carta> cartasPropias, List<Carta> cartasComunes) {
		List<Carta> mejorManoCartas = null;
		Mano mejorMano = null;

		for (int i = 3; i <= 5; i++) {
			List<List<Carta>> combinacionesComunes = UtilidadesMano.generarCombinaciones(cartasComunes, i);

			for (List<Carta> combinacionComunes : combinacionesComunes) {

				List<Carta> combinacionTotal = new ArrayList<>(cartasPropias);
				combinacionTotal.addAll(combinacionComunes);

				Mano manoActual = new Mano(combinacionTotal);

				if (mejorMano == null || manoActual.evaluarValorMano() > mejorMano.evaluarValorMano()) {
					mejorMano = manoActual;
					mejorManoCartas = combinacionTotal;
				}
			}
		}

		return mejorManoCartas;
	}

	/////////////////////////////////////////////////////////////////// APARTADO
	/////////////////////////////////////////////////////////////////// 3//////////////////////////////////////////////////////////////////////////
	public List<String> ordenarJugadoresPorMejorMano(List<List<Carta>> cartasJugadores, List<Carta> cartasComunes) {
		
		List<Jugador> jugadores = new ArrayList<>();
		
		for (int i = 0; i < cartasJugadores.size(); i++) {
			List<Carta> cartasJugador = cartasJugadores.get(i);
			Mano manoJugador = new Mano(evaluarMejorManoConComunes(cartasJugador,cartasComunes));
			Jugador jugador = new Jugador("J" + (i + 1), manoJugador);
			jugadores.add(jugador);
			
		}
		
		jugadores.sort((j1, j2) -> Integer.compare(j2.getMano().getValor(), j1.getMano().getValor()));

		List<String> resultados = new ArrayList<>();
		for (Jugador jugador : jugadores) {
			resultados.add(jugador.getIdentificador() + ": "+ jugador.getMano().getDescripcionMano());
		}

		return resultados;
	}
	/////////////////////////////////////////////////////////////////// APARTADO
	/////////////////////////////////////////////////////////////////// 4//////////////////////////////////////////////////////////////////////////
	// Implementar según sea necesario
}
