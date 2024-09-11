package App;

import Controlador.Controller;
import Modelo.Model;
import Vista.GUI;

public class Main {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Uso: java Main <archivoEntrada> <archivoSalida>");
            return;
        }

        String archivoEntrada = args[1];
        String archivoSalida = args[2];

        Model modelo = new Model();
        GUI vista = new GUI();
        Controller controlador = new Controller(modelo, vista);

        controlador.procesarArchivo(archivoEntrada, archivoSalida);
    }
}
