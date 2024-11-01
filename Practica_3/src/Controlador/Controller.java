package Controlador;

import java.util.List;
import Modelo.Model;

public class Controller {

	private final Model modelo;

	public Controller(Model modelo) {
		this.modelo = modelo;
	}

	public List<String> generateRandomBoard() {
        return modelo.generateRandomBoard();
    }

    public List<List<String>> generateRandomPlayerCards(List<String> usedCards, int numPlayers) {
        return modelo.generateRandomPlayerCards(usedCards, numPlayers);
    }
    
    public List<String> getEquity(List<List<String>> listaCartasJugadores, List<String> listaCartasBoard, List<Boolean> activePlayers, boolean type_game) {
    	return modelo.getEquity(listaCartasJugadores, listaCartasBoard, activePlayers, type_game);
	}
}
