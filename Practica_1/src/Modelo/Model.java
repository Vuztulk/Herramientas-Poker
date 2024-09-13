package Modelo;

import java.util.*;

public class Model {

	// Función para convertir una mano de String a objetos Carta
	public List<Carta> parsearCartas(String mano) {
		List<Carta> cartas = new ArrayList<>();
		for (int i = 0; i < mano.length(); i += 2) {
			cartas.add(new Carta(mano.charAt(i), mano.charAt(i + 1)));
		}
		return cartas;
	}

	// Función para evaluar la mejor mano de poker
	public String evaluarMejorMano(List<Carta> cartas) {
		int[] valores = new int[15]; // Array para contar la aparición de valores de 2 a 14
		List<Carta> mejoresCartas = new ArrayList<>();

		// Llenar el array con la cantidad de cada valor
		for (Carta carta : cartas) {
			valores[carta.getValor()]++;
		}

		// Verificar escalera
		for (int i = 14; i >= 5; i--) {
			if (esEscalera(valores, i)) {
				mejoresCartas = obtenerCartasPorValor(cartas, i, 5);
				return "Escalera de " + getNombreValor(i) + " a " + getNombreValor(i - 4);
			}
		}

		// Verificar póker
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 4) {
				mejoresCartas = obtenerCartasPorValor(cartas, i, 4);
				return "Póker de " + getNombreValor(i) + " con: " + mejoresCartas;
			}
		}

		// Verificar full
		if (esFull(valores)) {
			int trio = obtenerValorConRepeticiones(valores, 3);
			int pareja = obtenerValorConRepeticiones(valores, 2);
			mejoresCartas = obtenerCartasPorValor(cartas, trio, 3);
			mejoresCartas.addAll(obtenerCartasPorValor(cartas, pareja, 2));
			return "Full de " + getNombreValor(trio) + " con " + getNombreValor(pareja) + ": " + mejoresCartas;
		}

		// Verificar trío
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 3) {
				mejoresCartas = obtenerCartasPorValor(cartas, i, 3);
				return "Trío de " + getNombreValor(i) + " con: " + mejoresCartas;
			}
		}

		// Verificar pareja
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 2) {
				mejoresCartas = obtenerCartasPorValor(cartas, i, 2);
				return "Pareja de " + getNombreValor(i) + " con: " + mejoresCartas;
			}
		}

		// Si no hay combinaciones, retornar la carta más alta
		mejoresCartas = obtenerCartasPorValor(cartas, cartas.get(0).getValor(), 1);
		return "Carta alta: " + getNombreValor(cartas.get(0).getValor()) + " con: " + mejoresCartas;
	}

	// Función para verificar si hay un full (trío + pareja)
	private boolean esFull(int[] valores) {
		boolean hayTrio = false;
		boolean hayPareja = false;

		// Verifica si hay un trío
		for (int valor : valores) {
			if (valor == 3) {
				hayTrio = true;
				break;
			}
		}

		// Verifica si hay una pareja
		for (int valor : valores) {
			if (valor == 2) {
				hayPareja = true;
				break;
			}
		}

		// Hay full si existe al menos un trío y una pareja
		return hayTrio && hayPareja;
	}

	// Función auxiliar para verificar si hay escalera
	private boolean esEscalera(int[] valores, int maxValor) {
		for (int i = 0; i < 5; i++) {
			if (valores[maxValor - i] == 0)
				return false;
		}
		return true;
	}

	// Funciones auxiliares para obtener cartas por valor
	private List<Carta> obtenerCartasPorValor(List<Carta> cartas, int valor, int cantidad) {
		List<Carta> cartasSeleccionadas = new ArrayList<>();
		for (Carta carta : cartas) {
			if (carta.getValor() == valor && cartasSeleccionadas.size() < cantidad) {
				cartasSeleccionadas.add(carta);
			}
		}
		return cartasSeleccionadas;
	}

	private int obtenerValorConRepeticiones(int[] valores, int repeticiones) {
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == repeticiones)
				return i;
		}
		return -1; // No debería pasar
	}

	// Función para obtener el nombre de la carta (valor)
	private String getNombreValor(int valor) {
		return switch (valor) {
		case 14 -> "As";
		case 13 -> "K";
		case 12 -> "Q";
		case 11 -> "J";
		default -> String.valueOf(valor);
		};
	}

	// Detectar draws
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

		for (int i = 0; i < valores.size() - 3; i++) {
			int diferencia = valores.get(i + 3) - valores.get(i);

			// Verificar que la secuencia no esté en los extremos
			if (diferencia == 3 && valores.get(i) > 2 && valores.get(i + 3) < 14) {
				return true;
			}
		}
		return false;
	}

}
