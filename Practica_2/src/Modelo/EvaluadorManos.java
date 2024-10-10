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
		if (doblePar(cartas))
			return "TWO_PAIR";

		String tipoPar = par(cartas, board);
		if (!tipoPar.equals("NO_MADE_HAND"))
			return tipoPar;

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

	private boolean doblePar(List<String> cards) {
		Map<Character, Integer> rankCount = getRankCount(cards);
		return Collections.frequency(rankCount.values(), 2) == 2;
	}

	private String par(List<String> handCards, List<String> board) {
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
			} else if (handValue == secondHighestBoardValue) {
				return "MIDDLE_PAIR";
			} else {
				return "NO_MADE_HAND";
			}
		} else if (manoCartas.size() == 2) {
			int handValue1 = getRankValue(manoCartas.get(0).charAt(0));
			int handValue2 = getRankValue(manoCartas.get(1).charAt(0));

			int highestBoardValue = maxRank(boardValues);
			int secondHighestBoardValue = getSegundoMasAlto(boardValues);

			String rank = "NO_MADE_HAND";

			if (handValue1 == handValue2) {// Tengo pareja en mano
				if (handValue1 > highestBoardValue) {
					rank = "OVER_PAIR";
				} else if (handValue1 > secondHighestBoardValue) {
					rank = "PP_BELOW_TP";
				} else {
					rank = "WEAK_PAIR";
				}
			} else {// No tengo pareja en mano
				if (handValue1 == highestBoardValue || handValue2 == highestBoardValue) {
					rank = "TOP_PAIR";
				} else if (handValue1 == secondHighestBoardValue || handValue2 == secondHighestBoardValue) {
					rank = "MIDDLE_PAIR";
				} else {
					rank = "WEAK_PAIR";
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

}