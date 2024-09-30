package Modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MejorMano {

    private ClasesMano claseMano;
    private String descripcionMano;
    private List<Carta> cartasMano;

    public MejorMano(List<Carta> cartas) {
        this.cartasMano = new ArrayList<>();
        evaluarMano(cartas);
    }

    public ClasesMano getTipoMano() {
        return this.claseMano;
    }

    public String getDescripcionMano() {
        return this.descripcionMano;
    }
    
    public List<Carta> getCartasMejorMano() {
        return this.cartasMano;
    }

    private void evaluarMano(List<Carta> cartas) {
        int[] valores = UtilidadesMano.contarValores(cartas);
        Map<Character, Integer> conteoPalos = UtilidadesMano.contarPalos(cartas);
        int max = UtilidadesCarta.obtenerValorMaximo(cartas);
        int posMax = UtilidadesMano.obtenerPosicionCartaMasAlta(cartas, max);

        if (comprobarEscaleraColor(valores, cartas, conteoPalos)) {
            claseMano = ClasesMano.ESCALERA_COLOR;
        } else if (comprobarPoker(cartas, valores)) {
            claseMano = ClasesMano.POKER;
        } else if (comprobarFull(cartas, valores)) {
            claseMano = ClasesMano.FULL;
        } else if (comprobarColor(cartas, conteoPalos)) {
            claseMano = ClasesMano.COLOR;
        } else if (comprobarEscalera(cartas, valores)) {
            claseMano = ClasesMano.ESCALERA;
        } else if (comprobarTrio(cartas, valores)) {
            claseMano = ClasesMano.TRIO;
        } else if (comprobarDoblePareja(cartas, valores)) {
            claseMano = ClasesMano.DOBLE_PAREJA;
        } else if (comprobarPareja(cartas, valores)) {
            claseMano = ClasesMano.PAREJA;
        } else {
            claseMano = ClasesMano.CARTA_MAS_ALTA;
            descripcionMano = "Carta mas alta [" + UtilidadesCarta.getNombreValor(max) + cartas.get(posMax).getPalo() + "]";
            cartasMano.add(cartas.get(posMax));
        }
    }

    private boolean comprobarEscaleraColor(int[] valores, List<Carta> cartas, Map<Character, Integer> conteoPalos) {
        for (Character palo : conteoPalos.keySet()) {
            if (conteoPalos.get(palo) >= 5) {
                for (int i = 14; i >= 5; i--) {
                    int pos_escalera = UtilidadesMano.tieneEscalera(valores, i);
                    if (pos_escalera != -1) {
                        List<Carta> cartasEscaleraColor = new ArrayList<>();
                        for (int j = pos_escalera - 4; j <= pos_escalera; j++) {
                            for (Carta carta : cartas) {
                                if (carta.getValor() == j && carta.getPalo() == palo) {
                                    cartasEscaleraColor.add(carta);
                                }
                            }
                        }
                        if (cartasEscaleraColor.size() == 5) {
                            this.cartasMano = cartasEscaleraColor;
                            StringBuilder descripcion = new StringBuilder();
                            descripcion.append("Escalera de color [");
                            for (int j = 0; j < cartasEscaleraColor.size(); j++) {
                                descripcion.append(cartasEscaleraColor.get(j).toString());
                                if (j < cartasEscaleraColor.size() - 1) {
                                    descripcion.append(", ");
                                }
                            }
                            descripcion.append("]");
                            this.descripcionMano = descripcion.toString();
                            return true;
                        }
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
                cartasMano = cartasDelMismoPalo.subList(0, 5);
                descripcionMano = "Color " + cartasMano;
                return true;
            }
        }
        return false;
    }

    private boolean comprobarEscalera(List<Carta> cartas, int[] valores) {
        for (int i = 14; i >= 5; i--) {
            int pos_escalera = UtilidadesMano.tieneEscalera(valores, i);
            if (pos_escalera != -1) {
                List<Carta> cartasEscalera = new ArrayList<>();
                for (int j = pos_escalera - 4; j <= pos_escalera; j++) {
                    for (Carta carta : cartas) {
                        if (carta.getValor() == j) {
                            cartasEscalera.add(carta);
                        }
                    }
                }
                if (cartasEscalera.size() == 5) {
                    cartasMano = cartasEscalera;
                    StringBuilder descripcion = new StringBuilder();
                    descripcion.append("Escalera [");
                    for (int j = 0; j < cartasEscalera.size(); j++) {
                        descripcion.append(cartasEscalera.get(j).toString());
                        if (j < cartasEscalera.size() - 1) {
                            descripcion.append(", ");
                        }
                    }
                    descripcion.append("]");
                    this.descripcionMano = descripcion.toString();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean comprobarPoker(List<Carta> cartas, int[] valores) {
        for (int i = 14; i >= 2; i--) {
            if (valores[i] == 4) {
                cartasMano = UtilidadesMano.obtenerCartasPorValor(cartas, i, 4);
                descripcionMano = "Poker de " + UtilidadesCarta.getNombreValor(i) + " " + cartasMano;
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
            cartasMano.clear();
            cartasMano.addAll(cartasTrio);
            cartasMano.addAll(cartasPareja);
            descripcionMano = "Full de " + UtilidadesCarta.getNombreValor(trio) + " y " + UtilidadesCarta.getNombreValor(pareja) + ": " + cartasMano;
            return true;
        }
        return false;
    }

    private boolean comprobarTrio(List<Carta> cartas, int[] valores) {
        for (int i = 14; i >= 2; i--) {
            if (valores[i] == 3) {
                cartasMano = UtilidadesMano.obtenerCartasPorValor(cartas, i, 3);
                descripcionMano = "Trio de " + UtilidadesCarta.getNombreValor(i) + " " + cartasMano;
                return true;
            }
        }
        return false;
    }
    
    private boolean comprobarDoblePareja(List<Carta> cartas, int[] valores) {
        List<Integer> parejas = new ArrayList<>();
        List<Carta> cartasRestantes = new ArrayList<>(cartas);
        
        for (int i = 14; i >= 2; i--) {
            if (valores[i] == 2) {
                parejas.add(i);
                if (parejas.size() == 2) {
                    List<Carta> cartasPareja1 = UtilidadesMano.obtenerCartasPorValor(cartas, parejas.get(0), 2);
                    cartasRestantes.removeAll(cartasPareja1);
                    
                    List<Carta> cartasPareja2 = UtilidadesMano.obtenerCartasPorValor(cartas, parejas.get(1), 2);
                    cartasRestantes.removeAll(cartasPareja2);
                    
                    cartasMano.clear();
                    cartasMano.addAll(cartasPareja1);
                    cartasMano.addAll(cartasPareja2);
                    
                    Carta cartaAlta = cartasRestantes.get(0);
                    for (Carta carta : cartasRestantes) {
                        if (carta.getValor() > cartaAlta.getValor()) {
                            cartaAlta = carta;
                        }
                    }
                    cartasMano.add(cartaAlta);

                    descripcionMano = "Doble pareja de " + UtilidadesCarta.getNombreValor(parejas.get(0)) + " y " + UtilidadesCarta.getNombreValor(parejas.get(1)) + " + " + cartaAlta;
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean comprobarPareja(List<Carta> cartas, int[] valores) {
        for (int i = 14; i >= 2; i--) {
            if (valores[i] == 2) {
                cartasMano = UtilidadesMano.obtenerCartasPorValor(cartas, i, 2);
                descripcionMano = "Pareja de " + UtilidadesCarta.getNombreValor(i) + " " + cartasMano;
                return true;
            }
        }
        return false;
    }
    
}
