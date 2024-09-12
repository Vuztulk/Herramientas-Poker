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

		int[] valores = new int[15];
		for (Carta carta : cartas) {
			valores[carta.getValor()]++;
		}

		for (int i = 14; i >= 5; i--) {
			if (valores[i] > 0 && valores[i - 1] > 0 && valores[i - 2] > 0 && valores[i - 3] > 0
					&& valores[i - 4] > 0) {
				List<Carta> escalera = new ArrayList<>();
				for (int j = 0; j < 5; j++) {
					for (Carta carta : cartas) {
						if (carta.getValor() == i - j) {
							escalera.add(carta);
							break;
						}
					}
				}
				return "Escalera de " + getNombreValor(i) + " a " + getNombreValor(i - 4) + ": " + escalera;
			}
		}

		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 4) {
				List<Carta> poker = new ArrayList<>();
				for (Carta carta : cartas) {
					if (carta.getValor() == i) {
						poker.add(carta);
					}
				}
				return "Póker de " + getNombreValor(i) + "s: " + poker;
			}
		}

		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 3) {
				for (int j = 14; j >= 2; j--) {
					if (valores[j] == 2) {
						List<Carta> full = new ArrayList<>();
						for (Carta carta : cartas) {
							if (carta.getValor() == i || carta.getValor() == j) {
								full.add(carta);
							}
						}
						return "Full de " + getNombreValor(i) + "s con " + getNombreValor(j) + "s: " + full;
					}
				}
				List<Carta> trio = new ArrayList<>();
				for (Carta carta : cartas) {
					if (carta.getValor() == i) {
						trio.add(carta);
					}
				}
				return "Trío de " + getNombreValor(i) + "s: " + trio;
			}
		}

		int primeraPareja = 0;
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 2) {
				if (primeraPareja == 0) {
					primeraPareja = i;
				} else {
					List<Carta> doblePareja = new ArrayList<>();
					for (Carta carta : cartas) {
						if (carta.getValor() == primeraPareja || carta.getValor() == i) {
							doblePareja.add(carta);
						}
					}
					return "Doble pareja de " + getNombreValor(primeraPareja) + "s y " + getNombreValor(i) + "s: "
							+ doblePareja;
				}
			}
		}

		if (primeraPareja != 0) {
			List<Carta> pareja = new ArrayList<>();
			for (Carta carta : cartas) {
				if (carta.getValor() == primeraPareja) {
					pareja.add(carta);
				}
			}
			return "Pareja de " + getNombreValor(primeraPareja) + "s: " + pareja;
		}

		return "Carta alta: " + getNombreValor(cartas.get(0).getValor()) + ": " + cartas.get(0);
	}

	public List<String> detectarDraws(List<Carta> cartas) {
		List<String> draws = new ArrayList<>();
		if (tieneFlushDraw(cartas)) {
			draws.add("Flush");
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
			int gap = valores.get(i + 4) - valores.get(i);
			if (gap == 4) {
				if (valores.get(i + 1) - valores.get(i) != 1 || valores.get(i + 2) - valores.get(i + 1) != 1
						|| valores.get(i + 3) - valores.get(i + 2) != 1
						|| valores.get(i + 4) - valores.get(i + 3) != 1) {
					return true;
				}
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

		// Verificar todas las combinaciones posibles para un open-ended straight draw
		for (int i = 0; i < valores.size() - 3; i++) {
			if (valores.get(i + 3) - valores.get(i) == 3 && valores.get(i + 1) - valores.get(i) == 1
					&& valores.get(i + 2) - valores.get(i + 1) == 1) {
				return true;
			}
		}
		return false;
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
