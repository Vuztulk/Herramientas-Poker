package App;

import javax.swing.SwingUtilities;
import Controlador.Controlador;
import Modelo.Modelo;
import Vista.Vista;

public class Main {
    public static void main(String[] args) {
        Modelo modelo = new Modelo();
        
        SwingUtilities.invokeLater(() -> {
            Controlador controlador = new Controlador(modelo, null);
            Vista vista = new Vista(controlador);
            controlador.setVista(vista);
        });
    }
}
