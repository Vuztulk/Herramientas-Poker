package Controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Modelo.Model;
import Vista.View;
import Modelo.Carta;

public class Controller {

    private final Model modelo;
    private final View vista;

    public Controller(Model modelo, View vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void procesarArchivo(String apartado, String archivoEntrada, String archivoSalida) {
        try {
            List<String> manos = vista.leerArchivo(archivoEntrada);
            List<String> resultados = new ArrayList<>();

            for (String mano : manos) {
                List<Modelo.Carta> cartas = modelo.parsearCartas(mano);
                String mejorMano = modelo.evaluarMejorMano(cartas);
                List<String> draws = modelo.detectarDraws(cartas);

                StringBuilder resultado = new StringBuilder("Mejor mano: ").append(mejorMano).append("\n");
                for (String draw : draws) {
                    resultado.append("Draw: ").append(draw).append("\n");
                }
                resultados.add(resultado.toString());
            }

            vista.escribirArchivo(archivoSalida, resultados);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
