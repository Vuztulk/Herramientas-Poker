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

            switch (apartado) {
                case "1":
                    //resultados = procesarApartado1(manos);
                    break;
                case "2":
                    //resultados = procesarApartado2(manos);
                    break;
                case "3":
                    //resultados = procesarApartado3(manos);
                    break;
                default:
                    throw new IllegalArgumentException("Apartado no válido");
            }

            vista.escribirArchivo(archivoSalida, resultados);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*private List<String> procesarApartado1(List<String> manos) {
        List<String> resultados = new ArrayList<>();
        for (String mano : manos) {
            List<Carta> cartas = modelo.parsearCartas(mano);
            String mejorMano = modelo.evaluarMejorMano(cartas);
            List<String> draws = modelo.detectarDraws(cartas);

            StringBuilder resultado = new StringBuilder("Mejor mano: ").append(mejorMano).append("\n");
            for (String draw : draws) {
                resultado.append("Draw: ").append(draw).append("\n");
            }
            resultados.add(resultado.toString());
        }
        return resultados;
    }

    private List<String> procesarApartado2(List<String> manos) {
        List<String> resultados = new ArrayList<>();
        for (String mano : manos) {
            String[] partes = mano.split(";");
            List<Carta> cartasPropias = modelo.parsearCartas(partes[0]);
            int numCartasComunes = Integer.parseInt(partes[1]);
            List<Carta> cartasComunes = modelo.parsearCartas(partes[2]);

            String mejorMano = modelo.evaluarMejorManoConComunes(cartasPropias, cartasComunes);
            StringBuilder resultado = new StringBuilder("Mejor mano: ").append(mejorMano).append("\n");

            if (numCartasComunes < 5) {
                List<String> draws = modelo.detectarDrawsConComunes(cartasPropias, cartasComunes);
                for (String draw : draws) {
                    resultado.append("Draw: ").append(draw).append("\n");
                }
            }

            resultados.add(resultado.toString());
        }
        return resultados;
    }

    private List<String> procesarApartado3(List<String> manos) {
        List<String> resultados = new ArrayList<>();
        for (String mano : manos) {
            String[] partes = mano.split(";");
            int numJugadores = Integer.parseInt(partes[0]);

            List<List<Carta>> cartasJugadores = new ArrayList<>();
            for (int i = 1; i <= numJugadores; i++) {
                cartasJugadores.add(modelo.parsearCartas(partes[i]));
            }

            List<Carta> cartasComunes = modelo.parsearCartas(partes[numJugadores + 1]);
            List<String> rankingJugadores = modelo.ordenarJugadoresPorMejorMano(cartasJugadores, cartasComunes);

            for (String resultadoJugador : rankingJugadores) {
                resultados.add(resultadoJugador);
            }
            resultados.add("\n");
        }
        return resultados;
    }*/
}
