package Modelo;

import java.util.Arrays;
import java.util.Comparator;

public class Sklansky_Chubukov {
	
	String[] cartas = {

		    "AA", "KK", "QQ", "JJ", "TT", "99", "88", "77", "66", "55", "44", "33", "22",

		    "AKs", "AQs", "AJs", "ATs", "A9s", "A8s", "A7s", "A6s", "A5s", "A4s", "A3s", "A2s",
		    "KQs", "KJs", "KTs", "K9s", "K8s", "K7s", "K6s", "K5s", "K4s", "K3s", "K2s",
		    "QJs", "QTs", "Q9s", "Q8s", "Q7s", "Q6s", "Q5s", "Q4s", "Q3s", "Q2s",
		    "JTs", "J9s", "J8s", "J7s", "J6s", "J5s", "J4s", "J3s", "J2s",
		    "T9s", "T8s", "T7s", "T6s", "T5s", "T4s", "T3s", "T2s",
		    "98s", "97s", "96s", "95s", "94s", "93s", "92s",
		    "87s", "86s", "85s", "84s", "83s", "82s",
		    "76s", "75s", "74s", "73s", "72s",
		    "65s", "64s", "63s", "62s",
		    "54s", "53s", "52s",
		    "43s", "42s",
		    "32s",

		    "AKo", "AQo", "AJo", "ATo", "A9o", "A8o", "A7o", "A6o", "A5o", "A4o", "A3o", "A2o",
		    "KQo", "KJo", "KTo", "K9o", "K8o", "K7o", "K6o", "K5o", "K4o", "K3o", "K2o",
		    "QJo", "QTo", "Q9o", "Q8o", "Q7o", "Q6o", "Q5o", "Q4o", "Q3o", "Q2o",
		    "JTo", "J9o", "J8o", "J7o", "J6o", "J5o", "J4o", "J3o", "J2o",
		    "T9o", "T8o", "T7o", "T6o", "T5o", "T4o", "T3o", "T2o",
		    "98o", "97o", "96o", "95o", "94o", "93o", "92o",
		    "87o", "86o", "85o", "84o", "83o", "82o",
		    "76o", "75o", "74o", "73o", "72o",
		    "65o", "64o", "63o", "62o",
		    "54o", "53o", "52o",
		    "43o", "42o",
		    "32o"
		};

		double[] valores = {

		    999.0, 477.0, 239.0, 160.0, 120.0, 96.0, 80.0, 67.0, 58.0, 49.0, 41.0, 33.0, 24.0,

		    277.0, 137.0, 92.0, 70.0, 52.0, 45.0, 40.0, 35.0, 36.0, 33.0, 31.0, 29.0,
		    43.0, 36.0, 31.0, 24.0, 20.0, 19.0, 17.0, 16.0, 15.0, 14.0, 13.0,
		    25.0, 22.0, 16.0, 13.0, 11.0, 11.0, 10.0, 9.5, 8.9, 8.3,
		    18.0, 13.0, 10.0, 8.6, 7.4, 7.0, 6.5, 6.0, 5.6,
		    11.0, 8.7, 7.1, 6.0, 5.0, 4.6, 4.2, 3.8,
		    7.6, 6.1, 5.0, 4.1, 3.3, 3.0, 2.7,
		    5.6, 4.5, 3.6, 2.8, 2.2, 2.1,
		    4.2, 3.3, 2.6, 2.0, 1.6,
		    3.1, 2.4, 1.9, 1.5,
		    2.4, 1.9, 1.6,
		    1.7, 1.4,
		    1.3,

		    166.0, 96.0, 68.0, 53.0, 41.0, 36.0, 31.0, 28.0, 28.0, 26.0, 24.0, 23.0,
		    29.0, 25.0, 23.0, 18.0, 15.0, 14.0, 13.0, 12.0, 11.0, 11.0, 10.0,
		    16.0, 15.0, 12.0, 9.9, 8.5, 8.1, 7.5, 6.8, 6.3, 5.7,
		    12.0, 8.9, 7.4, 6.3, 5.4, 5.0, 4.5, 4.0, 3.4,
		    7.4, 6.1, 5.1, 4.3, 3.5, 3.1, 2.7, 2.4,
		    5.1, 4.3, 3.5, 2.8, 2.2, 2.0, 1.8,
		    3.8, 3.0, 2.4, 1.9, 1.5, 1.4,
		    2.7, 2.1, 1.7, 1.4, 1.1,
		    2.0, 1.6, 1.3, 1.1,
		    1.6, 1.3, 1.1,
		    1.2, 0.9,
		    0.9
		};
		
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