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
		Map<Character, Integer> paloConteo = new HashMap<>();
		for (Carta carta : cartas) {
			paloConteo.put(carta.getPalo(), paloConteo.getOrDefault(carta.getPalo(), 0) + 1);
		}
		return paloConteo.containsValue(4);
	}

	private boolean tieneGutshot(List<Carta> cartas) {
		List<Integer> valores = new ArrayList<>();
		for (Carta carta : cartas) {
			valores.add(carta.getValor());
		}
		valores.sort(Comparator.naturalOrder());

		for (int i = 0; i < valores.size() - 4; i++) {
			//Verifica que las cartas en los extremos (i y i + 4) tienen una diferencia de 4
			//y verifica que no hay una diferencia de 2 entre las cartas intermedias
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

		int[] valoresArray = new int[15];//Cuenta cuantas cartas hay de cada valor
		for (int valor : valores) {
			valoresArray[valor]++;
		}

		if (UtilidadesMano.tieneEscalera(valoresArray, valores.get(valores.size() - 1)) != -1) {
			return false;
		}

		for (int i = 0; i < valores.size() - 3; i++) {
			int diferencia = valores.get(i + 3) - valores.get(i);//Diferencia entre la cuarta carta y la primera

			if (diferencia == 3 && valores.get(i) > 2 && valores.get(i + 3) < 14) { // Verificar que no este en los extremos
				return true;
			}
		}
		return false;
	}
	
}