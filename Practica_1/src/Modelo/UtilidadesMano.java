package Modelo;

import java.util.*;

public class UtilidadesMano {

    public static boolean esEscalera(int[] valores, int maxValor) {
        for (int i = 0; i < 5; i++) {
            if (valores[maxValor - i] == 0)
                return false;
        }
        return true;
    }

    public static List<Carta> filtrarCartasPorPalo(List<Carta> cartas, char palo) {
        List<Carta> cartasFiltradas = new ArrayList<>();
        for (Carta carta : cartas) {
            if (carta.getPalo() == palo) {
                cartasFiltradas.add(carta);
            }
        }
        return cartasFiltradas;
    }

    public static boolean esFull(int[] valores) {
        boolean hayTrio = false;
        boolean hayPareja = false;

        for (int valor : valores) {
            if (valor == 3) {
                hayTrio = true;
                break;
            }
        }

        for (int valor : valores) {
            if (valor == 2) {
                hayPareja = true;
                break;
            }
        }

        return hayTrio && hayPareja;
    }

    public static List<Carta> obtenerCartasPorValor(List<Carta> cartas, int valor, int cantidad) {
        List<Carta> cartasSeleccionadas = new ArrayList<>();
        for (Carta carta : cartas) {
            if (carta.getValor() == valor && cartasSeleccionadas.size() < cantidad) {
                cartasSeleccionadas.add(carta);
            }
        }
        return cartasSeleccionadas;
    }

    public static int obtenerValorConRepeticiones(int[] valores, int repeticiones) {
        for (int i = 14; i >= 2; i--) {
            if (valores[i] == repeticiones)
                return i;
        }
        return -1;
    }
}
