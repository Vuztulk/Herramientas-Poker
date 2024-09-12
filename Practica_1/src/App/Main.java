package App;

import javax.swing.SwingUtilities;
import GUI.GUI;
import GUI.Interfaz;
import Controlador.Controller;
import Modelo.Model;
import Vista.View;

public class Main {
    public static void main(String[] args) {
        /*SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Interfaz().setVisible(true);
            }
        });*/
    	Model model = new Model();
    	View vista = new View();
    	Controller control = new Controller(model, vista);
    	control.procesarArchivo("1","entrada1.txt", "salida1.txt");
    	
    }
}
