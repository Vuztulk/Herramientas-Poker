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
	public String evaluarMejorMano(String mano) {
		List<Carta> cartas = UtilidadesCarta.parsearCartas(mano);
		Mano hand = new Mano(cartas);
		return hand.obtenerCartasComoString();
	}
	
	public String getDescripcionMano(String mano) {
		List<Carta> cartas = UtilidadesCarta.parsearCartas(mano);
		Mano hand = new Mano(cartas);
		return hand.getDescripcionMano();
	}
	
	public List<String> detectarDraws(String mano) {
		List<Carta> cartas = UtilidadesCarta.parsearCartas(mano);
		return draws.detectarDraws(cartas);
	}

	/////////////////////////////////////////////////////////////////// APARTADO
	/////////////////////////////////////////////////////////////////// 2//////////////////////////////////////////////////////////////////////////
	public String evaluarMejorManoConComunes(String cartasPropias, String cartasComunes) {
		
		List<Carta> cartasJugador = UtilidadesCarta.parsearCartas(cartasPropias);
		List<Carta> cartasComunesLista = UtilidadesCarta.parsearCartas(cartasComunes);

		String mejorManoCartas = null;
		Mano mejorMano = null;

		for (int i = 3; i <= 5; i++) {
			List<List<Carta>> combinacionesComunes = UtilidadesMano.generarCombinaciones(cartasComunesLista, i);

			for (List<Carta> combinacionComunes : combinacionesComunes) {
				List<Carta> combinacionTotal = new ArrayList<>(cartasJugador);
				combinacionTotal.addAll(combinacionComunes);

				Mano manoActual = new Mano(combinacionTotal);

				if (mejorMano == null || manoActual.evaluarValorMano() > mejorMano.evaluarValorMano()) {
					mejorMano = manoActual;
					mejorManoCartas = manoActual.obtenerCartasComoString();
				}
			}
		}

		return mejorManoCartas;
	}

	/////////////////////////////////////////////////////////////////// APARTADO
	/////////////////////////////////////////////////////////////////// 3//////////////////////////////////////////////////////////////////////////
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
	/////////////////////////////////////////////////////////////////// APARTADO
	/////////////////////////////////////////////////////////////////// 4//////////////////////////////////////////////////////////////////////////

	public String evaluarMejorManoOmaha(String cartasPropias, String cartasComunes) {
		
		Mano mejorMano = null;

		List<List<Carta>> combinacionesPropias = UtilidadesMano.generarCombinaciones(UtilidadesCarta.parsearCartas(cartasPropias), 2);
		List<List<Carta>> combinacionesComunes = UtilidadesMano.generarCombinaciones(UtilidadesCarta.parsearCartas(cartasComunes), 3);

		for (List<Carta> combinacionPropia : combinacionesPropias) {
			for (List<Carta> combinacionComun : combinacionesComunes) {

				List<Carta> combinacionTotal = new ArrayList<>(combinacionPropia);
				combinacionTotal.addAll(combinacionComun);

				Mano manoActual = new Mano(combinacionTotal);

				if (mejorMano == null || manoActual.evaluarValorMano() > mejorMano.evaluarValorMano()) {
					mejorMano = manoActual;
				}
			}
		}

		return mejorMano.obtenerCartasComoString();
	}

}
