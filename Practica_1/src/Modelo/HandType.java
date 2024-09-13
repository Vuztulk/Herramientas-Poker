package Modelo;

public enum HandType {
    ROYAL_FLUSH("Royal Flush"),
    STRAIGHT_FLUSH("Straight Flush"),
    FLUSH("Flush"),
    STRAIGHT("Straight"),
    FOUR_OF_A_KIND("Four of a Kind"),
    FULL_HOUSE("Full House"),
    THREE_OF_A_KIND("Three of a Kind"),
    TWO_PAIR("Two Pair"),
    ONE_PAIR("Pair"),
    HIGH_CARD("High Card");

    private final String nombre;

    HandType(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}