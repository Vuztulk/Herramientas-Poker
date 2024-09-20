package Modelo;

import java.util.*;

public class Draws {

	public Draws() {

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

		if (UtilidadesMano.tieneEscalera(valoresArray, valores.get(valores.size() - 1)) != -1) {
			return false;
		}

		for (int i = 0; i < valores.size() - 3; i++) {
			int diferencia = valores.get(i + 3) - valores.get(i);

			if (diferencia == 3 && valores.get(i) > 2 && valores.get(i + 3) < 14) { // Verificar que no este en losextremos
				return true;
			}
		}
		return false;
	}
	
}