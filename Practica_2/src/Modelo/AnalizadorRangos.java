package Modelo;

import java.util.*;

public class AnalizadorRangos {
	private Set<String> rangos;
	private List<String> board;
	private Map<String, Integer> handCombos;
	private int totalCombos;
	private EvaluadorManos evaluador;

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
			todosLosCombos.addAll(combos);
		}

		for (String combo : todosLosCombos) {
			List<String> manoCompleta = new ArrayList<>(board);
			manoCompleta.addAll(Arrays.asList(combo.split(",")));

			String handType = evaluador.evaluarMejorMano(String.join(",", manoCompleta), board);

			if (!usaCartaJugador(combo, handType, board) && !handType.equals("NO_MADE_HAND")) { // Solo contamos los combos que tengan una carta de la mano del jugador involucrada
				handType = "NO_MADE_HAND";
			}
			handCombos.put(handType, handCombos.getOrDefault(handType, 0) + 1);
			
			List<String> draws = evaluador.evaluarDraws(String.join(",", manoCompleta));
			for (String draw : draws) {
				if (usaCartaJugadorEnDraw(combo, draw, board)) {
					handCombos.put(draw, handCombos.getOrDefault(draw, 0) + 1);
				}
			}

			totalCombos++;
		}
	}

	private boolean usaCartaJugador(String combo, String handType, List<String> board) {
		List<String> cartasJugador = Arrays.asList(combo.split(","));
		List<String> manoCompleta = new ArrayList<>(board);
		manoCompleta.addAll(cartasJugador);

		switch (handType) {
		case "STRAIGHT_FLUSH":
		case "FLUSH":
			return tieneFlushConCartaJugador(manoCompleta, cartasJugador);
		case "STRAIGHT":
			return tieneEscaleraConCartaJugador(manoCompleta, cartasJugador);
		case "FOUR_OF_A_KIND":
		case "FULL_HOUSE":
		case "THREE_OF_A_KIND":
		case "TWO_PAIR":
			return tieneCombinacionConCartaJugador(manoCompleta, cartasJugador, handType);
		case "OVER_PAIR":
		case "TOP_PAIR":
		case "PP_BELOW_TP":
		case "MIDDLE_PAIR":
		case "WEAK_PAIR":
			return tieneParConCartaJugador(manoCompleta, cartasJugador, board);
		case "ACE_HIGH":
			return tieneAsAltoConCartaJugador(cartasJugador);
		default:
			return false;
		}
	}

	private boolean usaCartaJugadorEnDraw(String combo, String drawType, List<String> board) {
		List<String> cartasJugador = Arrays.asList(combo.split(","));
		List<String> manoCompleta = new ArrayList<>(board);
		manoCompleta.addAll(cartasJugador);

		switch (drawType) {
		case "DRAW_FLUSH":
			return tieneFlushDrawConCartaJugador(manoCompleta, cartasJugador);
		case "STRAIGHT_OPEN_ENDED":
		case "STRAIGHT_GUTSHOT":
			return tieneEscaleraDrawConCartaJugador(manoCompleta, cartasJugador);
		default:
			return false;
		}
	}

	private boolean tieneFlushConCartaJugador(List<String> manoCompleta, List<String> cartasJugador) {
		Map<Character, Integer> suitCount = new HashMap<>();
		for (String carta : manoCompleta) {
			char suit = carta.charAt(1);
			suitCount.put(suit, suitCount.getOrDefault(suit, 0) + 1);
		}

		for (char suit : suitCount.keySet()) {
			if (suitCount.get(suit) >= 5) {
				for (String carta : cartasJugador) {
					if (carta.charAt(1) == suit) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean tieneEscaleraConCartaJugador(List<String> manoCompleta, List<String> cartasJugador) {
		List<Integer> ranks = new ArrayList<>();
		for (String carta : manoCompleta) {
			ranks.add("23456789TJQKA".indexOf(carta.charAt(0)));
		}
		Collections.sort(ranks);

		for (int i = 0; i <= ranks.size() - 5; i++) {
			if (ranks.get(i + 4) - ranks.get(i) == 4) {
				List<Integer> escaleraRanks = ranks.subList(i, i + 5);
				for (String carta : cartasJugador) {
					if (escaleraRanks.contains("23456789TJQKA".indexOf(carta.charAt(0)))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean tieneCombinacionConCartaJugador(List<String> manoCompleta, List<String> cartasJugador,String handType) {
		
		Map<Character, List<String>> rankGroups = new HashMap<>();
		for (String carta : manoCompleta) {
			char rank = carta.charAt(0);
			if (!rankGroups.containsKey(rank)) {
				rankGroups.put(rank, new ArrayList<>());
			}
			rankGroups.get(rank).add(carta);
		}

		List<List<String>> combinacionRelevante = new ArrayList<>();
		switch (handType) {
		case "FOUR_OF_A_KIND":
			for (List<String> group : rankGroups.values()) {
				if (group.size() == 4) {
					combinacionRelevante.add(group);
				}
			}
			break;
		case "FULL_HOUSE":
		case "THREE_OF_A_KIND":
			for (List<String> group : rankGroups.values()) {
				if (group.size() >= 3) {
					combinacionRelevante.add(group);
				}
			}
			break;
		case "TWO_PAIR":
			for (List<String> group : rankGroups.values()) {
				if (group.size() >= 2 && combinacionRelevante.size() < 2) {
					combinacionRelevante.add(group);
				}
			}
			break;
		}

		for (List<String> grupo : combinacionRelevante) {
			for (String carta : grupo) {
				if (cartasJugador.contains(carta)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean tieneParConCartaJugador(List<String> manoCompleta, List<String> cartasJugador, List<String> board) {
		Map<Character, List<String>> rankGroups = new HashMap<>();
		for (String carta : manoCompleta) {
			char rank = carta.charAt(0);
			if (!rankGroups.containsKey(rank)) {
				rankGroups.put(rank, new ArrayList<>());
			}
			rankGroups.get(rank).add(carta);
		}

		for (List<String> group : rankGroups.values()) {
			if (group.size() == 2) {
				for (String carta : group) {
					if (cartasJugador.contains(carta) && !board.containsAll(group)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean tieneAsAltoConCartaJugador(List<String> cartasJugador) {
		for (String carta : cartasJugador) {
			if (carta.charAt(0) == 'A') {
				return true;
			}
		}
		return false;
	}

	private boolean tieneFlushDrawConCartaJugador(List<String> manoCompleta, List<String> cartasJugador) {
		Map<Character, List<String>> suitGroups = new HashMap<>();
		for (String carta : manoCompleta) {
			char suit = carta.charAt(1);
			if (!suitGroups.containsKey(suit)) {
				suitGroups.put(suit, new ArrayList<>());
			}
			suitGroups.get(suit).add(carta);
		}

		for (List<String> flushDraw : suitGroups.values()) {
			if (flushDraw.size() == 4) {
				for (String carta : cartasJugador) {
					if (flushDraw.contains(carta)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean tieneEscaleraDrawConCartaJugador(List<String> manoCompleta, List<String> cartasJugador) {
		List<Integer> ranks = new ArrayList<>();
		for (String carta : manoCompleta) {
			ranks.add("23456789TJQKA".indexOf(carta.charAt(0)));
		}
		Collections.sort(ranks);

		Set<Integer> uniqueRanks = new HashSet<>(ranks);
		List<Integer> uniqueRankList = new ArrayList<>(uniqueRanks);
		for (int i = 0; i <= uniqueRankList.size() - 4; i++) {
			if (uniqueRankList.get(i + 3) - uniqueRankList.get(i) == 3) {
				List<Integer> drawRanks = uniqueRankList.subList(i, i + 4);
				for (String carta : cartasJugador) {
					if (drawRanks.contains("23456789TJQKA".indexOf(carta.charAt(0)))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private Set<String> generarCombosValidosMano(String rango) {
		Set<String> combos = new HashSet<>();

		List<List<String>> combinacionesRango = generarCombinacionesRango(rango);
		List<List<String>> combinacionesFiltradas = filtrarCombinacionesDelBoard(combinacionesRango, board);

		for (List<String> cartasFiltradas : combinacionesFiltradas) {
			combos.add(String.join(",", cartasFiltradas));
		}

		return combos;
	}

	private List<List<String>> filtrarCombinacionesDelBoard(List<List<String>> combinaciones, List<String> board) {
		List<List<String>> filtradas = new ArrayList<>();
		for (List<String> combinacion : combinaciones) {
			boolean match = false;
			for (String carta : combinacion) {
				if (board.contains(carta)) {
					match = true;
					break;
				}
			}
			if (!match) {
				filtradas.add(combinacion);
			}
		}
		return filtradas;
	}

	private List<List<String>> generarCombinacionesRango(String hand) {
		List<List<String>> combinaciones = new ArrayList<>();
		String[] palos = { "h", "c", "d", "s" };

		if (hand.length() == 2) {
			for (int i = 0; i < palos.length; i++) {
				for (int j = i + 1; j < palos.length; j++) {
					List<String> combo = new ArrayList<>();
					combo.add(hand.charAt(0) + palos[i]);
					combo.add(hand.charAt(0) + palos[j]);
					combinaciones.add(combo);
				}
			}
		} else if (hand.length() == 3) {
			char carta1 = hand.charAt(0);
			char carta2 = hand.charAt(1);
			char tipo = hand.charAt(2);

			if (tipo == 's') {
				for (String palo : palos) {
					List<String> combo = new ArrayList<>();
					combo.add(carta1 + palo);
					combo.add(carta2 + palo);
					combinaciones.add(combo);
				}
			} else if (tipo == 'o') {
				for (int i = 0; i < palos.length; i++) {
					for (int j = 0; j < palos.length; j++) {
						if (i != j) {
							List<String> combo = new ArrayList<>();
							combo.add(carta1 + palos[i]);
							combo.add(carta2 + palos[j]);
							combinaciones.add(combo);
						}
					}
				}
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
