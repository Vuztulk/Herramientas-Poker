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
    
}
