package Modelo;

import java.util.HashSet;
import java.util.Set;

public class RangoCartas {
	private Set<String> rangos;// Los rangos internamente se guardan sin + ni -, sino todas las cartas que
								// participan en la seleccion
	private double porcentaje;
	private static final String ordenCartas = "23456789TJQKA";

	public RangoCartas(String rangoInput) {
		if (rangoInput == null || rangoInput.trim().isEmpty()) {
			rangos = new HashSet<>();
			this.porcentaje = 0;
			return;
		}
		rangos = new HashSet<>();
		procesarRango(rangoInput);
	}

	private void procesarRango(String rangoInput) {
		String[] partes = rangoInput.split(",");
		for (String parte : partes) {
			parte = parte.trim();
			if (parte.isEmpty())
				continue;
			if (parte.contains("+")) {
				agregarRangoMayor(parte);
			} else if (parte.contains("-")) {
				agregarRangoIntervalo(parte);
			} else {
				rangos.add(parte);
			}
		}
		this.porcentaje = (double) rangos.size() / 1326 * 100;
	}

	private void agregarRangoMayor(String parte) {
		String carta = parte.substring(0, 2);
		boolean suited = parte.contains("s");
		boolean offSuited = parte.contains("o");
		char valorCarta = carta.charAt(0);
		char segundaCarta = carta.charAt(1);

		if (valorCarta == segundaCarta) {
			for (int i = getRankValue(valorCarta); i < ordenCartas.length(); i++) {
				char cartaActual = ordenCartas.charAt(i);
				rangos.add(String.valueOf(cartaActual) + String.valueOf(cartaActual));
			}
		} else {
			int indiceValorCarta = ordenCartas.indexOf(valorCarta);
			int indiceSegundaCarta = ordenCartas.indexOf(segundaCarta);

			for (int i = indiceSegundaCarta; i < indiceValorCarta; i++) {
				char cartaActual = ordenCartas.charAt(i);
				if (suited) {
					rangos.add(valorCarta + String.valueOf(cartaActual) + "s");
				}
				if (offSuited || !suited) {
					rangos.add(valorCarta + String.valueOf(cartaActual) + "o");
				}
			}
		}
	}

	private void agregarRangoIntervalo(String parte) {
		String[] limites = parte.split("-");
		String limiteInferior = limites[0].trim();
		String limiteSuperior = limites[1].trim();
		boolean suited = limiteInferior.endsWith("s") && limiteSuperior.endsWith("s");
		boolean offSuited = limiteInferior.endsWith("o") && limiteSuperior.endsWith("o");

		char cartaInferiorAlta = limiteInferior.charAt(0);
		char cartaInferiorBaja = limiteInferior.charAt(1);
		char cartaSuperiorAlta = limiteSuperior.charAt(0);
		char cartaSuperiorBaja = limiteSuperior.charAt(1);

		int indiceInferiorAlto = ordenCartas.indexOf(cartaInferiorAlta);
		int indiceInferiorBajo = ordenCartas.indexOf(cartaInferiorBaja);
		int indiceSuperiorAlto = ordenCartas.indexOf(cartaSuperiorAlta);
		int indiceSuperiorBajo = ordenCartas.indexOf(cartaSuperiorBaja);

		// Intercambiar si limite superior < limite inferior
		if (indiceSuperiorAlto < indiceInferiorAlto
				|| (indiceSuperiorAlto == indiceInferiorAlto && indiceSuperiorBajo < indiceInferiorBajo)) {
			char tempAlta = cartaInferiorAlta;
			char tempBaja = cartaInferiorBaja;
			cartaInferiorAlta = cartaSuperiorAlta;
			cartaInferiorBaja = cartaSuperiorBaja;
			cartaSuperiorAlta = tempAlta;
			cartaSuperiorBaja = tempBaja;

			indiceInferiorAlto = ordenCartas.indexOf(cartaInferiorAlta);
			indiceInferiorBajo = ordenCartas.indexOf(cartaInferiorBaja);
			indiceSuperiorAlto = ordenCartas.indexOf(cartaSuperiorAlta);
			indiceSuperiorBajo = ordenCartas.indexOf(cartaSuperiorBaja);
		}

		for (int i = indiceInferiorAlto; i <= indiceSuperiorAlto; i++) {
			char cartaAlta = ordenCartas.charAt(i);

			int inicioBajo = (i == indiceInferiorAlto) ? indiceInferiorBajo : 0;
			int finBajo = (i == indiceSuperiorAlto) ? indiceSuperiorBajo : (ordenCartas.length() - 1);

			for (int j = inicioBajo; j <= finBajo; j++) {
				char cartaBaja = ordenCartas.charAt(j);
				if (cartaAlta != cartaBaja) {
					if (suited) {
						rangos.add(cartaAlta + String.valueOf(cartaBaja) + "s");
					} else if (offSuited) {
						rangos.add(cartaAlta + String.valueOf(cartaBaja) + "o");
					} else {
						rangos.add(cartaAlta + String.valueOf(cartaBaja));
					}
				}
			}
		}
	}

////////////////////////////////////////////////////////////////////////////////////

	public String obtenerAbreviacionRango(Set<String> cartas) {
		StringBuilder resultado = new StringBuilder();
		Set<String> cartasProcesadas = new HashSet<>();

		for (char c : ordenCartas.toCharArray()) {
			String par = String.valueOf(c) + c;
			if (cartas.contains(par)) {
				if (esRangoCompleto(cartas, par, true)) {
					resultado.append(par).append("+,");
					cartasProcesadas.add(par);
					break;
				}
			}
		}

		for (int i = ordenCartas.length() - 1; i >= 0; i--) {
			for (int j = i - 1; j >= 0; j--) {
				char cartaAlta = ordenCartas.charAt(i);
				char cartaBaja = ordenCartas.charAt(j);
				String combinacionSuited = "" + cartaAlta + cartaBaja + "s";
				String combinacionOffsuited = "" + cartaAlta + cartaBaja + "o";

				if (cartas.contains(combinacionSuited) && !cartasProcesadas.contains(combinacionSuited)) {
					procesarCombinacion(cartas, cartasProcesadas, resultado, combinacionSuited, true);
				}
				if (cartas.contains(combinacionOffsuited) && !cartasProcesadas.contains(combinacionOffsuited)) {
					procesarCombinacion(cartas, cartasProcesadas, resultado, combinacionOffsuited, false);
				}
			}
		}

		if (resultado.length() > 0 && resultado.charAt(resultado.length() - 1) == ',') {
			resultado.setLength(resultado.length() - 1);
		}

		return resultado.toString();
	}

	private void procesarCombinacion(Set<String> cartas, Set<String> cartasProcesadas, StringBuilder resultado, String combinacion, boolean esSuited) {
		if (esRangoCompleto(cartas, combinacion, false)) {
			resultado.append(combinacion.charAt(0)).append(combinacion.charAt(1)).append("+,");
			cartasProcesadas.addAll(obtenerRangoCompleto(combinacion, false));
		} else {
			String rangoIntervalo = obtenerRangoIntervalo(cartas, combinacion, esSuited);
			if (!rangoIntervalo.isEmpty()) {
				resultado.append(rangoIntervalo).append(",");
				cartasProcesadas.addAll(obtenerCartasEnIntervalo(rangoIntervalo));
			} else {
				resultado.append(combinacion).append(",");
				cartasProcesadas.add(combinacion);
			}
		}
	}

	private boolean esRangoCompleto(Set<String> cartas, String combinacion, boolean esPar) {
		char cartaAlta = combinacion.charAt(0);
		char cartaBaja = combinacion.charAt(1);
		String tipo = esPar ? "" : combinacion.substring(2);

		for (int i = ordenCartas.indexOf(cartaBaja); i <= ordenCartas.indexOf(cartaAlta) - 1; i++) {
			String carta = "" + cartaAlta + ordenCartas.charAt(i) + tipo;
			if (!cartas.contains(carta)) {
				return false;
			}
		}
		return true;
	}

	private Set<String> obtenerRangoCompleto(String combinacion, boolean esPar) {
		Set<String> rango = new HashSet<>();
		char cartaAlta = combinacion.charAt(0);
		char cartaBaja = combinacion.charAt(1);
		String tipo = esPar ? "" : combinacion.substring(2);

		for (int i = ordenCartas.indexOf(cartaBaja); i < ordenCartas.indexOf(cartaAlta); i++) {
			rango.add("" + cartaAlta + ordenCartas.charAt(i) + tipo);
		}
		return rango;
	}

	private String obtenerRangoIntervalo(Set<String> cartas, String combinacion, boolean esSuited) {
		char cartaAlta = combinacion.charAt(0);
		char cartaBaja = combinacion.charAt(1);
		String tipo = esSuited ? "s" : "o";

		char cartaLimiteInferior = cartaBaja;
		for (int i = ordenCartas.indexOf(cartaBaja) - 1; i >= 0; i--) {
			String carta = "" + cartaAlta + ordenCartas.charAt(i) + tipo;
			if (!cartas.contains(carta)) {
				break;
			}
			cartaLimiteInferior = ordenCartas.charAt(i);
		}

		if (cartaLimiteInferior != cartaBaja) {
			return cartaAlta + "" + cartaLimiteInferior + tipo + "-" + cartaAlta + cartaBaja + tipo;
		}
		return combinacion;
	}

	private Set<String> obtenerCartasEnIntervalo(String rangoIntervalo) {
		Set<String> cartas = new HashSet<>();
		String[] limites = rangoIntervalo.split("-");
		char cartaAlta = limites[0].charAt(0);
		char cartaBajaInicial = limites[0].charAt(1);
		char cartaBajaFinal = limites[1].charAt(1);
		String tipo = limites[0].substring(2);

		for (int i = ordenCartas.indexOf(cartaBajaInicial); i <= ordenCartas.indexOf(cartaBajaFinal); i++) {
			cartas.add("" + cartaAlta + ordenCartas.charAt(i) + tipo);
		}
		return cartas;
	}

	public Set<String> getRangos() {
		return rangos;
	}

	public double getPorcentaje() {
		return porcentaje;
	}

	private int getRankValue(char rank) {
		return ordenCartas.indexOf(rank);
	}

	public void actualizarPorcentaje(double nuevoPorcentaje) {
		this.porcentaje = nuevoPorcentaje;
	}
}