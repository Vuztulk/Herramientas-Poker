package Controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Modelo.Model;
import Vista.View;

public class Controller {

	private final Model modelo;
	private final View vista;

	public Controller(Model modelo, View vista) {
		this.modelo = modelo;
		this.vista = vista;
	}

	public void procesarOrden(String apartado, String archivoEntrada, String archivoSalida) {
		try {
			List<String> input = vista.leerArchivo(archivoEntrada);
			List<String> resultados = new ArrayList<>();

			switch (apartado) {
			case "1":
				resultados = procesarApartado1(input);
				break;
			case "2":
				resultados = procesarApartado2(input);
				break;
			case "3":
				resultados = procesarApartado3(input);
				break;
			case "4":
				resultados = procesarApartado4(input);
				break;
			default:
				break;
			}

			vista.escribirArchivo(archivoSalida, resultados);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<String> procesarApartado1(List<String> input) {

		List<String> resultados = new ArrayList<>();

		for (String mano : input) {

			StringBuilder resultado = new StringBuilder(mano).append("\n");
			String mejorMano = modelo.evaluarMejorMano(mano);

			resultado.append("- Best hand: ").append(modelo.getDescripcionMano(mejorMano)).append("\n");

			for (String draw : modelo.detectarDraws(mejorMano)) {
				resultado.append("- Draw: ").append(draw).append("\n");
			}

			resultados.add(resultado.toString());
		}
		return resultados;

	}

	private List<String> procesarApartado2(List<String> input) {
		List<String> resultados = new ArrayList<>();

		for (String mano : input) {
			String[] partes = mano.split(";");

			String mejorMano = modelo.evaluarMejorManoConComunes(partes[0], partes[2]);// Le pasamos las cartas propiasy las comunes

			String resultadoBase = String.join(";", partes[0], partes[1], partes[2]) + "\n";
			StringBuilder resultado = new StringBuilder(resultadoBase).append("- Best hand: ")
					.append(modelo.getDescripcionMano(mejorMano)).append(" with ").append(partes[0]).append(partes[2])
					.append("\n");

			if (Integer.parseInt(partes[1]) < 5) {// Si hay menos de 5 cartas comunes se miran los draws
				List<String> draws = modelo.detectarDraws(mejorMano);
				for (String draw : draws) {
					resultado.append("- Draw: ").append(draw).append("\n");
				}
			}

			resultados.add(resultado.toString());
		}

		return resultados;
	}

	private List<String> procesarApartado3(List<String> input) {
		List<String> resultados = new ArrayList<>();

		for (String mano : input) {

			String[] partes = mano.split(";");
			int numJugadores = Integer.parseInt(partes[0]);
			String cartasComunes = partes[numJugadores + 1];
			List<String> manosJugadores = new ArrayList<>();

			for (int i = 1; i <= numJugadores; i++) {// Añadimos las cartas de los jugadores a manosJugadores para pasarselo a la funcion de ordenar

				String cartasJugador = partes[i].substring(2);
				StringBuilder manoJugador = new StringBuilder();

				for (int j = 0; j < cartasJugador.length(); j += 2) {
					manoJugador.append(cartasJugador.substring(j, j + 2)).append("");
				}
				manosJugadores.add(manoJugador.toString().trim());

			}

			String resultadoBase = String.join(";", partes[0], partes[1], cartasComunes) + "\n";
			StringBuilder resultado = new StringBuilder(resultadoBase);

			List<String> resultadoMano = modelo.ordenarJugadoresPorMejorMano(manosJugadores, cartasComunes);
			for (String jugadorResultado : resultadoMano) {
				resultado.append(jugadorResultado).append("\n");
			}

			resultados.add(resultado.toString());
		}

		return resultados;
	}

	private List<String> procesarApartado4(List<String> input) {

		List<String> resultados = new ArrayList<>();

		for (String mano : input) {
			String[] partes = mano.split(";");


			String mejorMano = modelo.evaluarMejorManoOmaha(partes[0], partes[2]);

			String resultadoBase = String.join(";", partes[0], partes[1], partes[2]) + "\n";
			StringBuilder resultado = new StringBuilder(resultadoBase).append("- Best hand: ").append(modelo.getDescripcionMano(mejorMano)).append("\n");

			resultados.add(resultado.toString());
		}

		return resultados;
	}

}
