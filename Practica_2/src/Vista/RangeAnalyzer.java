package Vista;

import java.util.*;

public class RangeAnalyzer {
    private Set<String> range;
    private List<String> board;
    private Map<String, Integer> handCombos;
    private int totalCombos;

    public RangeAnalyzer(Set<String> range, List<String> board) {
        this.range = range;
        this.board = board;
        this.handCombos = new LinkedHashMap<>(); // Use LinkedHashMap to maintain order
        this.totalCombos = 0;
        initializeHandTypes();
        analyzeRange();
    }

    private void initializeHandTypes() {
        String[] handTypes = {
            "STRAIGHT_FLUSH", "FOUR_OF_A_KIND", "FULL_HOUSE", "FLUSH", "STRAIGHT",
            "THREE_OF_A_KIND", "TWO_PAIR", "OVER_PAIR", "TOP_PAIR", "PP_BELOW_TP",
            "MIDDLE_PAIR", "WEAK_PAIR", "PAIR", "ACE_HIGH", "NO_MADE_HAND",
            "STR_FLUSH_OPEN_ENDED", "STR_FLUSH_GUTSHOT", "DRAW_FLUSH",
            "STRAIGHT_OPEN_ENDED", "STRAIGHT_GUTSHOT"
        };
        for (String handType : handTypes) {
            handCombos.put(handType, 0);
        }
    }

    private void analyzeRange() {
        for (String hand : range) {
            if (isValidHand(hand)) {
                String bestHand = evaluateBestHand(hand);
                handCombos.put(bestHand, handCombos.get(bestHand) + 1);
                totalCombos++;
            }
        }
    }

    private boolean isValidHand(String hand) {
        // Implement logic to check if the hand is valid given the board
        // This should remove hands that conflict with board cards
        return true; // Placeholder
    }

    private String evaluateBestHand(String hand) {
        // Implement logic to evaluate the best possible hand
        // This should consider all hand types and special cases for pairs
        return "NO_MADE_HAND"; // Placeholder
    }

    public Map<String, Double> getHandProbabilities() {
        Map<String, Double> probabilities = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : handCombos.entrySet()) {
            if (entry.getValue() > 0) {
                double probability = (double) entry.getValue() / totalCombos * 100;
                probabilities.put(entry.getKey(), Math.round(probability * 10.0) / 10.0);
            }
        }
        return probabilities;
    }

    public int getTotalCombos() {
        return totalCombos;
    }
}