package Modelo;

import java.util.*;

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
		for (String rango : rangos) {
			Set<String> combos = generarCombosValidosMano(rango);
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
		Set<String> manoGenerada = new HashSet<>();
		Set<String> cartasDisponibles = new HashSet<>();
		
		int combosDisponibles = calcularCombosDisponibles(hand, boardCards);

		if (hand.length() == 2) {
			char rank = hand.charAt(0);
			for (int i = 0; i < SUITS.length; i++) {
				for (int j = i + 1; j < SUITS.length; j++) {
					String card1 = "" + rank + SUITS[i];
					String card2 = "" + rank + SUITS[j];
					if (!boardCards.contains(card1) && !boardCards.contains(card2)) {
						manoGenerada.add(card1 + card2);
						cartasDisponibles.add(card1);
						cartasDisponibles.add(card2);
					}
				}
			}
		} else if (hand.length() == 3) {
			char rank1 = hand.charAt(0);
			char rank2 = hand.charAt(1);
			char type = hand.charAt(2);
			if (type == 's') {
				for (char suit : SUITS) {
					String card1 = "" + rank1 + suit;
					String card2 = "" + rank2 + suit;
					if (!boardCards.contains(card1) && !boardCards.contains(card2)) {
						manoGenerada.add(card1 + card2);
						cartasDisponibles.add(card1);
						cartasDisponibles.add(card2);
					}
				}
			} else if (type == 'o') {
				for (char suit1 : SUITS) {
					for (char suit2 : SUITS) {
						if (suit1 != suit2) {
							String card1 = "" + rank1 + suit1;
							String card2 = "" + rank2 + suit2;
							if (!boardCards.contains(card1) && !boardCards.contains(card2)) {
								manoGenerada.add(card1 + card2);
								cartasDisponibles.add(card1);
								cartasDisponibles.add(card2);
							}
						}
					}
				}
			}
		}

		if (combosDisponibles > 0) {
			if (board.size() == 3) {
				for (String mano : manoGenerada) {
					List<String> comboCompleto = new ArrayList<>(board);
					comboCompleto.add(mano.substring(0, 2));
					comboCompleto.add(mano.substring(2, 4));
					combos.add(String.join(",", comboCompleto));
				}
			}
			else {
				List<List<String>> boardCombinaciones3 = generarCombinaciones(board, 3);
				for (List<String> boardCombo : boardCombinaciones3) {
					for (String mano : manoGenerada) {
						List<String> comboCompleto = new ArrayList<>(boardCombo);
						comboCompleto.add(mano.substring(0, 2));
						comboCompleto.add(mano.substring(2, 4));
						combos.add(String.join(",", comboCompleto));
					}
				}
				List<List<String>> boardCombinaciones4 = generarCombinaciones(board, 4);
				for (List<String> boardCombo : boardCombinaciones4) {
					for (String mano : cartasDisponibles) {
						List<String> comboCompleto = new ArrayList<>(boardCombo);
						comboCompleto.add(mano.substring(0, 2));
						combos.add(String.join(",", comboCompleto));
					}
				}
			}
		}

		return combos;
	}

	private int calcularCombosDisponibles(String hand, Set<String> boardCards) {
		if (hand.length() == 2) {
			char rank = hand.charAt(0);
			int cartasBoard = contarCartasEnBoard(rank, boardCards);
			if (cartasBoard == 0)
				return 6;
			if (cartasBoard == 1)
				return 3;
			if (cartasBoard == 2)
				return 1;
			return 0;
		} else if (hand.length() == 3 && hand.charAt(2) == 's') {
			char rank1 = hand.charAt(0);
			char rank2 = hand.charAt(1);
			int cartasBoardRank1 = contarCartasEnBoard(rank1, boardCards);
			int cartasBoardRank2 = contarCartasEnBoard(rank2, boardCards);
			if (cartasBoardRank1 == 0 && cartasBoardRank2 == 0)
				return 4;
			if (cartasBoardRank1 > 0 || cartasBoardRank2 > 0)
				return 3;
			return 0;
		} else if (hand.length() == 3 && hand.charAt(2) == 'o') {
			char rank1 = hand.charAt(0);
			char rank2 = hand.charAt(1);
			int cartasBoardRank1 = contarCartasEnBoard(rank1, boardCards);
			int cartasBoardRank2 = contarCartasEnBoard(rank2, boardCards);
			if (cartasBoardRank1 == 0 && cartasBoardRank2 == 0)
				return 12;
			if (cartasBoardRank1 > 0 || cartasBoardRank2 > 0)
				return 9;
			return 0;
		}
		return 0;
	}

	private int contarCartasEnBoard(char rank, Set<String> boardCards) {
		int count = 0;
		for (String card : boardCards) {
			if (card.charAt(0) == rank) {
				count++;
			}
		}
		return count;
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
	            } else if (handValue1 == highestBoardValue) {
	                rank = "TOP_PAIR";
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
