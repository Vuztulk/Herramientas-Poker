package Modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MejorMano {
	
	private ClasesMano claseMano;
	private String descripcionMano;
	
	public MejorMano(List<Carta> cartas) {
		evaluarMano(cartas);
	}
	
	public ClasesMano getTipoMano() {
		return this.claseMano;
	}
	
	public String getDescripcionMano() {
		return this.descripcionMano;
	}
	
	private void evaluarMano(List<Carta> cartas) {
	    int[] valores = UtilidadesMano.contarValores(cartas);
	    Map<Character, Integer> conteoPalos = UtilidadesMano.contarPalos(cartas);
	    int max = UtilidadesCarta.obtenerValorMaximo(cartas);
	    int posMax = UtilidadesMano.obtenerPosicionCartaMasAlta(cartas, max);

	    if (comprobarEscaleraColor(cartas, conteoPalos)) {
	        claseMano = ClasesMano.ESCALERA_COLOR;
	    } else if (comprobarPoker(cartas, valores)) {
	        claseMano = ClasesMano.POKER;
	    } else if (comprobarFull(cartas, valores)) {
	        claseMano = ClasesMano.FULL;
	    } else if (comprobarColor(cartas, conteoPalos)) {
	        claseMano = ClasesMano.COLOR;
	    } else if (comprobarEscalera(valores)) {
	        claseMano = ClasesMano.ESCALERA;
	    } else if (comprobarTrio(cartas, valores)) {
	        claseMano = ClasesMano.TRIO;
	    } else if (comprobarPareja(cartas, valores)) {
	        claseMano = ClasesMano.PAREJA;
	    } else {
	        claseMano = ClasesMano.CARTA_MAS_ALTA;
	        descripcionMano = "Carta mas alta [" + UtilidadesCarta.getNombreValor(max) + cartas.get(posMax).getPalo() + "]";
	    }
	}


    private boolean comprobarEscaleraColor(List<Carta> cartas, Map<Character, Integer> conteoPalos) {
        for (Character palo : conteoPalos.keySet()) {
            if (conteoPalos.get(palo) >= 5) {
                List<Carta> cartasDelMismoPalo = UtilidadesMano.filtrarCartasPorPalo(cartas, palo);
                int[] valoresDelPalo = UtilidadesMano.contarValores(cartasDelMismoPalo);
                for (int i = 14; i >= 5; i--) {
                    if (UtilidadesMano.tieneEscalera(valoresDelPalo, i)) {
                    	descripcionMano = "Escalera de color [" + UtilidadesCarta.getNombreValor(i - 4) + " - " + UtilidadesCarta.getNombreValor(i) + "]";
                    	 return true; 
                    }
                }
            }
        }
        return false;
    }

    private boolean comprobarColor(List<Carta> cartas, Map<Character, Integer> conteoPalos) {
        for (Character palo : conteoPalos.keySet()) {
            if (conteoPalos.get(palo) >= 5) {
                List<Carta> cartasDelMismoPalo = UtilidadesMano.filtrarCartasPorPalo(cartas, palo);
                cartasDelMismoPalo.sort(Comparator.comparingInt(Carta::getValor).reversed());
                descripcionMano = "Color " + cartasDelMismoPalo.subList(0, 5);
                return true;
            }
        }
        return false;
    }

    private boolean comprobarEscalera(int[] valores) {
        for (int i = 14; i >= 5; i--) {
            if (UtilidadesMano.tieneEscalera(valores, i)) {
            	descripcionMano = "Escalera [" + UtilidadesCarta.getNombreValor(i - 4) + " - " + UtilidadesCarta.getNombreValor(i) + "]";
            	 return true;
            }
        }
        return false;
    }

    private boolean comprobarPoker(List<Carta> cartas, int[] valores) {
        for (int i = 14; i >= 2; i--) {
            if (valores[i] == 4) {
                List<Carta> cartasPorValor = UtilidadesMano.obtenerCartasPorValor(cartas, i, 4);
                descripcionMano = "Poker de " + UtilidadesCarta.getNombreValor(i) + " " + cartasPorValor;
                return true;
            }
        }
        return false;
    }

    private boolean comprobarFull(List<Carta> cartas, int[] valores) {
        if (UtilidadesMano.contieneNDeUnValor(valores, 3) && UtilidadesMano.contieneNDeUnValor(valores, 2)) {
            int trio = UtilidadesMano.obtenerValorConRepeticiones(valores, 3);
            int pareja = UtilidadesMano.obtenerValorConRepeticiones(valores, 2);
            List<Carta> cartasTrio = UtilidadesMano.obtenerCartasPorValor(cartas, trio, 3);
            List<Carta> cartasPareja = UtilidadesMano.obtenerCartasPorValor(cartas, pareja, 2);
            descripcionMano = "Full de " + UtilidadesCarta.getNombreValor(trio) + " y " + UtilidadesCarta.getNombreValor(pareja) + ": " + cartasTrio + " " + cartasPareja;
            return true;
        }
        return false;
    }

    private boolean comprobarTrio(List<Carta> cartas, int[] valores) {
        for (int i = 14; i >= 2; i--) {
            if (valores[i] == 3) {
                List<Carta> cartasPorValor = UtilidadesMano.obtenerCartasPorValor(cartas, i, 3);
                descripcionMano = "Trio de " + UtilidadesCarta.getNombreValor(i) + " " + cartasPorValor;
                return true;
            }
        }
        return false;
    }

    private boolean comprobarPareja(List<Carta> cartas, int[] valores) {
        for (int i = 14; i >= 2; i--) {
            if (valores[i] == 2) {
                List<Carta> cartasPorValor = UtilidadesMano.obtenerCartasPorValor(cartas, i, 2);
                descripcionMano = "Pareja de " + UtilidadesCarta.getNombreValor(i) + " " + cartasPorValor;
                return true;
            }
        }
        return false;
    }

}
