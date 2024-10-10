package Modelo;

import java.util.*;
import java.util.stream.Collectors;

public class AnalizadorRangos {
    private Set<String> rangos;
    private List<String> board;
    private Map<String, Integer> handCombos;
    private int totalCombos;
    private EvaluadorManos evaluador;

    private static final char[] SUITS = { 'h', 'd', 'c', 's' };

    public AnalizadorRangos(Set<String> rangos, List<String> board) {
        this.rangos = rangos;
        this.board = board;
        this.handCombos = new LinkedHashMap<>();
        this.totalCombos = 0;
        this.evaluador = new EvaluadorManos();
        inicializarTipoManos();
        analizarRango();
    }

    private void inicializarTipoManos() {
        String[] handTypes = { "STRAIGHT_FLUSH", "FOUR_OF_A_KIND", "FULL_HOUSE", "FLUSH", "STRAIGHT", "THREE_OF_A_KIND",
                "TWO_PAIR", "OVER_PAIR", "TOP_PAIR", "PP_BELOW_TP", "MIDDLE_PAIR", "WEAK_PAIR", "ACE_HIGH",
                "NO_MADE_HAND", "DRAW_FLUSH", "STRAIGHT_OPEN_ENDED", "STRAIGHT_GUTSHOT" };
        for (String handType : handTypes) {
            handCombos.put(handType, 0);
        }
    }

    private void analizarRango() {
        Set<String> todosLosCombos = new HashSet<>();

        for (String rango : rangos) {
            Set<String> combos = generarCombosValidosMano(rango);
            for (String combo : combos) {
                if (esComboValido(combo, rango))
                    todosLosCombos.add(combo);
            }
        }

        for (String combo : todosLosCombos) {
            String handType = evaluador.evaluarMejorMano(combo, board);
            handCombos.put(handType, handCombos.getOrDefault(handType, 0) + 1);

            List<String> draws = evaluador.evaluarDraws(combo);
            for (String draw : draws) {
                handCombos.put(draw, handCombos.getOrDefault(draw, 0) + 1);
            }

            totalCombos++;
        }
    }

    private Set<String> generarCombosValidosMano(String hand) {
        Set<String> combos = new HashSet<>();
        List<String> cartasRango = generarCartasDelRango(hand);

        for (int i = 1; i <= 2; i++) {
            List<List<String>> combinacionesRango = generarCombinaciones(cartasRango, i);

            List<List<String>> combinacionesFiltradas = filtrarCombinacionesDelBoard(combinacionesRango, board);

            for (List<String> cartasFiltradas : combinacionesFiltradas) {
                List<String> cartasDisponibles = new ArrayList<>(board);
                cartasDisponibles.addAll(cartasFiltradas);

                List<List<String>> combinacionesFinales = generarCombinaciones(cartasDisponibles, 5);
                for (List<String> combinacionFinal : combinacionesFinales) {
                    combos.add(String.join(",", combinacionFinal));
                }
            }
        }

        return combos;
    }

    private List<List<String>> filtrarCombinacionesDelBoard(List<List<String>> combinaciones, List<String> board) {
        List<List<String>> combinacionesFiltradas = new ArrayList<>();

        for (List<String> combinacion : combinaciones) {
            boolean contieneCartaDelBoard = false;

            for (String carta : combinacion) {
                if (board.contains(carta)) {
                    contieneCartaDelBoard = true;
                    break;
                }
            }

            if (!contieneCartaDelBoard) {
                combinacionesFiltradas.add(combinacion);
            }
        }

        return combinacionesFiltradas;
    }

    private List<String> generarCartasDelRango(String hand) {
        List<String> cartas = new ArrayList<>();
        if (hand.length() == 2) {
            char rank = hand.charAt(0);
            for (char suit : SUITS) {
                cartas.add("" + rank + suit);
            }
        } else if (hand.length() == 3) {
            char rank1 = hand.charAt(0);
            char rank2 = hand.charAt(1);
            char type = hand.charAt(2);
            if (type == 's') {
                for (char suit : SUITS) {
                    cartas.add("" + rank1 + suit);
                    cartas.add("" + rank2 + suit);
                }
            } else if (type == 'o') {
                for (char suit1 : SUITS) {
                    for (char suit2 : SUITS) {
                        if (suit1 != suit2) {
                            cartas.add("" + rank1 + suit1);
                            cartas.add("" + rank2 + suit2);
                        }
                    }
                }
            }
        }
        return cartas;
    }

    private boolean esComboValido(String combo, String rango) {
        List<String> cartasCombo = Arrays.asList(combo.split(","));
        Set<String> cartasUnicas = new HashSet<>(cartasCombo);

        if (cartasUnicas.size() != cartasCombo.size()) {
            return false;
        }

        List<String> manoCartas = new ArrayList<>();
        List<String> boardCards = new ArrayList<>(board);

        for (String card : cartasCombo) {
            if (!boardCards.contains(card)) {
                manoCartas.add(card);
            }
        }

        if (rango.length() == 2) {
            char rank = rango.charAt(0);
            for (String carta : manoCartas) {
                if (carta.charAt(0) == rank) {
                    return true;
                }
            }
            return false;
        } else if (rango.length() == 3) {
            char rank1 = rango.charAt(0);
            char rank2 = rango.charAt(1);
            char type = rango.charAt(2);

            boolean tieneRank1 = false;
            boolean tieneRank2 = false;
            char paloRank1 = 0;
            char paloRank2 = 0;

            for (String carta : manoCartas) {
                if (carta.charAt(0) == rank1) {
                    tieneRank1 = true;
                    paloRank1 = carta.charAt(1);
                } else if (carta.charAt(0) == rank2) {
                    tieneRank2 = true;
                    paloRank2 = carta.charAt(1);
                }
            }

            if (type == 's') {
                return tieneRank1 && tieneRank2 && paloRank1 == paloRank2;
            } else if (type == 'o') {
                return tieneRank1 && tieneRank2 && paloRank1 != paloRank2;
            }
        }

        return false;
    }

    private List<List<String>> generarCombinaciones(List<String> lista, int n) {
        List<List<String>> combinaciones = new ArrayList<>();
        if (n == 0) {
            combinaciones.add(new ArrayList<>());
            return combinaciones;
        }

        for (int i = 0; i < lista.size(); i++) {
            String elemento = lista.get(i);
            List<String> subLista = lista.subList(i + 1, lista.size());
            for (List<String> subCombinacion : generarCombinaciones(subLista, n - 1)) {
                List<String> combinacion = new ArrayList<>();
                combinacion.add(elemento);
                combinacion.addAll(subCombinacion);
                combinaciones.add(combinacion);
            }
        }
        return combinaciones;
    }

    public Map<String, Double> getProbabilidadesMano() {
        Map<String, Double> probabilities = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : handCombos.entrySet()) {
            if (entry.getValue() > 0) {
                double probability = (double) entry.getValue() / totalCombos * 100;
                probabilities.put(entry.getKey(), Math.round(probability * 10.0) / 10.0);
            }
        }
        return probabilities;
    }

    public Map<String, Integer> getCombosPorTipoMano() {
        return new LinkedHashMap<>(handCombos);
    }

    public int getCombosTotales() {
        return totalCombos;
    }
}