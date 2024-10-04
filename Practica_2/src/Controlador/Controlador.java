package Controlador;

import java.util.List;
import java.util.Set;

import Modelo.AnalizadorRangos;
import Modelo.Modelo;
import Vista.MenuJugadores;

public class Controlador {
    private Modelo modelo;
    private MenuJugadores menuJugadores;

    public Controlador(Modelo modelo, MenuJugadores vista) {
        this.modelo = modelo;
        this.menuJugadores = vista;
    }
    
    public void setVista(MenuJugadores vista) {
        this.menuJugadores = vista;
    }
    
    public AnalizadorRangos inicializarAnalizadorRangos(Set<String> rango, List<String> board) {
    	return modelo.inicializarAnalizadorRangos(rango, board);
    }
    
	public void setRangoJugador(int idJugador, String rangoSeleccionado) {
		modelo.setRangoJugador(idJugador, rangoSeleccionado);	
	}
	
	public Set<String> getRangoJugador(int idJugador) {
		return modelo.getRangoJugador(idJugador);
	}
	
    public void setPorcentajeJugador(int idJugador, double porcentaje) {
        modelo.setPorcentajeJugador(idJugador, porcentaje);
    }

    public double getPorcentajeJugador(int idJugador) {
        return modelo.getPorcentajeJugador(idJugador);
    }

    

}
