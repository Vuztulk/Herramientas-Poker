package Modelo;

import java.util.List;

public class Model {

	private final Draws draws;
	private final Mano hand;

	public Model() {
		this.draws = new Draws();
		this.hand = new Mano();
	}

	public List<Carta> parsearCartas(String mano) {
		return UtilidadesCarta.parsearCartas(mano);
	}
/////////////////////////////////////////////////////////////////APARTADO 1//////////////////////////////////////////////////////////////////////////
	public String evaluarMejorMano(List<Carta> cartas) {
		return hand.evaluarMejorMano(cartas);
	}

	public List<String> detectarDraws(List<Carta> cartas) {
		return draws.detectarDraws(cartas);
	}
/////////////////////////////////////////////////////////////////APARTADO 2//////////////////////////////////////////////////////////////////////////
	public String evaluarMejorManoConComunes(List<Carta> cartasPropias, List<Carta> cartasComunes) {

		return null;
	}

	public List<String> detectarDrawsConComunes(List<Carta> cartasPropias, List<Carta> cartasComunes) {

		return null;
	}
/////////////////////////////////////////////////////////////////APARTADO 3//////////////////////////////////////////////////////////////////////////
	public List<String> ordenarJugadoresPorMejorMano(List<List<Carta>> cartasJugadores, List<Carta> cartasComunes) {

		return null;
	}
/////////////////////////////////////////////////////////////////APARTADO 4//////////////////////////////////////////////////////////////////////////

}
