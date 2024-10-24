package Modelo;

import java.util.*;

public class Equity {
    private List<List<String>> cartasJugadores;
    private List<String> cartasTablero;
    private boolean type_game;
    private int SIMULACIONES = 2000;

    public Equity(List<List<String>> cartasJugadores, List<String> cartasTablero, boolean type_game) {
        this.cartasJugadores = cartasJugadores;
        this.cartasTablero = cartasTablero;
        this.type_game = type_game;
        SIMULACIONES = type_game ? 2000000 : 100000;
    }

    public List<List<String>> calculateEquity() {
        double[] puntos = new double[cartasJugadores.size()];

        for (int i = 0; i < SIMULACIONES; i++) {
            List<String> deck = generateDeck();
            List<String> tableroSimulado = new ArrayList<>(cartasTablero);

            while (tableroSimulado.size() < 5) {
                tableroSimulado.add(robarCarta(deck));
            }

            List<Integer> ganadores = evaluarManos(tableroSimulado);
            double puntosJugador = 1.0 / ganadores.size();
            
            for (int ganador : ganadores) {
                puntos[ganador] += puntosJugador;
            }
        }

        List<List<String>> equidad = new ArrayList<>();
        for (int i = 0; i < cartasJugadores.size(); i++) {
            double porcentajeEquidad = (puntos[i] * 100.0) / SIMULACIONES;
            equidad.add(Arrays.asList(String.format("%.2f", porcentajeEquidad) + "%"));
        }

        return equidad;
    }
    
    private List<String> generateDeck() {
        List<String> deck = new ArrayList<>();
        String[] palos = {"h", "d", "c", "s"};
        String[] rangos = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};

        for (String palo : palos) {
            for (String rango : rangos) {
                String carta = rango + palo;
                if (!cartasTablero.contains(carta) && !esCartaUsadaPorJugadores(carta)) {
                    deck.add(carta);
                }
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

    private boolean esCartaUsadaPorJugadores(String carta) {
        for (List<String> manoJugador : cartasJugadores) {
            if (manoJugador.contains(carta)) {
                return true;
            }
        }
        return false;
    }

    private String robarCarta(List<String> deck) {
        return deck.remove(deck.size() - 1);
    }

    private List<Integer> evaluarManos(List<String> tablero) {
        List<Integer> ganadores = new ArrayList<>();
        int[] mejorMano = null;

        for (int i = 0; i < cartasJugadores.size(); i++) {
            int[] mejorManoJugador = null;
            List<String> manoJugador = cartasJugadores.get(i);

            if (type_game) {
                List<String> mano = new ArrayList<>(manoJugador);
                mano.addAll(tablero);
                mejorManoJugador = evaluarMano(mano);
            } else {
                mejorManoJugador = evaluarMejorManoOmaha(manoJugador, tablero);
            }
            
            if (mejorMano == null || compararManos(mejorManoJugador, mejorMano) > 0) {
                mejorMano = mejorManoJugador;
                ganadores.clear();
                ganadores.add(i);
            } else if (compararManos(mejorManoJugador, mejorMano) == 0) {
                ganadores.add(i);
            }
        }

        return ganadores;
    }

    private int[] evaluarMejorManoOmaha(List<String> manoJugador, List<String> tablero) {
        int[] mejorMano = null;
        List<List<String>> combinacionesMano = obtenerCombinacionesDeDos(manoJugador);
        List<List<String>> combinacionesTablero = obtenerCombinacionesDeTres(tablero);

        for (List<String> manoPlayer : combinacionesMano) {
            for (List<String> board : combinacionesTablero) {
                List<String> manoCompleta = new ArrayList<>(manoPlayer);
                manoCompleta.addAll(board);
                
                int[] valorManoActual = evaluarMano(manoCompleta);
                if (mejorMano == null || compararManos(valorManoActual, mejorMano) > 0) {
                    mejorMano = valorManoActual;
                }
            }
        }
        
        return mejorMano;
    }
    
    private List<List<String>> obtenerCombinacionesDeDos(List<String> cartas) {
        List<List<String>> combinaciones = new ArrayList<>();
        for (int i = 0; i < cartas.size() - 1; i++) {
            for (int j = i + 1; j < cartas.size(); j++) {
                List<String> combinacion = new ArrayList<>();
                combinacion.add(cartas.get(i));
                combinacion.add(cartas.get(j));
                combinaciones.add(combinacion);
            }
        }
        return combinaciones;
    }

    private List<List<String>> obtenerCombinacionesDeTres(List<String> cartas) {
        List<List<String>> combinaciones = new ArrayList<>();
        for (int i = 0; i < cartas.size() - 2; i++) {
            for (int j = i + 1; j < cartas.size() - 1; j++) {
                for (int k = j + 1; k < cartas.size(); k++) {
                    List<String> combinacion = new ArrayList<>();
                    combinacion.add(cartas.get(i));
                    combinacion.add(cartas.get(j));
                    combinacion.add(cartas.get(k));
                    combinaciones.add(combinacion);
                }
            }
        }
        return combinaciones;
    }
    
    private int compararManos(int[] mano1, int[] mano2) {
        for (int i = 0; i < mano1.length; i++) {
            if (mano1[i] != mano2[i]) {
                return Integer.compare(mano1[i], mano2[i]);
            }
        }
        return 0;
    }

    private int[] evaluarMano(List<String> mano) {
        Map<Integer, Integer> conteoRangos = new TreeMap<>(Collections.reverseOrder());
        Map<Character, List<String>> cartasPorPalo = new HashMap<>();
        
        for (String carta : mano) {
            char palo = carta.charAt(1);
            cartasPorPalo.putIfAbsent(palo, new ArrayList<>());
            cartasPorPalo.get(palo).add(carta);
            
            int valor = getRankValue(carta.charAt(0));
            conteoRangos.put(valor, conteoRangos.getOrDefault(valor, 0) + 1);
        }
        
        //resultado[0] es el tipo de mano, a igualdad de tipo de mano se comparan los valores de resultado[1-5]
        int[] resultado = new int[6];

        for (List<String> cartasPalo : cartasPorPalo.values()) {
            if (cartasPalo.size() >= 5 && escalera(cartasPalo)) {
                resultado[0] = 8;
                List<Integer> valores = getRanks(cartasPalo);
                Collections.sort(valores, Collections.reverseOrder());
                for (int i = 0; i < 5; i++) {
                    resultado[i + 1] = valores.get(i);
                }
                return resultado;
            }
        }

        Map<Integer, List<Integer>> frecuencias = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : conteoRangos.entrySet()) {
            int valor = entry.getKey();
            int frecuencia = entry.getValue();
            frecuencias.putIfAbsent(frecuencia, new ArrayList<>());
            frecuencias.get(frecuencia).add(valor);
        }

        // Poker
        if (frecuencias.containsKey(4)) {
            resultado[0] = 7;
            resultado[1] = frecuencias.get(4).get(0);
            // Kicker más alto
            for (int valor : conteoRangos.keySet()) {
                if (conteoRangos.get(valor) != 4) {
                    resultado[2] = valor;
                    break;
                }
            }
            return resultado;
        }

        // Full
        if (frecuencias.containsKey(3) && frecuencias.containsKey(2)) {
            resultado[0] = 6;
            resultado[1] = frecuencias.get(3).get(0);
            resultado[2] = Collections.max(frecuencias.get(2));
            return resultado;
        }

        // Escalera
        if (escalera(mano)) {
            resultado[0] = 5;
            List<Integer> valores = getRanks(mano);
            Collections.sort(valores, Collections.reverseOrder());
            for (int i = 0; i < 5; i++) {
                resultado[i + 1] = valores.get(i);
            }
            return resultado;
        }

        // Trio
        if (frecuencias.containsKey(3)) {
            resultado[0] = 4;
            resultado[1] = frecuencias.get(3).get(0);
            int count = 2;
            for (int valor : conteoRangos.keySet()) {
                if (conteoRangos.get(valor) != 3) {
                    resultado[count++] = valor;
                    if (count > 3) break;
                }
            }
            return resultado;
        }

        // Doble Pareja
        if (frecuencias.containsKey(2) && frecuencias.get(2).size() >= 2) {
            resultado[0] = 3;
            List<Integer> pares = frecuencias.get(2);
            Collections.sort(pares, Collections.reverseOrder());
            resultado[1] = pares.get(0);
            resultado[2] = pares.get(1);
            for (int valor : conteoRangos.keySet()) {
                if (conteoRangos.get(valor) == 1) {
                    resultado[3] = valor;
                    break;
                }
            }
            return resultado;
        }

        // Pareja
        if (frecuencias.containsKey(2)) {
            resultado[0] = 2;
            resultado[1] = frecuencias.get(2).get(0);
            int count = 2;
            for (int valor : conteoRangos.keySet()) {
                if (conteoRangos.get(valor) == 1) {
                    resultado[count++] = valor;
                    if (count > 4) break;
                }
            }
            return resultado;
        }

        // Carta Alta
        resultado[0] = 1;
        int count = 1;
        for (int valor : conteoRangos.keySet()) {
            resultado[count++] = valor;
            if (count > 5) break;
        }
        return resultado;
    }

    public boolean escalera(List<String> cards) {
        List<Integer> ranks = getRanks(cards);
        Collections.sort(ranks);

        int count = 1;
        int prev = ranks.get(0);

        for (int i = 1; i < ranks.size(); i++) {
            int current = ranks.get(i);
            if (current == prev + 1) {
                count++;
                if (count == 5) {
                    return true;
                }
            } else if (current != prev) {
                count = 1;
            }
            prev = current;
        }

        return false;
    }

    private List<Integer> getRanks(List<String> cards) {
        List<Integer> ranks = new ArrayList<>();
        for (String card : cards) {
            int value = getRankValue(card.charAt(0));
            ranks.add(value);
            if (value == 14) {
                ranks.add(1);
            }
        }
        return ranks;
    }

    private int getRankValue(char rank) {
        switch (rank) {
            case 'T': return 10;
            case 'J': return 11;
            case 'Q': return 12;
            case 'K': return 13;
            case 'A': return 14;
            default: return Character.getNumericValue(rank);
        }
    }
}