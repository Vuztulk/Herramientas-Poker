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

	public List<String> procesarOrden(String apartado, String archivoEntrada, String archivoSalida, boolean salidaEnArchivo) {
		try {
			List<String> input = vista.leerArchivo(archivoEntrada);
			List<String> resultados;

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
				return null;
			}
			if(salidaEnArchivo)
				vista.escribirArchivo(archivoSalida, resultados);
			else
				return resultados;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<String> procesarApartado1(List<String> input) {
		List<String> resultados = new ArrayList<>();

		for (String mano : input) {
			StringBuilder resultado = new StringBuilder(mano).append("\n");
			String mejorMano = modelo.evaluarMejorMano(mano);
			resultado.append("- Best hand: ").append(modelo.getDescripcionMano(mejorMano)).append("\n");
			agregarDraws(resultado, modelo.detectarDraws(mejorMano));
			resultados.add(resultado.toString());
		}
		return resultados;
	}

	private List<String> procesarApartado2(List<String> input) {
		List<String> resultados = new ArrayList<>();

		for (String mano : input) {
			String[] partes = mano.split(";");
			String mejorMano = modelo.evaluarMejorManoConComunes(partes[0], partes[2]);

			StringBuilder resultado = new StringBuilder(String.join(";", partes[0], partes[1], partes[2]))
					.append("\n- Best hand: ").append(modelo.getDescripcionMano(mejorMano)).append(" con ")
					.append(mejorMano).append("\n");

			if (Integer.parseInt(partes[1]) < 5) { // Si hay menos de 5 cartas comunes se miran los draws
				agregarDraws(resultado, modelo.detectarDraws(mejorMano));
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
			List<String> manosJugadores = extraerManosJugadores(partes, numJugadores);

			StringBuilder resultado = new StringBuilder().append(mano).append("\n");

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

			StringBuilder resultado = new StringBuilder(String.join(";", partes[0], partes[1], partes[2]))
					.append("\n- Best hand: ").append(modelo.getDescripcionMano(mejorMano)).append("\n");

			agregarDraws(resultado, modelo.detectarDraws(mejorMano));
			resultados.add(resultado.toString());
		}

		return resultados;
	}

	private void agregarDraws(StringBuilder resultado, List<String> draws) {
		for (String draw : draws) {
			resultado.append("- Draw: ").append(draw).append("\n");
		}
	}

	private List<String> extraerManosJugadores(String[] partes, int numJugadores) {
		List<String> manosJugadores = new ArrayList<>();

		for (int i = 1; i <= numJugadores; i++) {
			String cartasJugador = partes[i].substring(2);
			manosJugadores.add(cartasJugador.trim());
		}

		return manosJugadores;
	}

	////////////////////////////////////////
	////// METODOS PARA USO EN LA GUI //////
	////////////////////////////////////////
	
	//Hago esto puesto que la salida en los metodos de arriba no es la que necesito para pintar las cartas en la GUI
	
	public List<String> leerYProcesarArchivo(String rutaArchivo) throws IOException {
		return vista.leerArchivo(rutaArchivo);
	}

	public List<String> obtenerManoRaw(List<String> input) {
		List<String> resultados = new ArrayList<>();
		for (String mano : input) {
			resultados.add(modelo.getCartasMejorMano(mano));
		}
		return resultados;
	}

	public List<String> obtenerManoComunesRaw(List<String> input) {
		List<String> resultados = new ArrayList<>();
		for (String mano : input) {
			String[] partes = mano.split(";");
			resultados.add(modelo.evaluarMejorManoConComunesRaw(partes[0], partes[2]));
		}
		return resultados;
	}

	public List<List<String>> ordJugRaw(List<String> input) {
		List<List<String>> resultados = new ArrayList<>();
		for (String mano : input) {
			String[] partes = mano.split(";");
			int numJugadores = Integer.parseInt(partes[0]);
			String cartasComunes = partes[numJugadores + 1];
			List<String> manosJugadores = extraerManosJugadores(partes, numJugadores);

			List<String> resultadoMano = modelo.ordJugRaw(manosJugadores, cartasComunes);

			List<String> resultadoJugador = new ArrayList<>();
			for (String jugadorResultado : resultadoMano) {
				String resultadoSinEtiqueta = jugadorResultado.replaceAll("J\\d+: ", "");
				resultadoJugador.add(resultadoSinEtiqueta);
			}

			resultados.add(resultadoJugador);
		}
		return resultados;
	}

	public List<String> obtenerManoOmahaRaw(List<String> input) {
		List<String> resultados = new ArrayList<>();
		for (String mano : input) {
			String[] partes = mano.split(";");
			resultados.add(modelo.evaluarMejorManoOmaha(partes[0], partes[2]));
		}
		return resultados;
	}
}
