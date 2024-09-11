package Vista;

import java.io.*;
import java.util.List;

public class View {

    public List<String> leerArchivo(String nombreArchivo) throws IOException {
        List<String> lineas;
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            lineas = br.lines().toList();
        }
        return lineas;
    }

    public void escribirArchivo(String nombreArchivo, List<String> contenido) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (String linea : contenido) {
                bw.write(linea);
                bw.newLine();
            }
        }
    }

    public void mostrarResultados(String mejorMano, List<String> draws) {
        System.out.println("Mejor mano: " + mejorMano);
        for (String draw : draws) {
            System.out.println("Draw: " + draw);
        }
    }
}
