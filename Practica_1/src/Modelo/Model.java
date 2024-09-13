package Modelo;

import java.util.*;

public class Model {

	public List<Carta> parsearCartas(String mano) {
		List<Carta> cartas = new ArrayList<>();
		for (int i = 0; i < mano.length(); i += 2) {
			cartas.add(new Carta(mano.charAt(i), mano.charAt(i + 1)));
		}
		return cartas;
	}

	public String evaluarMejorMano(List<Carta> cartas) {
		int[] valores = new int[15];
		List<Carta> mejoresCartas = new ArrayList<>();
		Map<Character, Long> conteoPalos = new HashMap<>();

		for (Carta carta : cartas) {
			valores[carta.getValor()]++;
			conteoPalos.put(carta.getPalo(), conteoPalos.getOrDefault(carta.getPalo(), 0L) + 1);
		}

		// Escalera de color
		for (Character palo : conteoPalos.keySet()) {
			if (conteoPalos.get(palo) >= 5) {
				List<Carta> cartasDelMismoPalo = filtrarCartasPorPalo(cartas, palo);
				int[] valoresDelPalo = new int[15];
				for (Carta carta : cartasDelMismoPalo) {
					valoresDelPalo[carta.getValor()]++;
				}
				for (int i = 14; i >= 5; i--) {
					if (esEscalera(valoresDelPalo, i)) {
						mejoresCartas = obtenerCartasPorValor(cartasDelMismoPalo, i, 5);
						return "Escalera de color de " + UtilidadesCarta.getNombreValor(i - 4) + " a " + UtilidadesCarta.getNombreValor(i);
					}
				}
			}
		}

		// Color
		for (Character palo : conteoPalos.keySet()) {
			if (conteoPalos.get(palo) >= 5) {
				mejoresCartas = filtrarCartasPorPalo(cartas, palo);
				mejoresCartas.sort(Comparator.comparingInt(Carta::getValor).reversed());
				return "Color " + mejoresCartas.subList(0, 5);
			}
		}
		// Escalera
		for (int i = 14; i >= 5; i--) {
			if (esEscalera(valores, i)) {
				mejoresCartas = obtenerCartasPorValor(cartas, i, 5);
				return "Escalera de " + UtilidadesCarta.getNombreValor(i - 4) + " a " + UtilidadesCarta.getNombreValor(i);
			}
		}

		// Poker
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 4) {
				mejoresCartas = obtenerCartasPorValor(cartas, i, 4);
				return "Póker de " + UtilidadesCarta.getNombreValor(i) + " " + mejoresCartas;
			}
		}

		// Full
		if (esFull(valores)) {
			int trio = obtenerValorConRepeticiones(valores, 3);
			int pareja = obtenerValorConRepeticiones(valores, 2);
			mejoresCartas = obtenerCartasPorValor(cartas, trio, 3);
			mejoresCartas.addAll(obtenerCartasPorValor(cartas, pareja, 2));
			return "Full de " + UtilidadesCarta.getNombreValor(trio) + " " + UtilidadesCarta.getNombreValor(pareja) + ": " + mejoresCartas;
		}

		// Trio
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 3) {
				mejoresCartas = obtenerCartasPorValor(cartas, i, 3);
				return "Trío de " + UtilidadesCarta.getNombreValor(i) + " " + mejoresCartas;
			}
		}

		// Pareja
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 2) {
				mejoresCartas = obtenerCartasPorValor(cartas, i, 2);
				return "Pareja de " + UtilidadesCarta.getNombreValor(i) + " " + mejoresCartas;
			}
		}

		// Carta mas alta
		return "Carta alta: " + (UtilidadesCarta.obtenerValorMaximo(cartas));
	}

	// Full
	private boolean esFull(int[] valores) {
		boolean hayTrio = false;
		boolean hayPareja = false;

		for (int valor : valores) {
			if (valor == 3) {
				hayTrio = true;
				break;
			}
		}

		for (int valor : valores) {
			if (valor == 2) {
				hayPareja = true;
				break;
			}
		}

		return hayTrio && hayPareja;
	}

	private boolean esEscalera(int[] valores, int maxValor) {
		for (int i = 0; i < 5; i++) {
			if (valores[maxValor - i] == 0)
				return false;
		}
		return true;
	}

	private List<Carta> obtenerCartasPorValor(List<Carta> cartas, int valor, int cantidad) {
		List<Carta> cartasSeleccionadas = new ArrayList<>();
		for (Carta carta : cartas) {
			if (carta.getValor() == valor && cartasSeleccionadas.size() < cantidad) {
				cartasSeleccionadas.add(carta);
			}
		}
		return cartasSeleccionadas;
	}

	private List<Carta> filtrarCartasPorPalo(List<Carta> cartas, char palo) {
		List<Carta> filtradas = new ArrayList<>();
		for (Carta carta : cartas) {
			if (carta.getPalo() == palo) {
				filtradas.add(carta);
			}
		}
		return filtradas;
	}

	private int obtenerValorConRepeticiones(int[] valores, int repeticiones) {
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == repeticiones)
				return i;
		}
		return -1;
	}


	public List<String> detectarDraws(List<Carta> cartas) {

		List<String> draws = new ArrayList<>();
		if (tieneFlushDraw(cartas))
			draws.add("Flush Draw");
		if (tieneGutshot(cartas))
			draws.add("Straight Gutshot");
		if (tieneOpenEnded(cartas))
			draws.add("Straight Open-Ended");

		return draws;
	}

	private boolean tieneFlushDraw(List<Carta> cartas) {
		Map<Character, Long> paloConteo = new HashMap<>();
		for (Carta carta : cartas) {
			paloConteo.put(carta.getPalo(), paloConteo.getOrDefault(carta.getPalo(), 0L) + 1);
		}
		return paloConteo.containsValue(4L);
	}

	private boolean tieneGutshot(List<Carta> cartas) {
		List<Integer> valores = new ArrayList<>();
		for (Carta carta : cartas) {
			valores.add(carta.getValor());
		}
		valores.sort(Comparator.naturalOrder());

		for (int i = 0; i < valores.size() - 4; i++) {
			if (valores.get(i + 4) - valores.get(i) == 4 && valores.get(i + 3) - valores.get(i + 1) != 2) {
				return true;
			}
		}
		return false;
	}

	private boolean tieneOpenEnded(List<Carta> cartas) {
		List<Integer> valores = new ArrayList<>();
		for (Carta carta : cartas) {
			valores.add(carta.getValor());
		}
		valores.sort(Comparator.naturalOrder());

		int[] valoresArray = new int[15];
		for (int valor : valores) {
			valoresArray[valor]++;
		}

		if (esEscalera(valoresArray, valores.get(valores.size() - 1))) {
			return false;
		}

		for (int i = 0; i < valores.size() - 3; i++) {
			int diferencia = valores.get(i + 3) - valores.get(i);

			if (diferencia == 3 && valores.get(i) > 2 && valores.get(i + 3) < 14) { // Verificar que no este en los extremos
				return true;
			}
		}
		return false;
	}

}
