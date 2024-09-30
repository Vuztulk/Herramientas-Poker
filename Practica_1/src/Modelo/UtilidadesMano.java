package Modelo;

import java.util.*;

public class UtilidadesMano {
	
	public static int tieneEscalera(int[] valores, int maxValor) { //Devuelve la posicion mas alta donde empieza la escalera
        for (int i = maxValor; i >= 5; i--) {
            boolean escalera = true;
            for (int j = 0; j < 5; j++) {
                if (valores[i - j] == 0) {
                    escalera = false;
                    break;
                }
            }
            if (escalera) return i;
        }
        return -1;
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

    public static int[] contarValores(List<Carta> cartas) {
        int[] valores = new int[15];
        for (Carta carta : cartas) {
            valores[carta.getValor()]++;
        }
        return valores;
    }

    public static Map<Character, Integer> contarPalos(List<Carta> cartas) {
        Map<Character, Integer> conteoPalos = new HashMap<>();
        for (Carta carta : cartas) {
            char palo = carta.getPalo();
            conteoPalos.put(palo, conteoPalos.getOrDefault(palo, 0) + 1);
        }
        return conteoPalos;
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

    public static boolean contieneNDeUnValor(int[] valores, int n) {
        for (int valor : valores) {
            if (valor == n) return true;
        }
        return false;
    }

    public static int obtenerPosicionCartaMasAlta(List<Carta> cartas, int max) {
        int posMax = -1;
        for (int j = 0; j < cartas.size(); j++) {
            if (cartas.get(j).getValor() == max) {
                posMax = j;
            }
        }
        return posMax;
    }
	
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