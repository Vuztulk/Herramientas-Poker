package Vista;

import java.util.Arrays;
import java.util.Comparator;

public class Ranking {

    String[] cartas = {
        "AA", "AKs", "AQs", "AJs", "ATs", "A9s", "A8s", "A7s", "A6s", "A5s", "A4s", "A3s", "A2s", 
        "AKo", "KK", "KQs", "KJs", "KTs", "K9s", "K8s", "K7s", "K6s", "K5s", "K4s", "K3s", "K2s", 
        "AQo", "KQo", "QQ", "QJs", "QTs", "Q9s", "Q8s", "Q7s", "Q6s", "Q5s", "Q4s", "Q3s", "Q2s", 
        "AJo", "KJo", "QJo", "JJ", "JTs", "J9s", "J8s", "J7s", "J6s", "J5s", "J4s", "J3s", "J2s", 
        "ATo", "KTo", "QTo", "JTo", "TT", "T9s", "T8s", "T7s", "T6s", "T5s", "T4s", "T3s", "T2s", 
        "A9o", "K9o", "Q9o", "J9o", "T9o", "99", "98s", "97s", "96s", "95s", "94s", "93s", "92s", 
        "A8o", "K8o", "Q8o", "J8o", "T8o", "98o", "88", "87s", "86s", "85s", "84s", "83s", "82s", 
        "A7o", "K7o", "Q7o", "J7o", "T7o", "97o", "87o", "77", "67", "76s", "75s", "74s", "73s", 
        "72s", "A6o", "K6o", "Q6o", "J6o", "T6o", "96o", "86o", "76o", "66", "58", "65s", "64s", 
        "63s", "62s", "A5o", "K5o", "Q5o", "J5o", "T5o", "95o", "85o", "75o", "65o", "55", "54s", 
        "53s", "52s", "A4o", "K4o", "Q4o", "J4o", "T4o", "94o", "84o", "74o", "64o", "54o", "44", 
        "43s", "42s", "A3o", "K3o", "Q3o", "J3o", "T3o", "93o", "83o", "73o", "63o", "53o", "43o", 
        "33", "32s", "A2o", "K2o", "Q2o", "J2o", "T2o", "92o", "82o", "72o", "62o", "52o", "42o", 
        "32o", "22" 
    };

    double[] valores = {
        999, 277, 137, 92, 70, 52, 45, 40, 35, 36, 33, 31, 29, 166, 477, 43, 36, 31, 24, 20, 19, 18,
        17, 16, 15, 14, 13, 96, 29, 239, 25, 22, 16, 13, 11, 11, 10, 9.5, 8.9, 8.3, 68, 25, 16, 
        160, 18, 13, 10, 8.6, 7.4, 7, 6.5, 6, 7, 53, 23, 15, 12, 120, 11, 8.7, 7.1, 6, 5, 4.6, 
        4.2, 3.8, 41, 18, 12, 8.9, 7.4, 96, 7.6, 6.1, 5, 4.1, 3.3, 3, 2.7, 36, 15, 9.9, 7.4, 
        6.1, 5.1, 80, 5.6, 4.5, 3.6, 2.8, 2.2, 2.1, 31, 14, 8.5, 6.3, 5.1, 4.3, 3.8, 77, 67, 
        4.2, 3.3, 2.6, 2, 1.6, 28, 13, 8.1, 5.4, 4.3, 3.5, 3, 2.7, 58, 3.1, 2.4, 1.9, 1.5, 
        28, 12, 7.5, 5, 3.5, 2.8, 2.4, 2.1, 2, 49, 2.4, 1.9, 1.6, 26, 11, 6.8, 4.5, 3.1, 2.2, 
        1.9, 1.7, 1.6, 1.6, 41, 1.7, 1.4, 24, 11, 6.3, 4, 2.7, 2, 1.5, 1.4, 1.3, 1.3, 1.2, 
        33, 33, 32 
    };

    // Atributo para las cartas ordenadas por valor
    String[] cartasOrdenadas;

    public Ranking() {
        ordenarCartas();
    }

    // Método para ordenar las cartas
    private void ordenarCartas() {
        cartasOrdenadas = new String[cartas.length];
        for (int i = 0; i < cartas.length; i++) {
            cartasOrdenadas[i] = cartas[i];
        }
        
        // Usar Arrays.sort con un comparador basado en el array de valores
        Arrays.sort(cartasOrdenadas, new Comparator<String>() {
            @Override
            public int compare(String c1, String c2) {
                int index1 = indexOf(c1);
                int index2 = indexOf(c2);
                return Double.compare(valores[index1], valores[index2]);
            }
        });
    }

    // Método para encontrar el índice de una carta en el array
    private int indexOf(String carta) {
        for (int i = 0; i < cartas.length; i++) {
            if (cartas[i].equals(carta)) {
                return i;
            }
        }
        return -1; // No debería ocurrir
    }

    public String[] getCartas() {
        return cartas;
    }

    public String[] getCartasOrdenadas() {
        return cartasOrdenadas;
    }
}
