package Modelo;

import java.util.ArrayList;
import java.util.List;

public class UtilidadesCarta {

    public static String getNombreValor(int valor) {
        switch (valor) {
        	case 10: return "T";
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            case 14: return "A";
            default: return String.valueOf(valor);
        }
    }

    public static int getValorNombre(char valor) {
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
    
    public static List<Carta> parsearCartas(String mano) {
        List<Carta> cartas = new ArrayList<>();
        for (int i = 0; i < mano.length(); i += 2) {
            cartas.add(new Carta(mano.charAt(i), mano.charAt(i + 1)));
        }
        return cartas;
    }
    
    public static String convertirListaCartasAString(List<Carta> cartas) {
        StringBuilder sb = new StringBuilder();
        for (Carta carta : cartas) {
            sb.append(carta.toString());
        }
        return sb.toString();
    }
    
}
