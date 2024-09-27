package App;

import Controlador.Controlador;
import Modelo.Modelo;
import Vista.Vista;

public class Main {
    public static void main(String[] args) {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        Controlador controlador = new Controlador(modelo, vista);
    }
}
