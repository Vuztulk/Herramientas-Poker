package Modelo;


public class Jugador {
    private final String identificador;
    private Mano mano;

    public Jugador(String identificador, Mano mano) {
        this.identificador = identificador;
        this.mano = mano;
    }

    public String getIdentificador() {
        return identificador;
    }

    public Mano getMano() {
        return mano;
    }
}
