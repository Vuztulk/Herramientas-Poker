package Modelo;

import java.util.*;

public class UtilidadesMano {

	public static TipoMano evaluarTipoMano(List<Carta> cartas) {
		
		int[] valores = new int[15];
		Map<Character, Integer> conteoPalos = new HashMap<>();
		
		for (Carta carta : cartas) {
			valores[carta.getValor()]++;
			conteoPalos.put(carta.getPalo(), conteoPalos.getOrDefault(carta.getPalo(), 0) + 1);
		}

		for (Character palo : conteoPalos.keySet()) {
			if (conteoPalos.get(palo) >= 5) {
				List<Carta> cartasDelMismoPalo = filtrarCartasPorPalo(cartas, palo);
				int[] valoresDelPalo = new int[15];
				for (Carta carta : cartasDelMismoPalo) {
					valoresDelPalo[carta.getValor()]++;
				}
				for (int i = 14; i >= 5; i--) {
					if (esEscalera(valoresDelPalo, i)) {
						return TipoMano.ESCALERA_COLOR;
					}
				}
			}
		}

		for (Character palo : conteoPalos.keySet()) {
			if (conteoPalos.get(palo) >= 5) {
				return TipoMano.COLOR;
			}
		}

		for (int i = 14; i >= 5; i--) {
			if (esEscalera(valores, i)) {
				return TipoMano.ESCALERA;
			}
		}

		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 4) {
				return TipoMano.POKER;
			}
		}

		if (esFull(valores)) {
			return TipoMano.FULL;
		}

		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 3) {
				return TipoMano.TRIO;
			}
		}

		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 2) {
				return TipoMano.PAREJA;
			}
		}

		return TipoMano.CARTA_MAS_ALTA;
	}

	public static String obtenerDescripcionMano(List<Carta> cartas) {
		int[] valores = new int[15];
		Map<Character, Integer> conteoPalos = new HashMap<>();
		int max = UtilidadesCarta.obtenerValorMaximo(cartas);
		int posMax = -1;
		int j = 0;

		for (Carta carta : cartas) {
			valores[carta.getValor()]++;
			conteoPalos.put(carta.getPalo(), conteoPalos.getOrDefault(carta.getPalo(), 0) + 1);

			if (cartas.get(j).getValor() == max) {
				posMax = j;
			}
			j++;
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
						return "Escalera de color [" + UtilidadesCarta.getNombreValor(i - 4) + " - "+ UtilidadesCarta.getNombreValor(i) + "]";
					}
				}
			}
		}

		// Color
		for (Character palo : conteoPalos.keySet()) {
			if (conteoPalos.get(palo) >= 5) {
				List<Carta> cartasDelMismoPalo = filtrarCartasPorPalo(cartas, palo);
				cartasDelMismoPalo.sort(Comparator.comparingInt(Carta::getValor).reversed());
				return "Color " + cartasDelMismoPalo.subList(0, 5);
			}
		}

		// Escalera
		for (int i = 14; i >= 5; i--) {
			if (esEscalera(valores, i)) {
				return "Escalera [" + UtilidadesCarta.getNombreValor(i - 4) + " - " + UtilidadesCarta.getNombreValor(i)+ "]";
			}
		}

		// Poker
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 4) {
				List<Carta> cartasPorValor = obtenerCartasPorValor(cartas, i, 4);
				return "Poker de " + UtilidadesCarta.getNombreValor(i) + " " + cartasPorValor;
			}
		}

		// Full
		if (esFull(valores)) {
			int trio = obtenerValorConRepeticiones(valores, 3);
			int pareja = obtenerValorConRepeticiones(valores, 2);
			List<Carta> cartasTrio = obtenerCartasPorValor(cartas, trio, 3);
			List<Carta> cartasPareja = obtenerCartasPorValor(cartas, pareja, 2);
			return "Full de " + UtilidadesCarta.getNombreValor(trio) + " y " + UtilidadesCarta.getNombreValor(pareja)+ ": " + cartasTrio + " " + cartasPareja;
		}

		// Trio
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 3) {
				List<Carta> cartasPorValor = obtenerCartasPorValor(cartas, i, 3);
				return "Trio de " + UtilidadesCarta.getNombreValor(i) + " " + cartasPorValor;
			}
		}

		// Pareja
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 2) {
				List<Carta> cartasPorValor = obtenerCartasPorValor(cartas, i, 2);
				return "Pareja de " + UtilidadesCarta.getNombreValor(i) + " " + cartasPorValor;
			}
		}

		// Carta más alta
		return "Carta más alta [" + UtilidadesCarta.getNombreValor(max) + cartas.get(posMax).getPalo() + "]";
	}

	public static boolean esEscalera(int[] valores, int maxValor) {
		for (int i = 0; i < 5; i++) {
			if (valores[maxValor - i] == 0)
				return false;
		}
		return true;
	}

	public static List<Carta> filtrarCartasPorPalo(List<Carta> cartas, char palo) {
		List<Carta> cartasFiltradas = new ArrayList<>();
		for (Carta carta : cartas) {
			if (carta.getPalo() == palo) {
				cartasFiltradas.add(carta);
			}
		}
		return cartasFiltradas;
	}

	public static boolean esFull(int[] valores) {
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

	public static List<Carta> obtenerCartasPorValor(List<Carta> cartas, int valor, int cantidad) {
		List<Carta> cartasSeleccionadas = new ArrayList<>();
		for (Carta carta : cartas) {
			if (carta.getValor() == valor && cartasSeleccionadas.size() < cantidad) {
				cartasSeleccionadas.add(carta);
			}
		}
		return cartasSeleccionadas;
	}

	public static int obtenerValorConRepeticiones(int[] valores, int repeticiones) {
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == repeticiones)
				return i;
		}
		return -1;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//////https://www.wextensible.com/temas/combinatoria/combinaciones-repeticion.html///////
/////////////////////////////////////////////////////////////////////////////////////////
	public static List<List<Carta>> generarCombinaciones(List<Carta> cartas, int n) {
		List<List<Carta>> combinaciones = new ArrayList<>();
		generarCombinacionesRecursivo(cartas, n, 0, new ArrayList<>(), combinaciones);
		return combinaciones;
	}

	private static void generarCombinacionesRecursivo(List<Carta> cartas, int n, int indice, List<Carta> actual, List<List<Carta>> combinaciones) {
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
}
