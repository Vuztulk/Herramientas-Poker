package Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Model {
	Equity equity;

	public Model() {

	}

	private static final String[] SUITS = { "s", "h", "d", "c" };
	private static final String[] RANKS = { "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A" };

	public List<String> generateRandomBoard() {
		List<String> deck = generateDeck();
		Collections.shuffle(deck);
		return deck.subList(0, 5);
	}

	public List<List<String>> generateRandomPlayerCards(List<String> usedCards, int num_cards) {
		List<String> deck = generateDeck();
		deck.removeAll(usedCards);
		Collections.shuffle(deck);
		List<List<String>> playerCards = new ArrayList<>();

		for (int i = 0; i < 6; i++) {
			List<String> playerHand = new ArrayList<>();

			for (int j = 0; j < num_cards; j++) {
				if (!deck.isEmpty()) {
					playerHand.add(deck.remove(0));
				} else {
					throw new IllegalStateException(
							"No hay suficientes cartas en el mazo para generar todas las manos");
				}
			}

			playerCards.add(playerHand);
		}

		return playerCards;
	}

	private List<String> generateDeck() {
		List<String> deck = new ArrayList<>();
		for (String suit : SUITS) {
			for (String rank : RANKS) {
				deck.add(rank + suit);
			}
		}
		return deck;
	}

	public List<String> getEquity(List<List<String>> listaCartasJugadores, List<String> listaCartasBoard,
			List<Boolean> activePlayers, boolean type_game) {

		List<List<String>> activePlayerCards = new ArrayList<>();
		List<List<String>> foldedPlayerCards = new ArrayList<>();

		for (int i = 0; i < listaCartasJugadores.size(); i++) {
			if (activePlayers.get(i)) {
				activePlayerCards.add(listaCartasJugadores.get(i));
			} else {
				foldedPlayerCards.add(listaCartasJugadores.get(i));
			}
		}

		List<String> deadCards = new ArrayList<>();
		for (List<String> foldedHand : foldedPlayerCards) {
			deadCards.addAll(foldedHand);
		}

		equity = new Equity(activePlayerCards, listaCartasBoard, deadCards, type_game);
		List<String> activeEquities = equity.calculateEquity();

		List<String> equitys = new ArrayList<>();
		int active = 0;
		for (int i = 0; i < listaCartasJugadores.size(); i++) {
			if (activePlayers.get(i)) {
				equitys.add(activeEquities.get(active));
				active++;
			} else {
				equitys.add("0.00");
			}
		}

		return equitys;
	}

}