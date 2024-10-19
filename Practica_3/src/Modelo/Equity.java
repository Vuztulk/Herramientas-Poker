package Modelo;

import java.util.*;

public class Equity {
    private List<List<String>> playerCards;
    private List<String> boardCards;
    private static final int SIMULATIONS = 10000;

    public Equity(List<List<String>> playerCards, List<String> boardCards) {
        this.playerCards = playerCards;
        this.boardCards = boardCards;
    }

    public List<List<String>> calculateEquity() {
        int[] wins = new int[playerCards.size()];
        int[] ties = new int[playerCards.size()];

        for (int i = 0; i < SIMULATIONS; i++) {
            List<String> deck = generateDeck();
            List<String> simulatedBoard = new ArrayList<>(boardCards);
            
            while (simulatedBoard.size() < 5) {
                simulatedBoard.add(drawCard(deck));
            }

            int winner = evaluateHands(simulatedBoard);
            if (winner == -1) {
                for (int j = 0; j < ties.length; j++) {
                    ties[j]++;
                }
            } else {
                wins[winner]++;
            }
        }

        List<List<String>> equity = new ArrayList<>();
        for (int i = 0; i < playerCards.size(); i++) {
            double equityPercentage = (wins[i] + (ties[i] / playerCards.size())) * 100.0 / SIMULATIONS;
            equity.add(Arrays.asList(String.format("%.2f", equityPercentage) + "%"));
        }

        return equity;
    }

    private List<String> generateDeck() {
        List<String> deck = new ArrayList<>();
        String[] suits = {"h", "d", "c", "s"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};

        for (String suit : suits) {
            for (String rank : ranks) {
                String card = rank + suit;
                if (!boardCards.contains(card) && !isCardUsedByPlayers(card)) {
                    deck.add(card);
                }
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

    private boolean isCardUsedByPlayers(String card) {
        for (List<String> playerHand : playerCards) {
            if (playerHand.contains(card)) {
                return true;
            }
        }
        return false;
    }

    private String drawCard(List<String> deck) {
        return deck.remove(deck.size() - 1);
    }

    private int evaluateHands(List<String> board) {
        int bestHandValue = -1;
        int winner = -1;
        boolean tie = false;

        for (int i = 0; i < playerCards.size(); i++) {
            List<String> hand = new ArrayList<>(playerCards.get(i));
            hand.addAll(board);
            int handValue = evaluateHand(hand);

            if (handValue > bestHandValue) {
                bestHandValue = handValue;
                winner = i;
                tie = false;
            } else if (handValue == bestHandValue) {
                tie = true;
            }
        }

        return tie ? -1 : winner;
    }

    private int evaluateHand(List<String> hand) {
        Map<Character, Integer> rankCount = new HashMap<>();
        for (String card : hand) {
            char rank = card.charAt(0);
            rankCount.put(rank, rankCount.getOrDefault(rank, 0) + 1);
        }

        int pairs = 0;
        boolean hasThreeOfAKind = false;
        boolean hasFourOfAKind = false;

        for (int count : rankCount.values()) {
            if (count == 2) pairs++;
            if (count == 3) hasThreeOfAKind = true;
            if (count == 4) hasFourOfAKind = true;
        }

        if (hasFourOfAKind) return 7;
        if (hasThreeOfAKind && pairs == 1) return 6;
        if (hasThreeOfAKind) return 5;
        if (pairs == 2) return 4;
        if (pairs == 1) return 3;
        return 2; 
    }
}