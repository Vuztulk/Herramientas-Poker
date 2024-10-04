package Modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Vista.PanelAnalisis;

public class Modelo {
	
    private Map<Integer, RangoCartas> rangosPorJugador;
    private AnalizadorRangos analizadorRangos;
    
    public Modelo() {
        inicializarRangosJugadores();
    }

    public AnalizadorRangos inicializarAnalizadorRangos(Set<String> rango, List<String> board) {
    	return new AnalizadorRangos(rango, board);
    }
    
    private void inicializarRangosJugadores() {
        rangosPorJugador = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            rangosPorJugador.put(i, new RangoCartas(null));
        }
    }

    public Set<String> getRangoJugador(int idJugador) {
        return rangosPorJugador.getOrDefault(idJugador, new RangoCartas("")).getRangos();
    }

    public void setRangoJugador(String rangoInput, int idJugador) {
        rangosPorJugador.put(idJugador, crearRangoCartas(rangoInput));
    }

    public double getPorcentajeJugador(int idJugador) {
        return rangosPorJugador.getOrDefault(idJugador, new RangoCartas("")).getPorcentaje();
    }

    public void setPorcentajeJugador(int idJugador, double nuevoPorcentaje) {
        rangosPorJugador.computeIfPresent(idJugador, (k, v) -> {
            v.actualizarPorcentaje(nuevoPorcentaje);
            return v;
        });
    }

    private RangoCartas crearRangoCartas(String rangoInput) {
        return new RangoCartas(rangoInput);
    }

    public void setRangoJugador(int idJugador, String rangoSeleccionado) {
        rangosPorJugador.put(idJugador, crearRangoCartas(rangoSeleccionado));
    }
}
