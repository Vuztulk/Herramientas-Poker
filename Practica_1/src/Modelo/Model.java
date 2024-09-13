package Modelo;

import java.util.ArrayList;
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
	public List<Carta> evaluarMejorManoConComunes(List<Carta> cartasPropias, List<Carta> cartasComunes) {
		
		List<Carta> todasLasCartas = new ArrayList<>(cartasPropias);
		todasLasCartas.addAll(cartasComunes);

		List<List<Carta>> combinaciones = generarCombinaciones(todasLasCartas, cartasComunes.size());

		String mejorMano = "";
		List<Carta> mejorManoCartas = null;

		for (List<Carta> combinacion : combinaciones) {
			String manoActual = hand.evaluarMejorMano(combinacion);
			if (mejorMano.isEmpty() || compararManos(manoActual, mejorMano) > 0) {
				mejorMano = manoActual;
				mejorManoCartas = combinacion;
			}
		}

		return mejorManoCartas;
		
	}

	private List<List<Carta>> generarCombinaciones(List<Carta> cartas, int n) {
		List<List<Carta>> combinaciones = new ArrayList<>();
		generarCombinacionesRecursivo(cartas, n, 0, new ArrayList<>(), combinaciones);
		return combinaciones;
	}

	private void generarCombinacionesRecursivo(List<Carta> cartas, int n, int indice, List<Carta> actual,
			List<List<Carta>> combinaciones) {
		if (actual.size() == n) {
			combinaciones.add(new ArrayList<>(actual));
			return;
		}
		for (int i = indice; i < cartas.size(); i++) {
			actual.add(cartas.get(i));
			generarCombinacionesRecursivo(cartas, n, i + 1, actual, combinaciones);
			actual.remove(actual.size() - 1);
		}
	}

	private int compararManos(String mano1, String mano2) {
		int valorMano1 = evaluarValorMano(mano1);
		int valorMano2 = evaluarValorMano(mano2);

		return Integer.compare(valorMano1, valorMano2);
	}

	private int evaluarValorMano(String mano) {
		
		int indexInicio = mano.indexOf('[');//Solo deja el tipo de mano para evaluarlo

		if (indexInicio != -1) {//Solo deja el tipo de mano para evaluarlo
			mano = mano.substring(0, indexInicio).trim();
		}

		String tipoMano = mano.split(" ")[0];//Solo deja el tipo de mano para evaluarlo

		switch (tipoMano) {
		case "Escalera":
			if (mano.contains("de color")) {
				return 9; // Escalera de color
			}
			return 5; // Escalera
		case "Color":
			return 6; // Color
		case "Poker":
			return 8; // Poker
		case "Full":
			return 7; // Full
		case "Trio":
			return 4; // Trio
		case "Pareja":
			return 2; // Pareja
		case "Carta":
			return 1; // Carta mas alta
		default:
			return 0; // Caso por defecto
		}
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
