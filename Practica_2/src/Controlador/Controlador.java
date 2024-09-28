package Controlador;

import java.util.Set;
import Modelo.Modelo;
import Vista.Vista;

public class Controlador {
    private Modelo modelo;
    private Vista vista;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
    }
    
    public void setVista(Vista vista) {
        this.vista = vista;
    }
    
	public Set<String> getRangosJugador(int idJugador) {
		return modelo.obtenerRangoPorJugador(idJugador);
	}

	public void establecerRangoParaJugador(String rangoInput, int idJugador) {
		modelo.establecerRangoParaJugador(rangoInput, idJugador);
	}
	
    public void actualizarPorcentajeJugador(int idJugador, double porcentaje) {
        modelo.actualizarPorcentajeJugador(idJugador, porcentaje);
    }

    public double getPorcentajeJugador(int idJugador) {
        return modelo.getPorcentajeJugador(idJugador);
    }

	public void guardarRangoJugador(int idJugador, String rangoSeleccionado) {
		modelo.guardarRangoJugador(idJugador, rangoSeleccionado);
		
	}
}
