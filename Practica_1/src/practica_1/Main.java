import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Uso: java PokerHandEvaluator <numeroApartado> <entrada.txt> <salida.txt>");
            return;
        }

        String apartado = args[0];
        String inputFile = args[1];
        String outputFile = args[2];
a
        try {
            List<String> manos = leerArchivo(inputFile);
            List<String> resultados = new ArrayList<>();

            switch (apartado) {
                case "1":
                    for (String mano : manos) {
                        String resultado = evaluarManoCincoCartas(mano);
                        resultados.add(resultado);
                    }
                    break;
                case "2":
                    // Lógica para el apartado 2
                    break;
                case "3":
                    // Lógica para el apartado 3
                    break;
            }

            escribirArchivo(outputFile, resultados);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> leerArchivo(String file) throws IOException {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        }
        return lineas;
    }

    private static void escribirArchivo(String nombreArchivo, List<String> contenido) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (String linea : contenido) {
                bw.write(linea);
                bw.newLine();
            }
        }
    }

    private static String evaluarManoCincoCartas(String mano) {
        // Aquí iría la lógica para evaluar la mano
        return "Resultado para la mano: " + mano;
    }
}
