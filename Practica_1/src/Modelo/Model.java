package Modelo;

import java.util.*;

public class Model {

    public List<Carta> parsearCartas(String mano) {
        List<Carta> cartas = new ArrayList<>();
        for (int i = 0; i < mano.length(); i += 2) {
            char valor = mano.charAt(i);
            char palo = mano.charAt(i + 1);
            cartas.add(new Carta(valor, palo));
        }
        return cartas;
    }

    public String evaluarMejorMano(List<Carta> cartas) {
        // Aquí evaluaremos las diferentes jugadas posibles
        cartas.sort(Comparator.comparingInt(Carta::getValor).reversed());

        Map<Integer, Long> valorConteo = new HashMap<>();
        for (Carta carta : cartas) {
            valorConteo.put(carta.getValor(), valorConteo.getOrDefault(carta.getValor(), 0L) + 1);
        }

        if (valorConteo.containsValue(2L)) {
            return "Pareja de " + getNombreValor(valorConteo.entrySet().stream()
                    .filter(entry -> entry.getValue() == 2L)
                    .findFirst().get().getKey()) + "s";
        }

        return "Carta alta: " + getNombreValor(cartas.get(0).getValor());
    }

    public List<String> detectarDraws(List<Carta> cartas) {
        List<String> draws = new ArrayList<>();
        if (tieneFlushDraw(cartas)) {
            draws.add("Flush Draw");
        }
        if (tieneGutshot(cartas)) {
            draws.add("Gutshot");
        }
        if (tieneOpenEnded(cartas)) {
            draws.add("Straight Open-Ended");
        }
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
        valores.sort(Comparator.reverseOrder());
        return valores.get(0) - valores.get(1) == 2 && valores.get(1) - valores.get(2) == 1 && valores.get(2) - valores.get(3) == 1;
    }

    private boolean tieneOpenEnded(List<Carta> cartas) {
        List<Integer> valores = new ArrayList<>();
        for (Carta carta : cartas) {
            valores.add(carta.getValor());
        }
        valores.sort(Comparator.reverseOrder());
        for (int i = 0; i < valores.size() - 1; i++) {
            if (valores.get(i) - valores.get(i + 1) != 1) {
                return false;
            }
        }
        return true;
    }

    private String getNombreValor(int valor) {
        switch (valor) {
            case 14:
                return "A";
            case 13:
                return "K";
            case 12:
                return "Q";
            case 11:
                return "J";
            case 10:
                return "T";
            default:
                return String.valueOf(valor);
        }
    }

    public static class Carta {

        private final int valor;
        private final char palo;

        public Carta(char valor, char palo) {
            this.valor = parseValor(valor);
            this.palo = palo;
        }

        public int getValor() {
            return valor;
        }

        public char getPalo() {
            return palo;
        }

        private int parseValor(char valor) {
            switch (valor) {
                case 'A':
                    return 14;
                case 'K':
                    return 13;
                case 'Q':
                    return 12;
                case 'J':
                    return 11;
                case 'T':
                    return 10;
                default:
                    return valor - '0';
            }
        }
    }
}
