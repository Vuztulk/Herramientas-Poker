package Modelo;

public class Carta {
    
    private final int valor;
    private final char palo;

    public Carta(char valor, char palo) {
        this.valor = UtilidadesCarta.parseValor(valor);
        this.palo = palo;
    }

    public int getValor() {
        return valor;
    }

    public char getPalo() {
        return palo;
    }
    
    public String toString() {
        return UtilidadesCarta.getNombreValor(valor) + "" + palo;
    }
}
