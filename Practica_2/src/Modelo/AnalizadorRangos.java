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
				"NO_MADE_HAND", "DRAW_FLUSH", "STRAIGHT_OPEN_ENDED",
				"STRAIGHT_GUTSHOT" };
		for (String handType : handTypes) {
			handCombos.put(handType, 0);
		}
	}

	private void analizarRango() {
        for (String hand : range) {
            Set<String> combos = generarCombosValidosMano(hand);
            for (String combo : combos) {
                String handType = evaluarMejorMano(combo);
                handCombos.put(handType, handCombos.get(handType) + 1);
                
                List<String> draws = evaluarDraws(combo);
                for (String draw : draws) {
                    handCombos.put(draw, handCombos.get(draw) + 1);
                }
                
                totalCombos++;
            }
        }
    }

	private Set<String> generarCombosValidosMano(String hand) {
		Set<String> combos = new HashSet<>();
		Set<String> boardCards = new HashSet<>(board);

		if (hand.length() == 2) { // Pocket pair
			char rank = hand.charAt(0);
			for (int i = 0; i < SUITS.length; i++) {
				for (int j = i + 1; j < SUITS.length; j++) {
					String card1 = "" + rank + SUITS[i];
					String card2 = "" + rank + SUITS[j];
					if (!boardCards.contains(card1) && !boardCards.contains(card2)) {
						combos.add(card1 + card2);
					}
				}
			}
		} 
		else if (hand.length() == 3) {
			char rank1 = hand.charAt(0);
			char rank2 = hand.charAt(1);
			char type = hand.charAt(2);
			if (type == 's') { // Suited
				for (char suit : SUITS) {
					String card1 = "" + rank1 + suit;
					String card2 = "" + rank2 + suit;
					if (!boardCards.contains(card1) && !boardCards.contains(card2)) {
						combos.add(card1 + card2);
					}
				}
			} 
			else if (type == 'o') { // Offsuit
				for (char suit1 : SUITS) {
					for (char suit2 : SUITS) {
						if (suit1 != suit2) {
							String card1 = "" + rank1 + suit1;
							String card2 = "" + rank2 + suit2;
							if (!boardCards.contains(card1) && !boardCards.contains(card2)) {
								combos.add(card1 + card2);
							}
						}
					}
				}
			}
		}
		return combos;
	}

	private String evaluarMejorMano(String hand) {
        List<String> cartas = new ArrayList<>(board);
        cartas.add(hand.substring(0, 2));
        cartas.add(hand.substring(2, 4));

        if (escaleraColor(cartas)) return "STRAIGHT_FLUSH";
        if (poker(cartas)) return "FOUR_OF_A_KIND";
        if (full(cartas)) return "FULL_HOUSE";
        if (color(cartas)) return "FLUSH";
        if (escalera(cartas)) return "STRAIGHT";
        if (trio(cartas)) return "THREE_OF_A_KIND";
        if (doblePar(cartas)) return "TWO_PAIR";

        String tipoPar = par(hand, board);
        if (!tipoPar.equals("NO_MADE_HAND")) return tipoPar;

        if (hand.contains("A")) return "ACE_HIGH";

        return "NO_MADE_HAND";
    }

	private List<String> evaluarDraws(String hand) {
        List<String> cartas = new ArrayList<>(board);
        cartas.add(hand.substring(0, 2));
        cartas.add(hand.substring(2, 4));
        
        List<String> draws = new ArrayList<>();
        
        if (flushDraw(cartas)) draws.add("DRAW_FLUSH");
        if (straightOpenEnded(cartas)) draws.add("STRAIGHT_OPEN_ENDED");
        if (straightGutshot(cartas)) draws.add("STRAIGHT_GUTSHOT");
        
        return draws;
    }
	
	private boolean escaleraColor(List<String> cards) {
		return color(cards) && escalera(cards);
	}

	private boolean poker(List<String> cards) {
		Map<Character, Integer> rankCount = getRankCount(cards);
		return rankCount.containsValue(4);
	}

	private boolean full(List<String> cards) {
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

	private boolean doblePar(List<String> cards) {
		Map<Character, Integer> rankCount = getRankCount(cards);
		return Collections.frequency(rankCount.values(), 2) == 2;
	}

	private String par(String hand, List<String> board) {
		int handValue1 = getRankValue(hand.charAt(0));
		int handValue2 = getRankValue(hand.charAt(2));
		List<Integer> boardValues = getBoardValues(board);

		int highestBoardValue = maxRank(boardValues);
		int secondHighestBoardValue = getSegundoMasAlto(boardValues);

		// Si hay pareja en la mano
		if (handValue1 == handValue2) {
			if (handValue1 > highestBoardValue) {
				return "OVER_PAIR";
			} else if (handValue1 == highestBoardValue) {
				return "TOP_PAIR";
			} else if (handValue1 >= secondHighestBoardValue) {
				return "MIDDLE_PAIR";
			} else {
				return "WEAK_PAIR";
			}
		} else {// No hay pareja en la mano y verificamos si hay pareja en el board
			int handHighCount = 0;
			int handLowCount = 0;

			for (int boardValue : boardValues) {
				if (boardValue == handValue1)
					handHighCount++;
				if (boardValue == handValue2)
					handLowCount++;
			}

			if (handHighCount > 0) {// Carta mas alta de la mano
				if (handValue1 > highestBoardValue) {
					return "OVER_PAIR";
				} else if (handValue1 == highestBoardValue) {
					return "TOP_PAIR";
				} else if (handValue1 == secondHighestBoardValue) {
					return "MIDDLE_PAIR";
				} else {
					return "WEAK_PAIR";
				}
			} else if (handLowCount > 0) {// Segunda carta de la mano
				if (handValue2 > highestBoardValue) {
					return "OVER_PAIR";
				} else if (handValue2 == highestBoardValue) {
					return "TOP_PAIR";
				} else if (handValue2 == secondHighestBoardValue) {
					return "MIDDLE_PAIR";
				} else if (handValue2 < highestBoardValue && handValue1 < highestBoardValue) {
					return "PP_BELOW_TP";
				} else {
					return "WEAK_PAIR";
				}
			} else {
				return "NO_MADE_HAND";
			}
		}
	}
	
	private boolean flushDraw(List<String> cards) {
        Map<Character, Integer> suitCount = new HashMap<>();
        for (String card : cards) {
            char suit = card.charAt(1);
            suitCount.put(suit, suitCount.getOrDefault(suit, 0) + 1);
        }
        return suitCount.containsValue(4);
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
            if (ranks.get(i + 1) - ranks.get(i) == 1 &&
                ranks.get(i + 2) - ranks.get(i + 1) == 1 &&
                ranks.get(i + 3) - ranks.get(i + 2) == 1) {

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
