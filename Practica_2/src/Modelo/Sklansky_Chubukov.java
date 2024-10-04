package Modelo;

import java.util.Arrays;
import java.util.Comparator;

public class Sklansky_Chubukov {
	String[] cartas = {
			// Pares
			"AA", "KK", "QQ", "JJ", "TT", "99", "88", "77", "66", "55", "44", "33", "22",
			// Suited
			"AKs", "AQs", "AJs", "ATs", "A9s", "A8s", "A7s", "A6s", "A5s", "A4s", "A3s", "A2s", "KQs", "KJs", "KTs",
			"K9s", "K8s", "K7s", "K6s", "K5s", "K4s", "K3s", "K2s", "QJs", "QTs", "Q9s", "Q8s", "Q7s", "Q6s", "Q5s",
			"Q4s", "Q3s", "Q2s", "JTs", "J9s", "J8s", "J7s", "J6s", "J5s", "J4s", "J3s", "J2s", "T9s", "T8s", "T7s",
			"T6s", "T5s", "T4s", "T3s", "T2s", "98s", "97s", "96s", "95s", "94s", "93s", "92s", "87s", "86s", "85s",
			"84s", "83s", "82s", "76s", "75s", "74s", "73s", "72s", "65s", "64s", "63s", "62s", "54s", "53s", "52s",
			"43s", "42s", "32s",
			// Offsuit
			"AKo", "AQo", "AJo", "ATo", "A9o", "A8o", "A7o", "A6o", "A5o", "A4o", "A3o", "A2o", "KQo", "KJo", "KTo",
			"K9o", "K8o", "K7o", "K6o", "K5o", "K4o", "K3o", "K2o", "QJo", "QTo", "Q9o", "Q8o", "Q7o", "Q6o", "Q5o",
			"Q4o", "Q3o", "Q2o", "JTo", "J9o", "J8o", "J7o", "J6o", "J5o", "J4o", "J3o", "J2o", "T9o", "T8o", "T7o",
			"T6o", "T5o", "T4o", "T3o", "T2o", "98o", "97o", "96o", "95o", "94o", "93o", "92o", "87o", "86o", "85o",
			"84o", "83o", "82o", "76o", "75o", "74o", "73o", "72o", "65o", "64o", "63o", "62o", "54o", "53o", "52o",
			"43o", "42o", "32o" };

	double[] valores = {
			// Pares
			999, 277, 137, 92, 70, 52, 45, 40, 35, 36, 33, 31, 29,
			// Suited
			166, 96, 68, 53, 41, 36, 31, 28, 28, 26, 24, 24, 96, 68, 53, 41, 31, 28, 24, 22, 20, 19, 18, 17, 68, 53, 41,
			31, 24, 22, 20, 18, 17, 16, 15, 14, 53, 41, 31, 24, 20, 18, 16, 15, 14, 13, 12, 11, 41, 31, 24, 20, 16, 15,
			14, 13, 12, 11, 10, 9, 36, 28, 22, 18, 15, 14, 13, 12, 11, 10, 9, 8, 31, 24, 20, 16, 14, 13, 12, 11, 10, 9,
			8, 7,
			// Offsuit
			77, 67, 58, 49, 41, 33, 33, 32, 32, 31, 30, 29, 67, 58, 49, 41, 33, 32, 30, 28, 26, 25, 23, 22, 58, 49, 41,
			33, 30, 28, 26, 24, 22, 21, 19, 18, 49, 41, 33, 30, 26, 24, 22, 20, 19, 17, 16, 15, 41, 33, 30, 26, 22, 20,
			19, 17, 16, 15, 14, 13, 36, 32, 28, 24, 20, 19, 17, 16, 15, 14, 13, 12, 33, 30, 26, 22, 19, 17, 16, 15, 14,
			13, 12, 11 };

	String[] cartasOrdenadas;

	public Sklansky_Chubukov() {
		ordenarCartas();
	}

	private void ordenarCartas() {
		Integer[] indices = new Integer[cartas.length];
		for (int i = 0; i < indices.length; i++) {
			indices[i] = i;
		}

		Arrays.sort(indices, new Comparator<Integer>() {
			public int compare(Integer i1, Integer i2) {
				return Double.compare(valores[i2], valores[i1]);
			}
		});

		cartasOrdenadas = new String[cartas.length];
		for (int i = 0; i < cartas.length; i++) {
			cartasOrdenadas[i] = cartas[indices[i]];
		}
	}

	public String[] getCartas() {
		return cartas;
	}

	public String[] getCartasOrdenadas() {
		return cartasOrdenadas;
	}
}