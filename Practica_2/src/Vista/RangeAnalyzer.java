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
        List<String> allCards = new ArrayList<>(board);
        allCards.addAll(Arrays.asList(hand.split("")));
        Set<String> uniqueCards = new HashSet<>(allCards);
        return uniqueCards.size() == allCards.size();
    }

    private String evaluateBestHand(String hand) {
        List<String> allCards = new ArrayList<>(board);
        allCards.addAll(Arrays.asList(hand.split("")));
        
        if (isStraightFlush(allCards)) return "STRAIGHT_FLUSH";
        if (isFourOfAKind(allCards)) return "FOUR_OF_A_KIND";
        if (isFullHouse(allCards)) return "FULL_HOUSE";
        if (isFlush(allCards)) return "FLUSH";
        if (isStraight(allCards)) return "STRAIGHT";
        if (isThreeOfAKind(allCards)) return "THREE_OF_A_KIND";
        if (isTwoPair(allCards)) return "TWO_PAIR";
        
        String pairType = evaluatePairType(hand, board);
        if (!pairType.equals("NO_MADE_HAND")) return pairType;
        
        if (hasAceHigh(hand)) return "ACE_HIGH";
        
        // Check for draws
        if (isStraightFlushDraw(allCards)) return "STR_FLUSH_OPEN_ENDED";
        if (isStraightFlushGutshot(allCards)) return "STR_FLUSH_GUTSHOT";
        if (isFlushDraw(allCards)) return "DRAW_FLUSH";
        if (isStraightOpenEnded(allCards)) return "STRAIGHT_OPEN_ENDED";
        if (isStraightGutshot(allCards)) return "STRAIGHT_GUTSHOT";
        
        return "NO_MADE_HAND";
    }

    private boolean isStraightFlush(List<String> cards) {
        return isFlush(cards) && isStraight(cards);
    }

    private boolean isFourOfAKind(List<String> cards) {
        Map<Character, Integer> rankCount = getRankCount(cards);
        return rankCount.containsValue(4);
    }

    private boolean isFullHouse(List<String> cards) {
        Map<Character, Integer> rankCount = getRankCount(cards);
        return rankCount.containsValue(3) && rankCount.containsValue(2);
    }

    private boolean isFlush(List<String> cards) {
        Map<Character, Integer> suitCount = getSuitCount(cards);
        return suitCount.containsValue(5);
    }

    private boolean isStraight(List<String> cards) {
        List<Integer> ranks = getRanks(cards);
        Collections.sort(ranks);
        for (int i = 0; i < ranks.size() - 4; i++) {
            if (ranks.get(i+4) - ranks.get(i) == 4) return true;
        }
        return false;
    }

    private boolean isThreeOfAKind(List<String> cards) {
        Map<Character, Integer> rankCount = getRankCount(cards);
        return rankCount.containsValue(3);
    }

    private boolean isTwoPair(List<String> cards) {
        Map<Character, Integer> rankCount = getRankCount(cards);
        return Collections.frequency(rankCount.values(), 2) == 2;
    }

    private String evaluatePairType(String hand, List<String> board) {
        char handRank1 = hand.charAt(0);
        char handRank2 = hand.charAt(1);
        List<Character> boardRanks = getBoardRanks(board);
        
        if (handRank1 == handRank2) {
            if (getRankValue(handRank1) > getRankValue(Collections.max(boardRanks))) return "OVER_PAIR";
            if (getRankValue(handRank1) < getRankValue(Collections.max(boardRanks)) &&
                getRankValue(handRank1) > getRankValue(Collections.min(boardRanks))) return "PP_BELOW_TP";
            return "WEAK_PAIR";
        }
        
        if (boardRanks.contains(handRank1) || boardRanks.contains(handRank2)) {
            char pairRank = boardRanks.contains(handRank1) ? handRank1 : handRank2;
            if (pairRank == Collections.max(boardRanks)) return "TOP_PAIR";
            if (pairRank == getSecondHighestRank(boardRanks)) return "MIDDLE_PAIR";
            return "WEAK_PAIR";
        }
        
        return "NO_MADE_HAND";
    }

    private boolean hasAceHigh(String hand) {
        return hand.contains("A");
    }

    private boolean isStraightFlushDraw(List<String> cards) {
        Map<Character, List<Integer>> suitedRanks = getSuitedRanks(cards);
        for (List<Integer> ranks : suitedRanks.values()) {
            if (ranks.size() >= 4) {
                Collections.sort(ranks);
                for (int i = 0; i < ranks.size() - 3; i++) {
                    if (ranks.get(i+3) - ranks.get(i) == 4) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isStraightFlushGutshot(List<String> cards) {
        Map<Character, List<Integer>> suitedRanks = getSuitedRanks(cards);
        for (List<Integer> ranks : suitedRanks.values()) {
            if (ranks.size() >= 4) {
                Collections.sort(ranks);
                for (int i = 0; i < ranks.size() - 3; i++) {
                    if (ranks.get(i+3) - ranks.get(i) == 5 && !ranks.contains(ranks.get(i) + 2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isFlushDraw(List<String> cards) {
        Map<Character, Integer> suitCount = getSuitCount(cards);
        return suitCount.containsValue(4);
    }

    private boolean isStraightOpenEnded(List<String> cards) {
        List<Integer> ranks = getRanks(cards);
        Collections.sort(ranks);
        for (int i = 0; i < ranks.size() - 3; i++) {
            if (ranks.get(i+3) - ranks.get(i) == 3) {
                if (ranks.get(i) > 0 || ranks.get(i+3) < 12) { // Not including A2345 as open-ended
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isStraightGutshot(List<String> cards) {
        List<Integer> ranks = getRanks(cards);
        Collections.sort(ranks);
        for (int i = 0; i < ranks.size() - 3; i++) {
            if (ranks.get(i+3) - ranks.get(i) == 4) {
                int missingRank = ranks.get(i) + 1;
                if (!ranks.contains(missingRank) && missingRank >= 0 && missingRank <= 12) {
                    return true;
                }
            }
        }
        // Check for A2345 gutshot
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

    private char getSecondHighestRank(List<Character> ranks) {
        List<Character> sortedRanks = new ArrayList<>(ranks);
        sortedRanks.sort((a, b) -> getRankValue(b) - getRankValue(a));
        return sortedRanks.get(1);
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