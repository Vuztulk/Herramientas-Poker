package Modelo;

import java.util.*;

public class Model {

	// Parsear las cartas desde una cadena de caracteres
	public List<Carta> parsearCartas(String mano) {
		List<Carta> cartas = new ArrayList<>();
		for (int i = 0; i < mano.length(); i += 2) {
			char valor = mano.charAt(i);
			char palo = mano.charAt(i + 1);
			cartas.add(new Carta(valor, palo));
		}
		return cartas;
	}

	public String evaluarMejorMano(List<Carta> cartas) {
		cartas.sort(Comparator.comparingInt(Carta::getValor).reversed());

		int[] valores = new int[15]; // Para contar los valores de las cartas (1-14)
		for (Carta carta : cartas) {
			valores[carta.getValor()]++;
		}

		for (int i = 14; i >= 5; i--) {
			if (valores[i] > 0 && valores[i - 1] > 0 && valores[i - 2] > 0 && valores[i - 3] > 0
					&& valores[i - 4] > 0) {
				return "Escalera de " + getNombreValor(i) + " a " + getNombreValor(i - 4);
			}
		}

		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 4) {
				return "Póker de " + getNombreValor(i) + "s";
			}
		}

		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 3) {
				for (int j = 14; j >= 2; j--) {
					if (valores[j] == 2) {
						return "Full de " + getNombreValor(i) + "s con " + getNombreValor(j) + "s";
					}
				}
				return "Trío de " + getNombreValor(i) + "s";
			}
		}

		int primeraPareja = 0;
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 2) {
				if (primeraPareja == 0) {
					primeraPareja = i;
				} else {
					return "Doble pareja de " + getNombreValor(primeraPareja) + "s y " + getNombreValor(i) + "s";
				}
			}
		}

		if (primeraPareja != 0) {
			return "Pareja de " + getNombreValor(primeraPareja) + "s";
		}

		return "Carta alta: " + getNombreValor(cartas.get(0).getValor());
	}

	// Detectar posibles draws (flush, gutshot, open-ended) para el apartado 1
	public List<String> detectarDraws(List<Carta> cartas) {
		List<String> draws = new ArrayList<>();
		if (tieneFlushDraw(cartas)) {
			draws.add("Flush Draw");
		}
		if (tieneGutshot(cartas)) {
			draws.add("Gutshot");
		}
		if (tieneOpenEnded(cartas)) {
			draws.add("Straight Open-Ended");
		}
		return draws;
	}

	// Evaluar la mejor mano con cartas comunes para el apartado 2
	public String evaluarMejorManoConComunes(List<Carta> cartasPropias, List<Carta> cartasComunes) {
		// Combina las cartas propias con las comunes
		List<Carta> todasCartas = new ArrayList<>(cartasPropias);
		todasCartas.addAll(cartasComunes);

		// Ordena las cartas por valor descendente
		todasCartas.sort(Comparator.comparingInt(Carta::getValor).reversed());

		// Identificar la mejor mano posible combinando las cartas propias y comunes
		return evaluarMejorMano(todasCartas); // Sólo devuelve la mejor mano
	}

	// Detectar draws con cartas comunes para el apartado 2
	public List<String> detectarDrawsConComunes(List<Carta> cartasPropias, List<Carta> cartasComunes) {
		List<Carta> todasCartas = new ArrayList<>(cartasPropias);
		todasCartas.addAll(cartasComunes);
		return detectarDraws(todasCartas); // Reutiliza la función de detectarDraws
	}

	// Ordenar jugadores por mejor mano para el apartado 3
	public List<String> ordenarJugadoresPorMejorMano(List<List<Carta>> cartasJugadores, List<Carta> cartasComunes) {
		List<String> resultados = new ArrayList<>();
		Map<Integer, String> ranking = new HashMap<>();

		for (int i = 0; i < cartasJugadores.size(); i++) {
			List<Carta> todasCartas = new ArrayList<>(cartasJugadores.get(i));
			todasCartas.addAll(cartasComunes);
			String mejorMano = evaluarMejorMano(todasCartas);
			ranking.put(i + 1, mejorMano);
		}

		List<Integer> indices = new ArrayList<>(ranking.keySet());
		for (int i = 0; i < indices.size() - 1; i++) {
			for (int j = i + 1; j < indices.size(); j++) {
				if (ranking.get(indices.get(i)).compareTo(ranking.get(indices.get(j))) > 0) {
					int temp = indices.get(i);
					indices.set(i, indices.get(j));
					indices.set(j, temp);
				}
			}
		}

		for (int index : indices) {
			resultados.add("J" + index + ": " + ranking.get(index));
		}

		return resultados;
	}

	// Verificar si hay un flush draw
	private boolean tieneFlushDraw(List<Carta> cartas) {
		Map<Character, Long> paloConteo = new HashMap<>();
		for (Carta carta : cartas) {
			paloConteo.put(carta.getPalo(), paloConteo.getOrDefault(carta.getPalo(), 0L) + 1);
		}
		return paloConteo.containsValue(4L);
	}

	// Verificar si hay un gutshot
	private boolean tieneGutshot(List<Carta> cartas) {
		List<Integer> valores = new ArrayList<>();
		for (Carta carta : cartas) {
			valores.add(carta.getValor());
		}
		valores.sort(Comparator.reverseOrder());

		// Logica para gutshot (escalera con un hueco)
		return valores.get(0) - valores.get(1) == 2 && valores.get(1) - valores.get(2) == 1
				&& valores.get(2) - valores.get(3) == 1;
	}

	// Verificar si hay un open-ended
	private boolean tieneOpenEnded(List<Carta> cartas) {
		List<Integer> valores = new ArrayList<>();
		for (Carta carta : cartas) {
			valores.add(carta.getValor());
		}
		valores.sort(Comparator.reverseOrder());

		// Lógica para escalera open-ended
		for (int i = 0; i < valores.size() - 1; i++) {
			if (valores.get(i) - valores.get(i + 1) != 1) {
				return false;
			}
		}
		return true;
	}

	// Obtener el nombre del valor de la carta
	private String getNombreValor(int valor) {
		switch (valor) {
		case 14:
			return "A";
		case 13:
			return "K";
		case 12:
			return "Q";
		case 11:
			return "J";
		case 10:
			return "T";
		default:
			return String.valueOf(valor);
		}
	}
}
