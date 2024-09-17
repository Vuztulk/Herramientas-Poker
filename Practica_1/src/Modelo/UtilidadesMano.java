package Modelo;

import java.util.*;

public class UtilidadesMano {

    public static TipoMano evaluarTipoMano(List<Carta> cartas) {
        int[] valores = contarValores(cartas);
        Map<Character, Integer> conteoPalos = contarPalos(cartas);

        if (esEscaleraColor(cartas, conteoPalos)) {
            return TipoMano.ESCALERA_COLOR;
        }

        if (esPoker(valores)) {
            return TipoMano.POKER;
        }

        if (esFull(valores)) {
            return TipoMano.FULL;
        }

        if (esColor(conteoPalos)) {
            return TipoMano.COLOR;
        }

        if (esEscalera(valores)) {
            return TipoMano.ESCALERA;
        }

        if (esTrio(valores)) {
            return TipoMano.TRIO;
        }

        if (esPareja(valores)) {
            return TipoMano.PAREJA;
        }

        return TipoMano.CARTA_MAS_ALTA;
    }

    public static String obtenerDescripcionMano(List<Carta> cartas) {
        int[] valores = contarValores(cartas);
        Map<Character, Integer> conteoPalos = contarPalos(cartas);
        int max = UtilidadesCarta.obtenerValorMaximo(cartas);
        int posMax = obtenerPosicionCartaMasAlta(cartas, max);

        if (esEscaleraColor(cartas, conteoPalos)) {
            return obtenerDescripcionEscaleraColor(cartas, conteoPalos);
        }

        if (esColor(conteoPalos)) {
            return obtenerDescripcionColor(cartas, conteoPalos);
        }

        if (esEscalera(valores)) {
            return obtenerDescripcionEscalera(valores);
        }

        if (esPoker(valores)) {
            return obtenerDescripcionPoker(cartas, valores);
        }

        if (esFull(valores)) {
            return obtenerDescripcionFull(cartas, valores);
        }

        if (esTrio(valores)) {
            return obtenerDescripcionTrio(cartas, valores);
        }

        if (esPareja(valores)) {
            return obtenerDescripcionPareja(cartas, valores);
        }

        return "Carta más alta [" + UtilidadesCarta.getNombreValor(max) + cartas.get(posMax).getPalo() + "]";
    }
    
    private static boolean esEscaleraColor(List<Carta> cartas, Map<Character, Integer> conteoPalos) {
        for (Character palo : conteoPalos.keySet()) {
            if (conteoPalos.get(palo) >= 5) {
                List<Carta> cartasDelMismoPalo = filtrarCartasPorPalo(cartas, palo);
                int[] valoresDelPalo = contarValores(cartasDelMismoPalo);
                if (tieneEscalera(valoresDelPalo)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean esPoker(int[] valores) {
        return contieneNDeUnValor(valores, 4);
    }

    private static boolean esFull(int[] valores) {
        boolean hayTrio = contieneNDeUnValor(valores, 3);
        boolean hayPareja = contieneNDeUnValor(valores, 2);
        return hayTrio && hayPareja;
    }

    private static boolean esColor(Map<Character, Integer> conteoPalos) {
        for (Character palo : conteoPalos.keySet()) {
            if (conteoPalos.get(palo) >= 5) {
                return true;
            }
        }
        return false;
    }

    private static boolean esEscalera(int[] valores) {
        return tieneEscalera(valores);
    }

    private static boolean esTrio(int[] valores) {
        return contieneNDeUnValor(valores, 3);
    }

    private static boolean esPareja(int[] valores) {
        return contieneNDeUnValor(valores, 2);
    }

    private static int[] contarValores(List<Carta> cartas) {
        int[] valores = new int[15];
        for (Carta carta : cartas) {
            valores[carta.getValor()]++;
        }
        return valores;
    }

    private static Map<Character, Integer> contarPalos(List<Carta> cartas) {
        Map<Character, Integer> conteoPalos = new HashMap<>();
        for (Carta carta : cartas) {
            char palo = carta.getPalo();
            conteoPalos.put(palo, conteoPalos.getOrDefault(palo, 0) + 1);
        }
        return conteoPalos;
    }

    private static boolean tieneEscalera(int[] valores) {
        for (int i = 14; i >= 5; i--) {
            boolean escalera = true;
            for (int j = 0; j < 5; j++) {
                if (valores[i - j] == 0) {
                    escalera = false;
                    break;
                }
            }
            if (escalera) return true;
        }
        return false;
    }

    private static List<Carta> filtrarCartasPorPalo(List<Carta> cartas, char palo) {
        List<Carta> cartasFiltradas = new ArrayList<>();
        for (Carta carta : cartas) {
            if (carta.getPalo() == palo) {
                cartasFiltradas.add(carta);
            }
        }
        return cartasFiltradas;
    }

    private static boolean contieneNDeUnValor(int[] valores, int n) {
        for (int valor : valores) {
            if (valor == n) return true;
        }
        return false;
    }


    private static int obtenerPosicionCartaMasAlta(List<Carta> cartas, int max) {
        int posMax = -1;
        for (int j = 0; j < cartas.size(); j++) {
            if (cartas.get(j).getValor() == max) {
                posMax = j;
            }
        }
        return posMax;
    }

    private static String obtenerDescripcionEscaleraColor(List<Carta> cartas, Map<Character, Integer> conteoPalos) {
        for (Character palo : conteoPalos.keySet()) {
            if (conteoPalos.get(palo) >= 5) {
                List<Carta> cartasDelMismoPalo = filtrarCartasPorPalo(cartas, palo);
                int[] valoresDelPalo = contarValores(cartasDelMismoPalo);
                for (int i = 14; i >= 5; i--) {
                    if (tieneEscalera(valoresDelPalo, i)) {
                        return "Escalera de color [" + UtilidadesCarta.getNombreValor(i - 4) + " - " + UtilidadesCarta.getNombreValor(i) + "]";
                    }
                }
            }
        }
        return "";
    }

    private static String obtenerDescripcionColor(List<Carta> cartas, Map<Character, Integer> conteoPalos) {
        for (Character palo : conteoPalos.keySet()) {
            if (conteoPalos.get(palo) >= 5) {
                List<Carta> cartasDelMismoPalo = filtrarCartasPorPalo(cartas, palo);
                cartasDelMismoPalo.sort(Comparator.comparingInt(Carta::getValor).reversed());
                return "Color " + cartasDelMismoPalo.subList(0, 5);
            }
        }
        return "";
    }

    private static String obtenerDescripcionEscalera(int[] valores) {
        for (int i = 14; i >= 5; i--) {
            if (tieneEscalera(valores, i)) {
                return "Escalera [" + UtilidadesCarta.getNombreValor(i - 4) + " - " + UtilidadesCarta.getNombreValor(i) + "]";
            }
        }
        return "";
    }

    private static String obtenerDescripcionPoker(List<Carta> cartas, int[] valores) {
        for (int i = 14; i >= 2; i--) {
            if (valores[i] == 4) {
                List<Carta> cartasPorValor = obtenerCartasPorValor(cartas, i, 4);
                return "Poker de " + UtilidadesCarta.getNombreValor(i) + " " + cartasPorValor;
            }
        }
        return "";
    }

    private static String obtenerDescripcionFull(List<Carta> cartas, int[] valores) {
        if (esFull(valores)) {
            int trio = obtenerValorConRepeticiones(valores, 3);
            int pareja = obtenerValorConRepeticiones(valores, 2);
            List<Carta> cartasTrio = obtenerCartasPorValor(cartas, trio, 3);
            List<Carta> cartasPareja = obtenerCartasPorValor(cartas, pareja, 2);
            return "Full de " + UtilidadesCarta.getNombreValor(trio) + " y " + UtilidadesCarta.getNombreValor(pareja) + ": " + cartasTrio + " " + cartasPareja;
        }
        return "";
    }

    private static String obtenerDescripcionTrio(List<Carta> cartas, int[] valores) {
        for (int i = 14; i >= 2; i--) {
            if (valores[i] == 3) {
                List<Carta> cartasPorValor = obtenerCartasPorValor(cartas, i, 3);
                return "Trio de " + UtilidadesCarta.getNombreValor(i) + " " + cartasPorValor;
            }
        }
        return "";
    }

    private static String obtenerDescripcionPareja(List<Carta> cartas, int[] valores) {
        for (int i = 14; i >= 2; i--) {
            if (valores[i] == 2) {
                List<Carta> cartasPorValor = obtenerCartasPorValor(cartas, i, 2);
                return "Pareja de " + UtilidadesCarta.getNombreValor(i) + " " + cartasPorValor;
            }
        }
        return "";
    }

	public static boolean tieneEscalera(int[] valores, int maxValor) {
        for (int i = maxValor; i >= 5; i--) {
            boolean escalera = true;
            for (int j = 0; j < 5; j++) {
                if (valores[i - j] == 0) {
                    escalera = false;
                    break;
                }
            }
            if (escalera) return true;
        }
        return false;
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
