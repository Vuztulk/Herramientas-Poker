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

//TRANSFORMA DE TEXTO A GRAFICO	

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
			} else if (parte.length() == 2) {
				if (parte.charAt(0) == parte.charAt(1)) {
					rangos.add(parte);
				} else {
					rangos.add(parte + "s");
					rangos.add(parte + "o");
				}
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
				} else {// Caso de que el intervalo sean de pares
					rangos.add(cartaAlta + String.valueOf(cartaBaja));
				}
			}
		}
	}

////TRANSFORMA DE GRAFICO A TEXTO

	public String obtenerAbreviacionRango(Set<String> cartas) {
		StringBuilder resultado = new StringBuilder();
		Set<String> cartasProcesadas = new HashSet<>();

		procesarPares(cartas, cartasProcesadas, resultado);
		procesarCombinaciones(cartas, cartasProcesadas, resultado, "s");
		procesarCombinaciones(cartas, cartasProcesadas, resultado, "o");

		if (resultado.length() > 0 && resultado.charAt(resultado.length() - 1) == ',') {
			resultado.setLength(resultado.length() - 1);
		}

		return resultado.toString();
	}

	private void procesarPares(Set<String> cartas, Set<String> cartasProcesadas, StringBuilder resultado) {
		String rangoInicio = null, rangoFin = null;
		boolean incluyeAA = false;

		for (int i = ordenCartas.length() - 1; i >= 0; i--) {
			String par = String.valueOf(ordenCartas.charAt(i)) + ordenCartas.charAt(i);

			if (cartas.contains(par)) {
				if (rangoInicio == null)
					rangoInicio = par;
				rangoFin = par;
				cartasProcesadas.add(par);
				if (par.equals("AA"))
					incluyeAA = true;
			} else if (rangoInicio != null) {
				agregarRango(resultado, rangoFin, rangoInicio, incluyeAA);
				rangoInicio = null;
			}
		}

		if (rangoInicio != null)
			agregarRango(resultado, rangoFin, rangoInicio, incluyeAA);
	}

	private void procesarCombinaciones(Set<String> cartas, Set<String> cartasProcesadas, StringBuilder resultado,
			String tipo) {

		for (int i = ordenCartas.length() - 1; i >= 1; i--) {
			char cartaAlta = ordenCartas.charAt(i);
			boolean filaCompleta = true;
			String cartaMasBaja = null;

			for (int j = 0; j < i; j++) {
				char cartaBaja = ordenCartas.charAt(j);
				String combinacion = "" + cartaAlta + cartaBaja + tipo;

				if (cartas.contains(combinacion) && !cartasProcesadas.contains(combinacion)) {
					if (cartaMasBaja == null)
						cartaMasBaja = combinacion;
				} else {
					filaCompleta = false;
				}
			}

			if (filaCompleta && cartaMasBaja != null) {
				resultado.append(cartaMasBaja).append("+,");
			} else {
				agregarRangoCombinacion(cartas, cartasProcesadas, resultado, cartaAlta, tipo);
			}
		}
	}

	private void agregarRangoCombinacion(Set<String> cartas, Set<String> cartasProcesadas, StringBuilder resultado,
			char cartaAlta, String tipo) {
		String rangoInicio = null, rangoFin = null;

		for (int j = 0; j < ordenCartas.indexOf(cartaAlta); j++) {
			char cartaBaja = ordenCartas.charAt(j);
			String combinacion = "" + cartaAlta + cartaBaja + tipo;

			if (cartas.contains(combinacion) && !cartasProcesadas.contains(combinacion)) {
				if (rangoInicio == null)
					rangoInicio = combinacion;
				rangoFin = combinacion;
				cartasProcesadas.add(combinacion);
			} else if (rangoInicio != null) {
				agregarRango(resultado, rangoInicio, rangoFin, false);
				rangoInicio = null;
			}
		}

		if (rangoInicio != null)
			agregarRango(resultado, rangoInicio, rangoFin, false);
	}

	private void agregarRango(StringBuilder resultado, String rangoInicio, String rangoFin, boolean incluyeAA) {
		if (incluyeAA && !rangoInicio.equals(rangoFin)) {
			resultado.append(rangoInicio).append("+,");
		} else if (rangoInicio.equals(rangoFin)) {
			resultado.append(rangoInicio).append(",");
		} else if (esRangoConsecutivoPares(rangoInicio, rangoFin)) {
			resultado.append(rangoFin).append("-").append(rangoInicio).append(",");
		} else if (esRangoConsecutivo(rangoInicio, rangoFin)) {
			resultado.append(rangoFin).append("-").append(rangoInicio).append(",");
		} else {
			resultado.append(rangoFin).append("+,");
		}
	}

	private boolean esRangoConsecutivo(String rangoInicio, String rangoFin) {
		int indiceInicio = ordenCartas.indexOf(rangoInicio.charAt(1));
		int indiceFin = ordenCartas.indexOf(rangoFin.charAt(1));
		return indiceFin - indiceInicio != ordenCartas.indexOf(rangoFin.charAt(0)) - 1;
	}

	private boolean esRangoConsecutivoPares(String rangoInicio, String rangoFin) {
		int indiceInicio1 = ordenCartas.indexOf(rangoInicio.charAt(0));
		int indiceInicio2 = ordenCartas.indexOf(rangoInicio.charAt(1));
	    int indiceFin1 = ordenCartas.indexOf(rangoFin.charAt(0));
	    int indiceFin2 = ordenCartas.indexOf(rangoFin.charAt(1));
	    boolean es_par = indiceInicio1 == indiceInicio2 && indiceFin1 == indiceFin2;
	    return es_par && indiceInicio1 != indiceFin1;
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