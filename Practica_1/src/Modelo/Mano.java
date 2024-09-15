package Modelo;

import java.util.List;

public class Mano {
	private List<Carta> cartas;
	private TipoMano tipoMano;
	private String descripcionMano;
	private int valor;

	public Mano(List<Carta> cartas) {
		this.cartas = cartas;
		this.tipoMano = UtilidadesMano.evaluarTipoMano(cartas);
		this.descripcionMano = UtilidadesMano.obtenerDescripcionMano(cartas);
		this.valor = evaluarValorMano();
	}

	public TipoMano getTipoMano() {
		return tipoMano;
	}

	public String getDescripcionMano() {
		return descripcionMano;
	}

	public int getValor() {
		return valor;
	}

	public int evaluarValorMano() {
		return calcularSumaCartas() + calcularValorMano();
	}

	private int calcularSumaCartas() {

		int suma = 0;
		for (int i = 0; i < 5 && i < cartas.size(); i++) {
			suma += cartas.get(i).getValor();
		}
		return suma;
	}

	private int calcularValorMano() { //Usamos valores grandes para evitar que la suma de cartas se puedan colar en otro rango que no es el suyo
		switch (tipoMano) {
		case ESCALERA_COLOR:
			return 9000;
		case COLOR:
			return 6000;
		case ESCALERA:
			return 5000;
		case POKER:
			return 8000;
		case FULL:
			return 7000;
		case TRIO:
			return 4000;
		case PAREJA:
			return 2000;
		case CARTA_MAS_ALTA:
			return 1000;
		default:
			return 0;
		}
	}
	
	public String obtenerCartasComoString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cartas.size(); i++) {
            sb.append(cartas.get(i).toString());
        }
        return sb.toString();
    }
}
