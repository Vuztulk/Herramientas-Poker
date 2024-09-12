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
				resultados = procesarApartado3(manos);
				break;
			case "4":
				resultados = procesarApartado4(manos);
				break;
			default:

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
			String mejorMano = modelo.evaluarMejorMano(cartas);
			List<String> draws = modelo.detectarDraws(cartas);

			StringBuilder resultado = new StringBuilder(mano).append("\n");
			resultado.append("- Best hand: ").append(mejorMano).append("\n");
			for (String draw : draws) {
				resultado.append("- Draw: ").append(draw).append("\n");
			}
			resultados.add(resultado.toString());
		}
		return resultados;
	}

	private List<String> procesarApartado2(List<String> manos) {
		List<String> resultados = new ArrayList<>();
		for (String mano : manos) {
			// Dividimos las partes del string: cartas propias y comunes
			String[] partes = mano.split(";");
			List<Carta> cartasPropias = modelo.parsearCartas(partes[0]); // Cartas del jugador
			int numCartasComunes = Integer.parseInt(partes[1]); // Número de cartas comunes
			List<Carta> cartasComunes = modelo.parsearCartas(partes[2]); // Cartas comunes

			// Evaluar la mejor mano combinando cartas propias y comunes
			String mejorMano = modelo.evaluarMejorManoConComunes(cartasPropias, cartasComunes);

			// Crear el formato de salida para la mejor mano
			StringBuilder resultado = new StringBuilder();
			resultado.append(partes[0]).append(";").append(partes[1]).append(";").append(partes[2]).append("\n")
					.append("- Best hand: ").append(mejorMano).append(" with ").append(partes[0]).append(partes[2])
					.append("\n"); // Añade las cartas usadas

			// Si hay menos de 5 cartas comunes, calcular y mostrar los draws
			if (numCartasComunes < 5) {
				List<String> draws = modelo.detectarDrawsConComunes(cartasPropias, cartasComunes);
				for (String draw : draws) {
					resultado.append("- Draw: ").append(draw).append("\n");
				}
			}

			// Añadir el resultado a la lista
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
		// TODO Auto-generated method stub
		return null;
	}

}
