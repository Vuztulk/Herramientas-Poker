package Modelo;

public class Carta {
    
    private final int valor;
    private final char palo;

    public Carta(char valor, char palo) {
        this.valor = parseValor(valor);
        this.palo = palo;
    }

    public int getValor() {
        return valor;
    }

    public char getPalo() {
        return palo;
    }
    
    public String toString() {
        return getNombreValor(valor) + "" + palo;
    }
    
    private int parseValor(char valor) {
        switch (valor) {
            case 'A': return 14;
            case 'K': return 13;
            case 'Q': return 12;
            case 'J':  return 11;
            case 'T': return 10;
            default: return valor - '0';
        }
    }
    
    private String getNombreValor(int valor) {
        switch (valor) {
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            case 14: return "A";
            default: return String.valueOf(valor);
        }
    }
}
