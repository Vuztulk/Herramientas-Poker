package Controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Modelo.Model;
import Vista.View;
import Modelo.Carta;

public class Controller {

	private final Model modelo;
	private final View vista;

	public Controller(Model modelo, View vista) {
		this.modelo = modelo;
		this.vista = vista;
	}

	public void procesarOrden(String apartado, String archivoEntrada, String archivoSalida) {
		try {
			List<String> manos = vista.leerArchivo(archivoEntrada);
			List<String> resultados = new ArrayList<>();

			switch (apartado) {
			case "1":
				resultados = procesarApartado1(manos);
				break;
			case "2":
				resultados = procesarApartado2(manos);
				break;
			case "3":
				// resultados = procesarApartado3(manos);
				break;
			case "4":
				// resultados = procesarApartado4(manos);
				break;
			default:
				break;
			}

			vista.escribirArchivo(archivoSalida, resultados);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<String> procesarApartado1(List<String> manos) {

		List<String> resultados = new ArrayList<>();

		for (String mano : manos) {

			List<Carta> cartas = modelo.parsearCartas(mano);

			StringBuilder resultado = new StringBuilder(mano).append("\n");
			resultado.append("- Best hand: ").append(modelo.evaluarMejorMano(cartas)).append("\n");

			for (String draw : modelo.detectarDraws(cartas)) {
				resultado.append("- Draw: ").append(draw).append("\n");
			}

			resultados.add(resultado.toString());
		}
		return resultados;

	}

	private List<String> procesarApartado2(List<String> manos) {
		List<String> resultados = new ArrayList<>();

		for (String mano : manos) {
			String[] partes = mano.split(";");

			List<Carta> mejorManoCartas = modelo.evaluarMejorManoConComunes(modelo.parsearCartas(partes[0]), modelo.parsearCartas(partes[2]));
			String mejorMano = modelo.evaluarMejorMano(mejorManoCartas);

			String resultadoBase = String.join(";", partes[0], partes[1], partes[2]) + "\n";
			StringBuilder resultado = new StringBuilder(resultadoBase).append("- Best hand: ").append(mejorMano).append(" with ").append(partes[0]).append(partes[2]).append("\n");

			if (Integer.parseInt(partes[1]) < 5) {//Si hay menos de 5 cartas comunes se miran los draws
				List<String> draws = modelo.detectarDraws(mejorManoCartas);
				for (String draw : draws) {
					resultado.append("- Draw: ").append(draw).append("\n");
				}
			}

			resultados.add(resultado.toString());
		}

		return resultados;
	}

	private List<String> procesarApartado3(List<String> manos) {
		List<String> resultados = new ArrayList<>();
		for (String mano : manos) {
			String[] partes = mano.split(";");
			int numJugadores = Integer.parseInt(partes[0]);

			List<List<Carta>> cartasJugadores = new ArrayList<>();
			for (int i = 1; i <= numJugadores; i++) {
				cartasJugadores.add(modelo.parsearCartas(partes[i]));
			}

			List<Carta> cartasComunes = modelo.parsearCartas(partes[numJugadores + 1]);
			List<String> rankingJugadores = modelo.ordenarJugadoresPorMejorMano(cartasJugadores, cartasComunes);

			for (String resultadoJugador : rankingJugadores) {
				resultados.add(resultadoJugador);
			}
			resultados.add("\n");
		}
		return resultados;
	}

	private List<String> procesarApartado4(List<String> manos) {
		return null;
	}

}
