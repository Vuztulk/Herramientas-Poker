package Modelo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Modelo {
    private Map<Integer, RangoCartas> rangosPorJugador;

    public Modelo() {
        rangosPorJugador = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            rangosPorJugador.put(i, new RangoCartas(""));
        }
    }

    public Set<String> obtenerRangoPorJugador(int idJugador) {
        RangoCartas rangoCartas = rangosPorJugador.get(idJugador);
        return rangoCartas != null ? rangoCartas.getRangos() : new HashSet<>();
    }

    public double obtenerPorcentajePorJugador(int idJugador) {
        RangoCartas rangoCartas = rangosPorJugador.get(idJugador);
        return rangoCartas != null ? rangoCartas.getPorcentaje() : 0.0;
    }

    public void establecerRangoParaJugador(String rangoInput, int idJugador) {
        if (rangosPorJugador.containsKey(idJugador)) {
            rangosPorJugador.put(idJugador, crearRangoCartas(rangoInput));
        }
    }

    public RangoCartas crearRangoCartas(String rangoInput) {
        return new RangoCartas(rangoInput);
    }

    public void actualizarPorcentajeJugador(int idJugador, double nuevoPorcentaje) {
        RangoCartas rangoCartas = rangosPorJugador.get(idJugador);
        if (rangoCartas != null) {
            rangoCartas.actualizarPorcentaje(nuevoPorcentaje);
        }
    }

    public double getPorcentajeJugador(int idJugador) {
        return obtenerPorcentajePorJugador(idJugador);
    }

    public void guardarRangoJugador(int idJugador, String rangoSeleccionado) {
        if (rangosPorJugador.containsKey(idJugador)) {
            RangoCartas nuevoRango = crearRangoCartas(rangoSeleccionado);
            rangosPorJugador.put(idJugador, nuevoRango);
        } else {
            rangosPorJugador.put(idJugador, crearRangoCartas(rangoSeleccionado));
        }
    }
}
