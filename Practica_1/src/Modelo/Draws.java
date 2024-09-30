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
            draws.add("Straight Gunshot");
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
        
        int[] valoresArray = new int[15];
        for (Carta carta : cartas) {
        	valoresArray[carta.getValor()]++; // Cuenta cuantas cartas hay de cada valor
        }
        
        //En esta caso comprobamos si ya hay una escalera, si la hay directamente damos como caso negativo, aun que se podria comprobar
        //en el for de mas abajo si hay una escalera mas alta que la que se ha comprobado en esta funcion, tan solo empezando por i = 14 en vez de 1
        if (UtilidadesMano.tieneEscalera(valoresArray, valores.get(valores.size() - 1)) != -1) {
            return false;
        }
        
        // Consideramos el As como 1 y 14
        if (valoresArray[14] > 0) {
        	valoresArray[1] = valoresArray[14];
        }

        int consecutivos = 0;
        int huecos = 0;
        for (int i = 1; i <= 14; i++) {
            if (valoresArray[i] > 0) {
                consecutivos++;
            } else {
                huecos++;
                if (huecos > 1) {
                    if (consecutivos == 4 && huecos == 2) {
                        return true;
                    }
                    consecutivos = 0;
                    huecos = 0;
                }
            }
            if (consecutivos == 4 && huecos == 1) {
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
            if (valores.get(i + 1) - valores.get(i) == 1 && valores.get(i + 2) - valores.get(i + 1) == 1 && valores.get(i + 3) - valores.get(i + 2) == 1) {
                return true;
            }
        }

        return false;
    }


}