package Modelo;

import java.util.*;
import java.util.stream.Collectors;

public class AnalizadorRangos {
	private Set<String> rangos;
	private List<String> board;
	private Map<String, Integer> handCombos;
	private int totalCombos;

	private static final char[] SUITS = { 'h', 'd', 'c', 's' };

	public AnalizadorRangos(Set<String> rangos, List<String> board) {
		this.rangos = rangos;
		this.board = board;
		this.handCombos = new LinkedHashMap<>();
		this.totalCombos = 0;
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
			String handType = evaluarMejorMano(combo);
			handCombos.put(handType, handCombos.getOrDefault(handType, 0) + 1);

			List<String> draws = evaluarDraws(combo);
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

	// Quita combinaciones que contengan cartas del rango en el board
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

//Evaluacion de manos

	private String evaluarMejorMano(String hand) {
		List<String> cartas = extraerCartas(hand);

		if (escaleraColor(cartas))
			return "STRAIGHT_FLUSH";
		if (poker(cartas))
			return "FOUR_OF_A_KIND";
		if (full(cartas))
			return "FULL_HOUSE";
		if (color(cartas))
			return "FLUSH";
		if (escalera(cartas))
			return "STRAIGHT";
		if (trio(cartas))
			return "THREE_OF_A_KIND";
		if (doblePar(cartas))
			return "TWO_PAIR";

		String tipoPar = par(cartas);
		if (!tipoPar.equals("NO_MADE_HAND"))
			return tipoPar;

		if (hand.contains("A"))
			return "ACE_HIGH";

		return "NO_MADE_HAND";
	}

	private List<String> evaluarDraws(String hand) {
		List<String> cartas = extraerCartas(hand);
		List<String> draws = new ArrayList<>();

		if (flushDraw(cartas))
			draws.add("DRAW_FLUSH");
		if (straightOpenEnded(cartas))
			draws.add("STRAIGHT_OPEN_ENDED");
		if (straightGutshot(cartas))
			draws.add("STRAIGHT_GUTSHOT");

		return draws;
	}

	private boolean escaleraColor(List<String> cards) {
		return color(cards) && escalera(cards);
	}

	private boolean poker(List<String> cards) {
		return getRankCount(cards).containsValue(4);
	}

	private boolean full(List<String> cards) {
		Map<Character, Integer> rankCount = getRankCount(cards);
		return rankCount.containsValue(3) && rankCount.containsValue(2);
	}

	private boolean color(List<String> cards) {
		return getSuitCount(cards).containsValue(5);
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

	private boolean trio(List<String> cards) {
		Map<Character, Integer> rankCount = getRankCount(cards);
		return rankCount.containsValue(3);
	}

	private boolean doblePar(List<String> cards) {
		Map<Character, Integer> rankCount = getRankCount(cards);
		return Collections.frequency(rankCount.values(), 2) == 2;
	}

	private String par(List<String> handCards) {
		List<Integer> boardValues = getBoardValues(board);
		List<String> boardCards = new ArrayList<>(board);
		List<String> manoCartas = new ArrayList<>();

		for (String card : handCards) {
			if (!boardCards.contains(card)) {
				manoCartas.add(card);
			}
		}

		if (manoCartas.size() == 1) {

			int handValue = getRankValue(manoCartas.get(0).charAt(0));
			int highestBoardValue = maxRank(boardValues);
			int secondHighestBoardValue = getSegundoMasAlto(boardValues);

			if (handValue == highestBoardValue) {
				return "TOP_PAIR";
			} else if (handValue > highestBoardValue) {
				return "OVER_PAIR";
			} else if (handValue == secondHighestBoardValue) {
				return "MIDDLE_PAIR";
			} else if (handValue < highestBoardValue && handValue > secondHighestBoardValue) {
				return "PP_BELOW_TP";
			} else {
				return "NO_MADE_HAND";
			}
		} else if (manoCartas.size() == 2) {
			int handValue1 = getRankValue(manoCartas.get(0).charAt(0));
			int handValue2 = getRankValue(manoCartas.get(1).charAt(0));

			int highestBoardValue = maxRank(boardValues);
			int secondHighestBoardValue = getSegundoMasAlto(boardValues);

			String rank = "";

			if (handValue1 == handValue2) {
				if (handValue1 > highestBoardValue) {
					rank = "OVER_PAIR";
				} else if (handValue1 > secondHighestBoardValue) {
					rank = "PP_BELOW_TP";
				} else if (handValue1 == secondHighestBoardValue) {
					rank = "MIDDLE_PAIR";
				} else {
					rank = "WEAK_PAIR";
				}
			} else {
				if (handValue1 == highestBoardValue) {
					rank = "TOP_PAIR";
				} else if (handValue1 > highestBoardValue) {
					rank = "OVER_PAIR";
				} else if (handValue1 == secondHighestBoardValue) {
					rank = "MIDDLE_PAIR";
				} else if (handValue1 < highestBoardValue && handValue1 > secondHighestBoardValue) {
					rank = "PP_BELOW_TP";
				} else if (handValue2 == highestBoardValue) {
					rank = "TOP_PAIR";
				} else if (handValue2 > highestBoardValue) {
					rank = "OVER_PAIR";
				} else if (handValue2 == secondHighestBoardValue) {
					rank = "MIDDLE_PAIR";
				} else if (handValue2 < highestBoardValue && handValue2 > secondHighestBoardValue) {
					rank = "PP_BELOW_TP";
				} else {
					rank = "NO_MADE_HAND";
				}
			}

			return rank;
		}
		return null;

	}

	private boolean flushDraw(List<String> cards) {
		return getSuitCount(cards).containsValue(4);
	}

	private boolean straightGutshot(List<String> cards) {
		List<Integer> ranks = getRanks(cards);
		ranks.sort(Comparator.naturalOrder());

		int[] rankArray = new int[15];
		for (int rank : ranks) {
			rankArray[rank]++;
		}

		if (escalera(cards)) {
			return false;
		}

		int firstRank = 1;
		for (int i = 1; i <= 14; i++) {
			if (rankArray[i] > 0) {
				firstRank = i;
				break;
			}
		}

		int consecutive = 0;
		int gaps = 0;

		for (int i = firstRank; i <= 14; i++) {
			if (rankArray[i] > 0) {
				consecutive++;
			} else {
				gaps++;
				if (gaps > 1) {
					consecutive = 0;
					gaps = 0;
				}
			}
			if (consecutive >= 4 && gaps == 1) {
				return true;
			}
		}
		return false;
	}

	private boolean straightOpenEnded(List<String> cards) {
		List<Integer> ranks = getRanks(cards);
		ranks.sort(Comparator.naturalOrder());

		int[] rankArray = new int[15];
		for (int rank : ranks) {
			rankArray[rank]++;
		}

		if (escalera(cards)) {
			return false;
		}

		for (int i = 0; i < ranks.size() - 3; i++) {
			if (ranks.get(i + 1) - ranks.get(i) == 1 && ranks.get(i + 2) - ranks.get(i + 1) == 1
					&& ranks.get(i + 3) - ranks.get(i + 2) == 1) {

				if (!(ranks.get(i) == 2 || ranks.get(i + 3) == 14)) {
					return true;
				}
			}
		}

		return false;
	}

	private int maxRank(List<Integer> boardValues) {
		int maxValue = boardValues.get(0);
		for (int value : boardValues) {
			if (value > maxValue) {
				maxValue = value;
			}
		}
		return maxValue;
	}

	private List<Integer> getBoardValues(List<String> board) {
		List<Integer> values = new ArrayList<>();
		for (String card : board) {
			values.add(getRankValue(card.charAt(0)));
		}
		return values;
	}

	private Map<Character, Integer> getRankCount(List<String> cards) {
		Map<Character, Integer> rankCount = new HashMap<>();
		for (String card : cards) {
			char rank = card.charAt(0);
			rankCount.put(rank, rankCount.getOrDefault(rank, 0) + 1);
		}
		return rankCount;
	}

	private Map<Character, Integer> getSuitCount(List<String> cards) {
		Map<Character, Integer> suitCount = new HashMap<>();
		for (String card : cards) {
			char suit = card.charAt(1);
			suitCount.put(suit, suitCount.getOrDefault(suit, 0) + 1);
		}
		return suitCount;
	}

	private List<Integer> getRanks(List<String> cards) {
		List<Integer> ranks = new ArrayList<>();
		for (String card : cards) {
			ranks.add(getRankValue(card.charAt(0)));
		}
		return ranks;
	}

	private int getRankValue(char rank) {
		return "23456789TJQKA".indexOf(rank) + 2;
	}

	private List<String> extraerCartas(String hand) {
		List<String> cartas = new ArrayList<>();
		String[] cartasArray = hand.split(",");

		for (String carta : cartasArray) {
			cartas.add(carta);
		}

		return cartas;
	}

	private int getSegundoMasAlto(List<Integer> values) {
		List<Integer> sortedValues = new ArrayList<>(values);
		sortedValues.sort((a, b) -> b - a);
		return sortedValues.get(1);
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
