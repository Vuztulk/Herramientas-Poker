package App;

import javax.swing.SwingUtilities;
import Controlador.Controlador;
import Modelo.Modelo;
import Vista.MenuJugadores;

public class Main {
    public static void main(String[] args) {
        Modelo modelo = new Modelo();
        
        SwingUtilities.invokeLater(() -> {
            Controlador controlador = new Controlador(modelo, null);
            MenuJugadores vista = new MenuJugadores(controlador);
            controlador.setVista(vista);
        });
    }
}
