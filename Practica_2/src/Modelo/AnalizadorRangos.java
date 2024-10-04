package Modelo;

import java.util.*;

public class AnalizadorRangos {
	private Set<String> range;
	private List<String> board;
	private Map<String, Integer> handCombos;
	private int totalCombos;

	private static final char[] SUITS = { 'h', 'd', 'c', 's' };

	public AnalizadorRangos(Set<String> range, List<String> board) {
		this.range = range;
		this.board = board;
		this.handCombos = new LinkedHashMap<>();
		this.totalCombos = 0;
		inicializarTipoManos();
		analizarRango();
	}

	private void inicializarTipoManos() {
		String[] handTypes = { "STRAIGHT_FLUSH", "FOUR_OF_A_KIND", "FULL_HOUSE", "FLUSH", "STRAIGHT", "THREE_OF_A_KIND",
				"TWO_PAIR", "OVER_PAIR", "TOP_PAIR", "PP_BELOW_TP", "MIDDLE_PAIR", "WEAK_PAIR", "ACE_HIGH",
				"NO_MADE_HAND", "STR_FLUSH_OPEN_ENDED", "STR_FLUSH_GUTSHOT", "DRAW_FLUSH", "STRAIGHT_OPEN_ENDED",
				"STRAIGHT_GUTSHOT" };
		for (String handType : handTypes) {
			handCombos.put(handType, 0);
		}
	}

	private void analizarRango() {
		Set<String> combos = generarCombosRango(range);
		for (String hand : combos) {
			if (verificarMano(hand)) {
				String bestHand = evaluarMejorMano(hand);
				handCombos.put(bestHand, handCombos.get(bestHand) + 1);
				totalCombos++;
			}
		}
	}

	// Generar todas las combinaciones posibles a partir del rango
	private Set<String> generarCombosRango(Set<String> range) {
		Set<String> combos = new HashSet<>();
		for (String hand : range) {
			combos.addAll(generarCombosMano(hand));
		}
		return combos;
	}

	private Set<String> generarCombosMano(String hand) {
		Set<String> combos = new HashSet<>();
		if (hand.length() == 3 && hand.charAt(2) == 's') {// Mano suited
			char rank1 = hand.charAt(0);
			char rank2 = hand.charAt(1);
			for (char suit : SUITS) {
				String card1 = "" + rank1 + suit;
				String card2 = "" + rank2 + suit;
				combos.add(card1 + card2);
			}
		} else if (hand.length() == 3 && hand.charAt(2) == 'o') {// Mano offsuited
			char rank1 = hand.charAt(0);
			char rank2 = hand.charAt(1);
			for (char suit1 : SUITS) {
				for (char suit2 : SUITS) {
					if (suit1 != suit2) {
						String card1 = "" + rank1 + suit1;
						String card2 = "" + rank2 + suit2;
						combos.add(card1 + card2);
					}
				}
			}
		} else if (hand.length() == 2) {// Parejas
			char rank = hand.charAt(0);
			for (int i = 0; i < SUITS.length; i++) {
				for (int j = i + 1; j < SUITS.length; j++) {
					String card1 = "" + rank + SUITS[i];
					String card2 = "" + rank + SUITS[j];
					combos.add(card1 + card2);
				}
			}
		}
		return combos;
	}


	private boolean verificarMano(String hand) {// Verifica que el combo no comparta cartas con el board
		List<String> allCards = new ArrayList<>(board);
		String card1 = hand.substring(0, 2);
		String card2 = hand.substring(2, 4);

		// Verificar que las cartas no estén en el board
		if (board.contains(card1) || board.contains(card2)) {
			return false;
		}

		allCards.add(card1);
		allCards.add(card2);

		Set<String> uniqueCards = new HashSet<>(allCards);
		return uniqueCards.size() == allCards.size(); // No hay duplicados
	}

	private String evaluarMejorMano(String hand) {
		List<String> cartas = new ArrayList<>(board);
		String carta1 = hand.substring(0, 2);
		String carta2 = hand.substring(2, 4);
		cartas.add(carta1);
		cartas.add(carta2);

		if (escaleraColor(cartas))
			return "STRAIGHT_FLUSH";
		if (poker(cartas))
			return "FOUR_OF_A_KIND";
		if (house(cartas))
			return "FULL_HOUSE";
		if (color(cartas))
			return "FLUSH";
		if (escalera(cartas))
			return "STRAIGHT";
		if (trio(cartas))
			return "THREE_OF_A_KIND";
		if (par(cartas))
			return "TWO_PAIR";

		String tipoPar = evaluarTipoPar(hand, board);
		if (!tipoPar.equals("NO_MADE_HAND"))
			return tipoPar;

		if (contieneAs(hand))
			return "ACE_HIGH";

		if (straightFlushDraw(cartas))
			return "STR_FLUSH_OPEN_ENDED";
		if (straightFlushGutshot(cartas))
			return "STR_FLUSH_GUTSHOT";
		if (flushDraw(cartas))
			return "DRAW_FLUSH";
		if (straightOpenEnded(cartas))
			return "STRAIGHT_OPEN_ENDED";
		if (straightGutshot(cartas))
			return "STRAIGHT_GUTSHOT";

		return "NO_MADE_HAND";
	}

	private boolean escaleraColor(List<String> cards) {
		return color(cards) && escalera(cards);
	}

	private boolean poker(List<String> cards) {
		Map<Character, Integer> rankCount = getRankCount(cards);
		return rankCount.containsValue(4);
	}

	private boolean house(List<String> cards) {
		Map<Character, Integer> rankCount = getRankCount(cards);
		return rankCount.containsValue(3) && rankCount.containsValue(2);
	}

	private boolean color(List<String> cards) {
		Map<Character, Integer> suitCount = getSuitCount(cards);
		return suitCount.containsValue(5);
	}

	private boolean escalera(List<String> cards) {
		List<Integer> ranks = getRanks(cards);
		Collections.sort(ranks);
		for (int i = 0; i < ranks.size() - 4; i++) {
			if (ranks.get(i + 4) - ranks.get(i) == 4)
				return true;
		}
		return false;
	}

	private boolean trio(List<String> cards) {
		Map<Character, Integer> rankCount = getRankCount(cards);
		return rankCount.containsValue(3);
	}

	private boolean par(List<String> cards) {
		Map<Character, Integer> rankCount = getRankCount(cards);
		return Collections.frequency(rankCount.values(), 2) == 2;
	}

	private String evaluarTipoPar(String hand, List<String> board) {
		char handRank1 = hand.charAt(0);
		char handRank2 = hand.charAt(2);
		List<Character> boardRanks = getBoardRanks(board);

		if (handRank1 == handRank2) {
			if (getRankValue(handRank1) > getRankValue(Collections.max(boardRanks)))
				return "OVER_PAIR";
			if (getRankValue(handRank1) < getRankValue(Collections.max(boardRanks)) && getRankValue(handRank1) > getRankValue(Collections.min(boardRanks)))
				return "PP_BELOW_TP";
			
			return "WEAK_PAIR";
		}

		if (boardRanks.contains(handRank1) || boardRanks.contains(handRank2)) {
			char pairRank = boardRanks.contains(handRank1) ? handRank1 : handRank2;
			if (pairRank == Collections.max(boardRanks))
				return "TOP_PAIR";
			if (pairRank == getSengundoMasAlto(boardRanks))
				return "MIDDLE_PAIR";
			return "WEAK_PAIR";
		}

		return "NO_MADE_HAND";
	}

	private boolean contieneAs(String hand) {
		return hand.contains("A");
	}

	private boolean straightFlushDraw(List<String> cards) {
		Map<Character, List<Integer>> suitedRanks = getSuitedRanks(cards);
		for (List<Integer> ranks : suitedRanks.values()) {
			if (ranks.size() >= 4) {
				Collections.sort(ranks);
				for (int i = 0; i < ranks.size() - 3; i++) {
					if (ranks.get(i + 3) - ranks.get(i) == 4)
						return true;
				}
			}
		}
		return false;
	}

	private boolean straightFlushGutshot(List<String> cards) {
		Map<Character, List<Integer>> suitedRanks = getSuitedRanks(cards);
		for (List<Integer> ranks : suitedRanks.values()) {
			if (ranks.size() >= 4) {
				Collections.sort(ranks);
				for (int i = 0; i < ranks.size() - 3; i++) {
					if (ranks.get(i + 3) - ranks.get(i) == 5 && !ranks.contains(ranks.get(i) + 2)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean flushDraw(List<String> cards) {
		Map<Character, Integer> suitCount = getSuitCount(cards);
		return suitCount.containsValue(4);
	}

	private boolean straightOpenEnded(List<String> cards) {
		List<Integer> ranks = getRanks(cards);
		Collections.sort(ranks);
		for (int i = 0; i < ranks.size() - 3; i++) {
			if (ranks.get(i + 3) - ranks.get(i) == 3) {
				if (ranks.get(i) > 0 || ranks.get(i + 3) < 12) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean straightGutshot(List<String> cards) {
		List<Integer> ranks = getRanks(cards);
		Collections.sort(ranks);
		for (int i = 0; i < ranks.size() - 3; i++) {
			if (ranks.get(i + 3) - ranks.get(i) == 4) {
				int missingRank = ranks.get(i) + 1;
				if (!ranks.contains(missingRank) && missingRank >= 0 && missingRank <= 12) {
					return true;
				}
			}
		}
		if (ranks.contains(12) && ranks.contains(0) && ranks.contains(1) && ranks.contains(2) && !ranks.contains(3)) {
			return true;
		}
		return false;
	}

	private Map<Character, List<Integer>> getSuitedRanks(List<String> cards) {
		Map<Character, List<Integer>> suitedRanks = new HashMap<>();
		for (String card : cards) {
			char suit = card.charAt(1);
			int rank = getRankValue(card.charAt(0));
			suitedRanks.computeIfAbsent(suit, k -> new ArrayList<>()).add(rank);
		}
		return suitedRanks;
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

	private List<Character> getBoardRanks(List<String> board) {
		List<Character> ranks = new ArrayList<>();
		for (String card : board) {
			ranks.add(card.charAt(0));
		}
		return ranks;
	}

	private int getRankValue(char rank) {
		return "23456789TJQKA".indexOf(rank);
	}

	private char getSengundoMasAlto(List<Character> ranks) {
		List<Character> sortedRanks = new ArrayList<>(ranks);
		sortedRanks.sort((a, b) -> getRankValue(b) - getRankValue(a));
		return sortedRanks.get(1);
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

	public int getCombosTotales() {
		return totalCombos;
	}

}
