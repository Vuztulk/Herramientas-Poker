package Modelo;

import java.util.List;

public class UtilidadesCarta {

    public static String getNombreValor(int valor) {
        switch (valor) {
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            case 14: return "A";
            default: return String.valueOf(valor);
        }
    }

    public static int parseValor(char valor) {
        switch (valor) {
            case 'A': return 14;
            case 'K': return 13;
            case 'Q': return 12;
            case 'J': return 11;
            case 'T': return 10;
            default: return valor - '0';
        }
    }

    public static int obtenerValorMaximo(List<Carta> cartas) {
        int[] valores = new int[15];
        for (Carta carta : cartas) {
            valores[carta.getValor()]++;
        }
        for (int i = valores.length - 1; i >= 2; i--) {
            if (valores[i] > 0) {
                return i;
            }
        }
        return -1;
    }
}
