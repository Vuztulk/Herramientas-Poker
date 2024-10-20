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

    public List<List<String>> generateRandomPlayerCards(List<String> usedCards, int numPlayers) {
        List<String> deck = generateDeck();
        deck.removeAll(usedCards);
        Collections.shuffle(deck);
        List<List<String>> playerCards = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            List<String> playerHand = new ArrayList<>();
            playerHand.add(deck.remove(0));
            playerHand.add(deck.remove(0));
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
    
    public List<List<String>> getEquity(List<List<String>> listaCartasJugadores, List<String> listaCartasBoard, List<Boolean> activePlayers) {
        List<List<String>> activePlayerCards = new ArrayList<>();
        for (int i = 0; i < listaCartasJugadores.size(); i++) {
            if (activePlayers.get(i)) {
                activePlayerCards.add(listaCartasJugadores.get(i));
            }
        }

        equity = new Equity(activePlayerCards, listaCartasBoard);
        List<List<String>> activeEquities = equity.calculateEquity();

        // Reinsert folded players with 0% equity
        List<List<String>> allEquities = new ArrayList<>();
        int activeIndex = 0;
        for (int i = 0; i < listaCartasJugadores.size(); i++) {
            if (activePlayers.get(i)) {
                allEquities.add(activeEquities.get(activeIndex));
                activeIndex++;
            } else {
                List<String> foldedEquity = new ArrayList<>();
                foldedEquity.add("0.00");
                allEquities.add(foldedEquity);
            }
        }

        return allEquities;
    }
}