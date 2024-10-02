package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Model {

    public Model() {}

    public List<Carta> parsearCartas(String mano) {
        return UtilidadesCarta.parsearCartas(mano);
    }

    public String evaluarMejorMano(String mano) {
        return new Mano(parsearCartas(mano)).obtenerCartasComoString();
    }
    
    public String getCartasMejorMano(String mano) {
        return new Mano(parsearCartas(mano)).obtenerMejorManoComoString();
    }

    public String getDescripcionMano(String mano) {
        return new Mano(parsearCartas(mano)).getDescripcionMano();
    }

    public List<String> detectarDraws(String mano) {
        return new Draws().detectarDraws(parsearCartas(mano));
    }

    public String evaluarMejorManoConComunes(String cartasPropias, String cartasComunes) {
        return evaluarMejorManoGenerica(cartasPropias, cartasComunes, 3, 5, true);
    }
    
    public String evaluarMejorManoConComunesRaw(String cartasPropias, String cartasComunes) {
        return evaluarMejorManoGenerica(cartasPropias, cartasComunes, 3, 5, true);
    }

    public List<String> ordenarJugadoresPorMejorMano(List<String> manosJugadores, String cartasComunes) {
        return ordenarJugadores(manosJugadores, cartasComunes, false);
    }

    public List<String> ordJugRaw(List<String> manosJugadores, String cartasComunes) {
        return ordenarJugadores(manosJugadores, cartasComunes, true);
    }

    private List<String> ordenarJugadores(List<String> manosJugadores, String cartasComunes, boolean mostrarMejorMano) {
        List<Jugador> jugadores = new ArrayList<>();

        for (int i = 0; i < manosJugadores.size(); i++) {
            String mejorManoCartas = evaluarMejorManoConComunes(manosJugadores.get(i), cartasComunes);
            Mano manoJugador = new Mano(UtilidadesCarta.parsearCartas(mejorManoCartas));
            jugadores.add(new Jugador("J" + (i + 1), manoJugador));
        }

        jugadores.sort((j1, j2) -> Integer.compare(j2.getMano().getValor(), j1.getMano().getValor()));

        List<String> resultados = new ArrayList<>();
        for (Jugador jugador : jugadores) {
            String resultado = jugador.getIdentificador() + ": " + //Dependiendo del valor muestra la mano raw o la descipcion completa
            				   (mostrarMejorMano ? jugador.getMano().obtenerMejorManoComoString() : jugador.getMano().getDescripcionMano());
            resultados.add(resultado);
        }

        return resultados;
    }

    private String evaluarMejorManoGenerica(String cartasPropias, String cartasComunes, int minCombinacion, int maxCombinacion, boolean res) {
        List<Carta> cartasJugador = parsearCartas(cartasPropias);
        List<Carta> cartasComunesLista = parsearCartas(cartasComunes);

        Mano mejorMano = null;

        for (int i = minCombinacion; i <= maxCombinacion; i++) {
            for (List<Carta> combinacionComunes : UtilidadesMano.generarCombinaciones(cartasComunesLista, i)) {
                List<Carta> combinacionTotal = new ArrayList<>(cartasJugador);
                combinacionTotal.addAll(combinacionComunes);

                Mano manoActual = new Mano(combinacionTotal);
                if (mejorMano == null || manoActual.evaluarValorMano() > mejorMano.evaluarValorMano()) {
                    mejorMano = manoActual;
                }
            }
        }
        return res ? mejorMano.obtenerMejorManoComoString() : mejorMano.obtenerCartasComoString();
    }

    public String evaluarMejorManoOmaha(String cartasPropias, String cartasComunes) {
        List<Carta> cartasJugador = parsearCartas(cartasPropias);
        List<Carta> cartasComunesLista = parsearCartas(cartasComunes);

        Mano mejorMano = null;
        List<List<Carta>> combinacionesPropias = UtilidadesMano.generarCombinaciones(cartasJugador, 2);
        List<List<Carta>> combinacionesComunes = UtilidadesMano.generarCombinaciones(cartasComunesLista, 3);

        for (List<Carta> combinacionPropias : combinacionesPropias) {
            for (List<Carta> combinacionComunes : combinacionesComunes) {
                List<Carta> combinacionTotal = new ArrayList<>(combinacionPropias);
                combinacionTotal.addAll(combinacionComunes);

                Mano manoActual = new Mano(combinacionTotal);
                if (mejorMano == null || manoActual.evaluarValorMano() > mejorMano.evaluarValorMano()) {
                    mejorMano = manoActual;
                }
            }
        }
        return mejorMano.obtenerCartasComoString();
    }
}
