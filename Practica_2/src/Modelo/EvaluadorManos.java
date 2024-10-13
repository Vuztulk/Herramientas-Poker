package Modelo;

import java.util.*;

public class EvaluadorManos {

	public String evaluarMejorMano(String hand, List<String> board) {

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

		String resultadoPar = especificarPair(cartas, board);
		if (!resultadoPar.equals("NO_MADE_HAND")) {
			return resultadoPar;
		}

		if (hand.contains("A"))
			return "ACE_HIGH";

		return "NO_MADE_HAND";
	}

	public List<String> evaluarDraws(String hand) {
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

	private String especificarPair(List<String> cartasMano, List<String> cartasBoard) {
		cartasMano.removeAll(cartasBoard);
	    Map<Character, Integer> rankCountMano = getRankCount(cartasMano);
	    Map<Character, Integer> rankCountBoard = getRankCount(cartasBoard);
	    
	    List<Character> paresCombinadosManoBoard = new ArrayList<>();
	    
	    // Buscar pares combinando mano y tablero
	    for (char rankMano : rankCountMano.keySet()) {
	        if (rankCountBoard.containsKey(rankMano)) {
	            paresCombinadosManoBoard.add(rankMano);
	        }
	    }
	    
	    // Verificar doble par
	    if (paresCombinadosManoBoard.size() >= 2) {
	        paresCombinadosManoBoard.sort((a, b) -> Character.compare(b, a));
	        return "TWO_PAIR";
	    }
	    
	    // Caso de pareja simple
	    if (paresCombinadosManoBoard.size() == 1) {
	        return especificarParSimple(paresCombinadosManoBoard.get(0), cartasBoard);
	    }
	    
	    // Verificar si hay un par en la mano
	    for (Map.Entry<Character, Integer> entry : rankCountMano.entrySet()) {
	        if (entry.getValue() == 2) {
	            return especificarParSimple(entry.getKey(), cartasBoard);
	        }
	    }
	    
	    return "NO_MADE_HAND";
	}

	private String especificarParSimple(char parValue, List<String> cartasBoard) {
		List<Character> boardOrdenado = cartasBoard.stream().map(card -> card.charAt(0)).distinct()
				.sorted((a, b) -> compararCartas(b, a)).toList();

		if (compararCartas(parValue, boardOrdenado.get(0)) > 0) {
			return "OVER_PAIR";
		} else if (parValue == boardOrdenado.get(0)) {
			return "TOP_PAIR";
		} else if (boardOrdenado.size() > 1 && compararCartas(parValue, boardOrdenado.get(1)) > 0) {
			return "PP_BELOW_TP";
		} else if (boardOrdenado.size() > 1 && parValue == boardOrdenado.get(1)) {
			return "MIDDLE_PAIR";
		} else {
			return "WEAK_PAIR";
		}
	}

	private int compararCartas(char a, char b) {
		return getRankValue(a) - getRankValue(b);
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

}