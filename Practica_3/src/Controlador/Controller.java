package Controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Modelo.Equity;
import Modelo.Model;

public class Controller {

	private final Model modelo;

	public Controller(Model modelo) {
		this.modelo = modelo;
	}

	public List<String> leerYProcesarArchivo(String absolutePath) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> generateRandomBoard() {
        return modelo.generateRandomBoard();
    }

    public List<List<String>> generateRandomPlayerCards(List<String> usedCards, int numPlayers) {
        return modelo.generateRandomPlayerCards(usedCards, numPlayers);
    }
    
    public List<List<String>> getEquity(List<List<String>> listaCartasJugadores, List<String> listaCartasBoard, List<Boolean> activePlayers) {
    	return modelo.getEquity(listaCartasJugadores, listaCartasBoard, activePlayers);
	}
}
