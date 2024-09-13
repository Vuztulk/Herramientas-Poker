package Modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mano {
	
	public String evaluarMejorMano(List<Carta> cartas) {
		
		int[] valores = new int[15];
		List<Carta> mejoresCartas = new ArrayList<>();
		Map<Character, Long> conteoPalos = new HashMap<>();
		int max = UtilidadesCarta.obtenerValorMaximo(cartas);
        int posMax = -1, j = 0;
        
		for (Carta carta : cartas) {
			valores[carta.getValor()]++;
			conteoPalos.put(carta.getPalo(), conteoPalos.getOrDefault(carta.getPalo(), 0L) + 1);
			
			if (cartas.get(j).getValor() == max) {
                posMax = j;
            }
			j++;
		}

		// Escalera de color
		for (Character palo : conteoPalos.keySet()) {
			if (conteoPalos.get(palo) >= 5) {
				List<Carta> cartasDelMismoPalo = UtilidadesMano.filtrarCartasPorPalo(cartas, palo);
				int[] valoresDelPalo = new int[15];
				for (Carta carta : cartasDelMismoPalo) {
					valoresDelPalo[carta.getValor()]++;
				}
				for (int i = 14; i >= 5; i--) {
					if (UtilidadesMano.esEscalera(valoresDelPalo, i)) {
						mejoresCartas = UtilidadesMano.obtenerCartasPorValor(cartasDelMismoPalo, i, 5);
						return "Escalera de color [" + UtilidadesCarta.getNombreValor(i - 4) + " a " + UtilidadesCarta.getNombreValor(i) + "]";
					}
				}
			}
		}

		// Color
		for (Character palo : conteoPalos.keySet()) {
			if (conteoPalos.get(palo) >= 5) {
				mejoresCartas = UtilidadesMano.filtrarCartasPorPalo(cartas, palo);
				mejoresCartas.sort(Comparator.comparingInt(Carta::getValor).reversed());
				return "Color " + mejoresCartas.subList(0, 5);
			}
		}
		// Escalera
		for (int i = 14; i >= 5; i--) {
			if (UtilidadesMano.esEscalera(valores, i)) {
				mejoresCartas = UtilidadesMano.obtenerCartasPorValor(cartas, i, 5);
				return "Escalera [" + UtilidadesCarta.getNombreValor(i - 4) + " - " + UtilidadesCarta.getNombreValor(i) + "]";
			}
		}

		// Poker
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 4) {
				mejoresCartas = UtilidadesMano.obtenerCartasPorValor(cartas, i, 4);
				return "Póker de " + UtilidadesCarta.getNombreValor(i) + " " + mejoresCartas;
			}
		}

		// Full
		if (UtilidadesMano.esFull(valores)) {
			int trio = UtilidadesMano.obtenerValorConRepeticiones(valores, 3);
			int pareja = UtilidadesMano.obtenerValorConRepeticiones(valores, 2);
			mejoresCartas = UtilidadesMano.obtenerCartasPorValor(cartas, trio, 3);
			mejoresCartas.addAll(UtilidadesMano.obtenerCartasPorValor(cartas, pareja, 2));
			return "Full de " + UtilidadesCarta.getNombreValor(trio) + " " + UtilidadesCarta.getNombreValor(pareja) + ": " + mejoresCartas;
		}

		// Trio
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 3) {
				mejoresCartas = UtilidadesMano.obtenerCartasPorValor(cartas, i, 3);
				return "Trío de " + UtilidadesCarta.getNombreValor(i) + " " + mejoresCartas;
			}
		}

		// Pareja
		for (int i = 14; i >= 2; i--) {
			if (valores[i] == 2) {
				mejoresCartas = UtilidadesMano.obtenerCartasPorValor(cartas, i, 2);
				return "Pareja de " + UtilidadesCarta.getNombreValor(i) + " " + mejoresCartas;
			}
		}

		// Carta mas alta
		return "Carta mas alta [" + UtilidadesCarta.getNombreValor(max) + cartas.get(posMax).getPalo() +  "]";
	}
	
}
